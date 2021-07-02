package org.apache.zeppelin.rest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;
import org.apache.zeppelin.annotation.ZeppelinApi;
import org.apache.zeppelin.bean.Zorganization;
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
 * @author weigfly
 * 组织机构接口服务
 */
@Path("/zorg")
@Produces("application/json")
@Singleton
public class ZorgRestApi {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZorgRestApi.class);
	private JdbcRealm obj;
	
	@Inject
	public ZorgRestApi() {
		Collection<Realm> realmsList = ShiroUtil.getRealmsList();
		obj = (JdbcRealm) realmsList.toArray()[0];
	}
	
	
	/*
	 * 查询组织列表
	 */
	@GET
	@ZeppelinApi
	public Response listZorgs() {
		LOGGER.info("----------------------获取组织机构列表-----------------------------------");
		List<Zorganization> list = new ArrayList<Zorganization>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select id,org_name,parent_id,org_desc,available from organization";
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
				Zorganization org = new Zorganization();
				org.setOrgId(rs.getString(1));
				org.setOrgName(rs.getString(2));
				org.setParentId(rs.getString(3));
				org.setOrgDesc(rs.getString(4));
				org.setAvailable(rs.getBoolean(5));
				list.add(org);
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
	
	@GET
	@Path("info/{orgId}")
	@ZeppelinApi
	public Response getOrgById(@PathParam("infoId") String orgId) {
		LOGGER.info("----------------------获取组织信息-----------------------------------");
		Zorganization org = new Zorganization();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select id,org_name,parent_id,org_desc,available from organization where id = '"+orgId+"'";
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
				org.setOrgId(rs.getString(1));
				org.setOrgName(rs.getString(2));
				org.setParentId(rs.getString(3));
				org.setOrgDesc(rs.getString(4));
				org.setAvailable(rs.getBoolean(5));
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
		return new JsonResponse<>(Status.OK, "success", org).build();
	}
	
	@POST
	@Path("info")
	public Response newZorg(String message) {
		Zorganization org = Zorganization.fromJson(message);
		if (org == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "INSERT INTO organization" + 
					"(id, org_name, parent_id, org_desc, available)" + 
					"VALUES('"+ShiroUtil.getRandomString(10)+"', '"+org.getOrgName()+"', '"
					+org.getParentId()+"', '"
					+org.getOrgDesc()+"', "
					+org.isAvailable()+")";
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
	@Path("info/{orgId}")
	@ZeppelinApi
	public Response updateInfo(String message, @PathParam("orgId") String orgId) {
		Zorganization org = Zorganization.fromJson(message);
		if (org == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "update organization " + 
					"SET org_name='"+org.getOrgName()+"', parent_id='"+org.getParentId()+""
							+ "', org_desc='"+org.getOrgDesc()+"', available=" + org.isAvailable()
					+" where id = '"+orgId+"'";
			
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
	@Path("info/{orgId}")
	@ZeppelinApi
	public Response removeInfo(@PathParam("orgId") String orgId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "delete from organization where id = '"+orgId+"'";
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
