package com.revature.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



import org.apache.log4j.Logger;

import com.revature.models.Role;
import com.revature.models.User;


public class GetDB {
	//private static Logger log = Logger.getLogger(GetDB.class);
	
	public ArrayList<User> getAll(Connection conn) throws SQLException{
		ArrayList<User> allUsers = new ArrayList<User>();
		
		assert conn != null;
		
		String select = "SELECT * FROM users";
		PreparedStatement ps = conn.prepareStatement(select);
		
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String email = rs.getString("email");
			String pwd = rs.getString("pwd");
			String user_role = rs.getString("user_role");
			Role role = null;
			
			if(user_role.equals("ADMIN")) {
				role = Role.ADMIN;
			} else if (user_role.equals("CUSTOMER")) {
				role = Role.CUSTOMER;
			} else {
		//		log.fatal("User role not found for user: " + email);
				System.out.println("re");
				System.exit(0);;
			}
			
			User holder = new User(id, firstName, lastName, email, pwd, role);
			System.out.println(holder.toString());
		//	log.info("All users have been pulled from the databse");
			allUsers.add(holder);
		}
		
		
		return allUsers;
	}

	/** Method aims to take the user's email and password, then assess if they can connect
	 * 
	 * @param conn --passed connection
	 * @param Uemail --user email
	 * @param Upassword --user password
	 * @return
	 * @throws SQLException
	 */
	public User getByUsername(Connection conn, String Uemail, String Upassword) throws SQLException{
		
		String preppedEmail = "'"+Uemail+"'";
		
		String sql = "SELECT * FROM users WHERE email = " + preppedEmail;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt("id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			String email = rs.getString("email");
			String pwd = rs.getString("pwd");
			String user_role = rs.getString("user_role");
			Role role = null;
			
			
			//assert role
			if(user_role.equals("ADMIN")) {
				role = role.ADMIN;
			} else if (user_role.equals("CUSTOMER")) {
				role = role.CUSTOMER;
			} else {
	//			log.fatal("User role not found for user: " + email);
				System.exit(0);;
			}
			
			//check to make sure passwords match
			if(pwd.equals(Upassword)) {
				System.out.println("Welcome " + firstName);
		//		log.info("User " + Uemail + " has logged in");
				
				//if they do match, return the user
				User u = new User(id, firstName, lastName, email, pwd, role);
				System.out.println(u.toString());
				
			} else {
		//		log.error("USER " + email + " HAS FAILED LOGIN, EXITING APPLICATION");
				System.exit(0);
			}
		
	}
		return null;
	}
	
			
}





