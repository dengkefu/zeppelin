package org.apache.zeppelin.rest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;
import org.apache.zeppelin.annotation.ZeppelinApi;
import org.apache.zeppelin.bean.Zrole;
import org.apache.zeppelin.bean.ZrolePermission;
import org.apache.zeppelin.rest.message.ZrolePermissionRequest;
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
 * 用户角色管理
 * @author weigfly
 */
@Path("/zrole")
@Produces("application/json")
@Singleton
public class ZroleRestApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZroleRestApi.class);
	private JdbcRealm obj;
	
	@Inject
	public ZroleRestApi() {
		Collection<Realm> realmsList = ShiroUtil.getRealmsList();
		obj = (JdbcRealm) realmsList.toArray()[0];
	}
	
	@GET
	@ZeppelinApi
	public Response listRoles() {
		LOGGER.info("----------------------获取角色列表-----------------------------------");
		List<Zrole> zrolelist = new ArrayList<Zrole>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select id,role,description,available from roles";
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
				Zrole role = new Zrole();
				role.setId(rs.getString(1));
				role.setRole(rs.getString(2));
				role.setDescription(rs.getString(3));
				role.setAvailable(rs.getBoolean(4));
				zrolelist.add(role);
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
	@Path("info/{roleId}")
	@ZeppelinApi
	public Response getRoleById(@PathParam("roleId") String roleId) {
		LOGGER.info("----------------------获取角色信息-----------------------------------");
		Zrole zrole = new Zrole();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select id,role,description,available from roles where id = '"+roleId+"'";
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
	
	
	@DELETE
	@Path("info/{roleId}")
	@ZeppelinApi
	public Response removeInfo(@PathParam("roleId") String roleId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "delete from roles where id = '"+roleId+"'";
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
	@Path("info/{roleId}")
	@ZeppelinApi
	public Response updateInfo(String message, @PathParam("roleId") String roleId) {
		Zrole role = Zrole.fromJson(message);
		if (role == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "update roles " + 
					"SET role='"+role.getRole()+"', description='"+role.getDescription()
							+ "', available="+role.isAvailable()
					+" where id = '"+roleId+"'";
			
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
	
	@POST
	@Path("info")
	@ZeppelinApi
	public Response addNewRole(String message) {
		LOGGER.info("----------------------添加角色信息-----------------------------------");
		Zrole role = Zrole.fromJson(message);
		if (role == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "INSERT INTO roles\n" + 
					"(id,role, description, available)" + 
					"VALUES('"+ShiroUtil.getRandomString(10)+"', '"+role.getRole()+"', '"
					+role.getDescription()+"',"
					+role.isAvailable()+");";
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
	
	@GET
	@Path("info/{roleId}/permission")
	@ZeppelinApi
	public Response getUserRoleById(@PathParam("roleId") String roleId) {
		LOGGER.info("----------------------获取角色权限列表信息-----------------------------------");
		List<ZrolePermission> list = new ArrayList<ZrolePermission>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select a.role_id,a.role_name,a.permission_id,b.permission_name,b.permission_uri,"
					+ "b.permission_method,b.description,b.available from roles_permissions a "
					+ "left join permissions b on a.permission_id = b.id where a.role_id = '"+roleId+"'";
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
				ZrolePermission ur = new ZrolePermission();
				ur.setRoleId(rs.getString(1));ur.setRoleName(rs.getString(2));ur.setPermissionId(rs.getString(3));
				ur.setPermissionName(rs.getString(4));ur.setPermissionUri(rs.getString(5));ur.setPermissionMethod(rs.getString(6));
				ur.setPermissionDesc(rs.getString(7));ur.setPermissionAvailable(rs.getBoolean(8));
				list.add(ur);
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
		return new JsonResponse<>(Status.OK, "success", list).build();
	}
	
	@DELETE
	@Path("info/{roleId}/permission/{permissionId}")
	@ZeppelinApi
	public Response deleteZrolePermission(@PathParam("roleId") String roleId,@PathParam("permissionId") String permissionId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "delete from roles_permissions where role_id = '"+roleId+"' and permission_id = '"+permissionId+"'";
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
	
	@POST
	@Path("info/permission")
	@ZeppelinApi
	public Response addZrolePermission(String message) {
		ZrolePermissionRequest ur = ZrolePermissionRequest.fromJson(message);
		if (ur == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "INSERT INTO roles_permissions\n" + 
					"(role_id, role_name, permission_id, permission)\n" + 
					"VALUES('"+ur.getRoleId()+"', '"+ur.getRoleName()+"', '"
					+ur.getPermissionId()+"', '"
					+ur.getPermissionName()+"');";
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
