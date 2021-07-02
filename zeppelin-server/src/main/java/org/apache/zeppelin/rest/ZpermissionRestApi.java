package org.apache.zeppelin.rest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;
import org.apache.zeppelin.annotation.ZeppelinApi;
import org.apache.zeppelin.bean.Zrole;
import org.apache.zeppelin.bean.ZrolePermission;
import org.apache.zeppelin.rest.message.ZpermissionRequest;
import org.apache.zeppelin.server.JsonResponse;
import org.apache.zeppelin.utils.ShiroUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 权限资源接口
 * @author weigfly
 */
@Path("/zpermission")
@Produces("application/json")
@Singleton
public class ZpermissionRestApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZpermissionRestApi.class);
	private JdbcRealm obj;
	
	@Inject
	public ZpermissionRestApi() {
		Collection<Realm> realmsList = ShiroUtil.getRealmsList();
		obj = (JdbcRealm) realmsList.toArray()[0];
	}
	
	@GET
	@ZeppelinApi
	public Response listPermissions() {
		LOGGER.info("----------------------获取权限资源列表-----------------------------------");
		List<ZrolePermission> zrolelist = new ArrayList<ZrolePermission>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select id,permission_name,permission_uri,permission_method,description,available from permissions";
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		}
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				ZrolePermission p = new ZrolePermission();
				p.setPermissionId(rs.getString(1));
				p.setPermissionName(rs.getString(2));
				p.setPermissionUri(rs.getString(3));
				p.setPermissionMethod(rs.getString(4));
				p.setPermissionDesc(rs.getString(5));
				p.setPermissionAvailable(rs.getBoolean(6));
				zrolelist.add(p);
			}
		} catch (Exception e) {
			LOGGER.error("Error retrieving User list from JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(con);
		}
		return new JsonResponse<>(Status.OK, "success", zrolelist).build();
	}
	
	@GET
	@Path("info/{permissionId}")
	@ZeppelinApi
	public Response getRoleById(@PathParam("permissionId") String permissionId) {
		LOGGER.info("----------------------获取资源信息信息-----------------------------------");
		Zrole zrole = new Zrole();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select id,permission_name,permission_uri,permission_method,description,available from permissions where id = '"+permissionId+"'";
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		}
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				zrole.setId(rs.getString(1));
				zrole.setRole(rs.getString(2));
				zrole.setDescription(rs.getString(3));
				zrole.setAvailable(rs.getBoolean(4));
			}
		} catch (Exception e) {
			LOGGER.error("Error retrieving User list from JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(con);
		}
		return new JsonResponse<>(Status.OK, "success", zrole).build();
	}
	
	
	@POST
	@Path("info")
	@ZeppelinApi
	public Response addNewPermission(String message) {
		LOGGER.info("----------------------添加权限资源信息-----------------------------------");
		ZpermissionRequest  p = ZpermissionRequest.fromJson(message);
		if (p == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "INSERT INTO permissions" + 
					"(id,permission_name, permission_uri,permission_method,description, available)" + 
					"VALUES('"+ShiroUtil.getRandomString(10)+"', '"+p.getPermissionName()+"', '"
					+p.getPermissionUri()+"','"
					+p.getPermissionMethod()+"','"
					+p.getDescription()+"',"
					+p.isAvailable()+");";
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		}
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			flag = ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error retrieving User list from JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		} finally {
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(con);
		}
			return new JsonResponse<>(Status.OK, "success", null).build();
	}

	@DELETE
	@Path("info/{permissionId}")
	@ZeppelinApi
	public Response deleteZrolePermission(@PathParam("permissionId") String permissionId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "delete from permissions where permission_id = '"+permissionId+"'";
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		}
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			flag = ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error retrieving User list from JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		} finally {
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(con);
		}
		return new JsonResponse<>(Status.OK, "success", null).build();
	}
	
	@PUT
	@Path("info/{permissionId}")
	@ZeppelinApi
	public Response updateInfo(String message, @PathParam("permissionId") String permissionId) {
		ZpermissionRequest  p = ZpermissionRequest.fromJson(message);
		if (p == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "update permissions " + 
					"SET permission_name='"+p.getPermissionName()+"', permission_uri='"+p.getPermissionUri()+""
							+ "', permission_method='"+p.getPermissionMethod()+"', available=" + p.isAvailable()
							+", description = '"+p.getDescription()+"' "
					+" where id = '"+permissionId+"'";
			
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		}
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			flag = ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error retrieving User list from JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		} finally {
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(con);
		}
		return new JsonResponse<>(Status.OK, "success", null).build();
	}

}
