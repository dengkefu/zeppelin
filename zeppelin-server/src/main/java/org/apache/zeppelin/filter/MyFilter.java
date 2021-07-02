package org.apache.zeppelin.filter;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.JdbcUtils;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.util.WebUtils;
import org.apache.zeppelin.bean.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.IOException;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 用户权限过滤器
 * @author weigfly
 */
public class MyFilter extends JdbcRealm  implements Filter {
	private final Logger LOGGER = LoggerFactory.getLogger(MyFilter.class);
	 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("--------------初始化过滤器----------------");
	}
	
	
	 private String extractPrincipal(Subject subject) {
		    String principal;
		    Object principalObject = subject.getPrincipal();
		    if (principalObject instanceof Principal) {
		      principal = ((Principal) principalObject).getName();
		    } else {
		      principal = String.valueOf(principalObject);
		    }
		    return principal;
	}

	
	public String getPrincipal() {
	    Subject subject = org.apache.shiro.SecurityUtils.getSubject();
	    String principal;
	    if (subject.isAuthenticated()) {
	      principal = extractPrincipal(subject);
	    } else {
	      principal = "anonymous";
	    }
	    return principal;
	  }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//1、获取当前访问地址
		//2、获取当前登陆用户的角色、权限
		//3、匹配当前用户是否拥有所访问的地址权限
		//4、返回结果
		boolean flag = false;
		HttpServletRequest httpRequest = ((HttpServletRequest) request);
		String method = httpRequest.getMethod();
		String uri = httpRequest.getPathInfo();
		LOGGER.info("--------------执行过滤----------------");
		
		  LOGGER.info(httpRequest.getMethod()+"------------------\n"+httpRequest.getPathInfo()+"-----------\n");
		  String userName = getPrincipal();
		  if(userName.equals("anonymous")) {
			  flag = true;
		  }else if(userName.equals("")){
			  flag = false;
		  }else {
			  Collection<Realm> realmsList = getRealmsList();
		      if (realmsList != null) {
		        for (Realm realm : realmsList) {
		          String realClassName = realm.getClass().getName();
		          LOGGER.debug("RealmClass.getName: " + realClassName);
		          if (realClassName.equals("org.apache.shiro.realm.jdbc.JdbcRealm")) {
		        	 
		        	  List<Permission> userPermissions = getUserPermission((JdbcRealm) realm,userName);
		        	  if(userPermissions.size() == 0) {
		        		  flag = false;
		        	  }else {
		        		  flag = isPermission(method,uri,userPermissions);
		        	  }
		          }
		        }
		      }
		  }
	    if(flag) {
	    	chain.doFilter(request, response);
	    }else {
	    	WebUtils.saveRequest(request);
	    	WebUtils.issueRedirect(request, response, "/api/login");
	    }
	}
	
	private boolean isPermission(String method,String uri,List<Permission> list) {
		for(Permission p : list) {
			if(uri.contains("?")) {///api/notebook/search?q=[query]暂时先过去
				return true;
			}
			String[] array2 = uri.split("/");// /helium/suggest/2F2YS7PCE/paragraph_1580998407443_936860398
			//  /zuser/info/123   /zuser/info
			List<String> a1 = Arrays.asList(p.getPermission_uri().split("/"));// /helium/suggest
			int n = 0;
			for(String u : array2) {
				if(a1.contains(u))
					n++;
			}
			if(n == a1.size() && p.getPermissionMethod().equals(method)) {
				return true;
			}
		}
		return false;
	}

	  public Collection<Realm> getRealmsList() {
		    String key = ThreadContext.SECURITY_MANAGER_KEY;
		    DefaultWebSecurityManager defaultWebSecurityManager = (DefaultWebSecurityManager) ThreadContext.get(key);
		    return defaultWebSecurityManager.getRealms();
	  }
	
	  private List<Permission> getUserPermission(JdbcRealm obj,String userName){
		  LOGGER.info("----------------------获取用户权限-----------------------------------");
			  List<Permission> permissionlist = new ArrayList<Permission>();
			    Connection con = null;
			    PreparedStatement ps = null;
			    ResultSet rs = null;
			    DataSource dataSource = null;
			    String rolequery;
			    try {
			      dataSource = (DataSource) FieldUtils.readField(obj, "dataSource", true);
			      rolequery = "select a.username,b.role_name,d.permission_method , "
			    		  +" d.permission_name ,d.permission_uri ,d.description from users a "
			    		  +" left join user_roles b on a.id  = b.user_id "
			    		  +" left join roles_permissions c on b.role_id = c.role_id "
			    		  +" left join permissions d on c.permission_id = d.id "
			    		  +" where a.username = '"+userName+"'";
			    } catch (IllegalAccessException e) {
			      LOGGER.error("Error while accessing dataSource for JDBC Realm", e);
			      return Lists.newArrayList();
			    }

			    try {
			      con = dataSource.getConnection();
			      ps = con.prepareStatement(rolequery);
			      rs = ps.executeQuery();
			      while (rs.next()) {
			    	  Permission p = new Permission();
			    	  p.setUserName(rs.getString(1));
			    	  p.setRoleName(rs.getString(2));
			    	  p.setPermissionMethod(rs.getString(3));
			    	  p.setPermission_name(rs.getString(4));
			    	  p.setPermission_uri(rs.getString(5));
			    	  p.setDescription(rs.getString(6));
			    	  LOGGER.info(p.toString());
			    	  permissionlist.add(p);
			      }
			    } catch (Exception e) {
			      LOGGER.error("Error retrieving User list from JDBC Realm", e);
			    } finally {
			      JdbcUtils.closeResultSet(rs);
			      JdbcUtils.closeStatement(ps);
			      JdbcUtils.closeConnection(con);
			    }
			    return permissionlist;
	  }

	@Override
	public void destroy() {
		LOGGER.info("--------------销毁过滤器----------------");
	}

}
