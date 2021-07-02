package org.apache.zeppelin.rest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.util.JdbcUtils;
import org.apache.zeppelin.annotation.ZeppelinApi;
import org.apache.zeppelin.bean.Zuser;
import org.apache.zeppelin.bean.ZuserRole;
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
 * 用户管理接口
 * @author weigfly
 */
@Path("/zuser")
@Produces("application/json")
@Singleton
public class ZUserRestApi {
	private static final Logger LOGGER = LoggerFactory.getLogger(ZUserRestApi.class);
	private JdbcRealm obj;
	
	@Inject
	public ZUserRestApi() {
		Collection<Realm> realmsList = ShiroUtil.getRealmsList();
		obj = (JdbcRealm) realmsList.toArray()[0];
	}

	/*
	 * 查询用户列表
	 */
	@GET
	@ZeppelinApi
	public Response listZusers() {
		LOGGER.info("----------------------获取用户列表-----------------------------------");
		List<Zuser> zuserlist = new ArrayList<Zuser>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select a.id,a.username,a.password,a.password_salt,a.locked,a.org_id,b.org_name from users a left join organization b on a.org_id = b.id";
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
				Zuser user = new Zuser();
				user.setId(rs.getString(1));
				user.setName(rs.getString(2));
				user.setPassword(rs.getString(3));
				user.setPassword_salt(rs.getString(4));
				user.setLocked(rs.getBoolean(5));
				user.setOrgId(rs.getString(6));
				user.setOrgName(rs.getString(7));
				zuserlist.add(user);
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
		return new JsonResponse<>(Status.OK, "success", zuserlist).build();
	}

	/*
	 * 根据用户ID查询用户信息
	 */
	@GET
	@Path("info/{infoId}")
	@ZeppelinApi
	public Response getUserById(@PathParam("infoId") String infoId) {
		LOGGER.info("----------------------获取用户信息-----------------------------------");
		Zuser zuser = new Zuser();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select a.id,a.username,a.password,a.password_salt,a.locked,a.org_id,b.org_name from users a left join organization b on a.org_id = b.id where a.id = '"+infoId+"'";
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
				zuser.setId(rs.getString(1));
				zuser.setName(rs.getString(2));
				zuser.setPassword(rs.getString(3));
				zuser.setPassword_salt(rs.getString(4));
				zuser.setLocked(rs.getBoolean(5));
				zuser.setOrgId(rs.getString(6));
				zuser.setOrgName(rs.getString(7));
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
		return new JsonResponse<>(Status.OK, "success", zuser).build();
	}
	
	/*
	 * 获取用户角色列表
	 */
	@GET
	@Path("info/{infoId}/role")
	@ZeppelinApi
	public Response getUserRoleById(@PathParam("infoId") String infoId) {
		LOGGER.info("----------------------获取用户角色信息-----------------------------------");
		List<ZuserRole> list = new ArrayList<ZuserRole>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "select user_id,username,role_id,role_name from user_roles where user_id = '"+infoId+"'";
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
			return new JsonResponse<>(Status.INTERNAL_SERVER_ERROR, e.getMessage(), ExceptionUtils.getStackTrace(e))
					.build();
		}
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {//select user_id,username,role_id,role_name from user_roles where user_id = '
				ZuserRole ur = new ZuserRole();
				ur.setUserId(rs.getString(1));
				ur.setUserName(rs.getString(2));
				ur.setRoleId(rs.getString(3));
				ur.setRoleName(rs.getString(4));
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
	
	/*
	 * 给用户赋角色
	 */
	@POST
	@Path("info/role")
	@ZeppelinApi
	public Response addZuserRole(String message) {
		ZuserRole ur = ZuserRole.fromJson(message);
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
			query = "INSERT INTO user_roles\n" + 
					"(user_id, username, role_id, role_name)\n" + 
					"VALUES('"+ur.getUserId()+"', '"+ur.getUserName()+"', '"
					+ur.getRoleId()+"', '"
					+ur.getRoleName()+"');";
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

	
	private boolean deleteZuserRole(String infoId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query = "";
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "delete from user_roles where user_id = '"+infoId+"'";
		} catch (IllegalAccessException e) {
			LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
		}
		try {
			con = dataSource.getConnection();
			ps = con.prepareStatement(query);
			flag = ps.execute();
		} catch (Exception e) {
			LOGGER.error("Error retrieving User list from JDBC Realm", e);
		} finally {
			JdbcUtils.closeStatement(ps);
			JdbcUtils.closeConnection(con);
		}
		return flag;
	}
	/*
	 * 添加用户
	 */
	@POST
	@Path("info")
	public Response newZuser(String message) {
		Zuser user = Zuser.fromJson(message);
		if (user == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "INSERT INTO users\n" + 
					"(id,username, password, password_salt, locked, org_id)" + 
					"VALUES('"+ShiroUtil.getRandomString(10)+"', '"+user.getName()+"', '"
					+user.getPassword()+"', '"
					+user.getPassword_salt()+"', "
					+user.isLocked()+",'"+user.getOrgId()+"');";
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
	@Path("info/{userId}")
	@ZeppelinApi
	public Response updateInfo(String message, @PathParam("userId") String userId) {
		Zuser user = Zuser.fromJson(message);
		if (user == null) {
			return new JsonResponse<>(Status.BAD_REQUEST).build();
		}
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "update users " + 
					"SET username='"+user.getName()+"', password='"+user.getPassword()+""
							+ "', password_salt='"+user.getPassword_salt()+"', locked=" + user.isLocked()
							+", org_id = '"+user.getOrgId()+"' "
					+" where id = '"+userId+"'";
			
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
	@Path("info/{userId}")
	@ZeppelinApi
	public Response removeInfo(@PathParam("userId") String userId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "delete from users where id = '"+userId+"'";
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
	@Path("info/{userId}/role/{roleId}")
	@ZeppelinApi
	public Response removeUserRole(@PathParam("userId") String userId,@PathParam("roleId") String roleId) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean flag = false;
		DataSource dataSource = null;
		String query;
		try {
			dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			query = "delete from users_roles where user_id = '"+userId+"' and role_id = '"+roleId+"'";
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
