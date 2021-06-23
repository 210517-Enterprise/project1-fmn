package com.revature.repos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.util.ForeignKeyField;
import com.revature.util.Metamodel;


public class GetDB {
	private static Logger log = Logger.getLogger(GetDB.class);
	
	
	/** Method aims to retrieve all users from the databse
	 * 
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<User> getAllUsers(Connection conn, Metamodel<User> mm) throws SQLException{
		ArrayList<User> allUsers = new ArrayList<User>();
		
		String select = "SELECT * FROM " + mm.getTableName();
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
				log.fatal("User role not found for user: " + email);
				System.exit(0);;
			}
			
			User holder = new User(id, firstName, lastName, email, pwd, role);
			System.out.println(holder.toString());
			log.info("All users have been pulled from the databse");
			allUsers.add(holder);
		}
		
		log.info("All Users have been pulled from the database!");
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
	public User getByUserID(Connection conn, Metamodel<User> mm, int userID) throws SQLException{
		
		String sql = "SELECT * FROM " + mm.getTableName() + " WHERE " + mm.getPrimaryKey().getColumnName() + " = " + userID;
		
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
				log.fatal("User role not found for user: " + email);
				System.exit(0);;
			}
			
			User holder = new User(id, firstName, lastName, email, pwd, role);
			log.info("Retrieved user: " + firstName + " from the database!");
			return holder;
	}
		log.error("No user found with id: " + userID);
		return null;
		
	}	
	
	/**
	 * getAllOrders aims to retrieve all orders and return them as an arraylist
	 * 
	 * @param conn
	 * @param om
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Order> getAllOrders(Connection conn, Metamodel<Order> om) throws SQLException{
		ArrayList<Order> orders = new ArrayList<Order>();
			
		String sql = "SELECT * FROM " + om.getTableName();
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
			
		while(rs.next()) {
			int id = rs.getInt(1);
			int userID = rs.getInt(2);
			int productID = rs.getInt(3);
			Date date = rs.getDate(4);
			int priceTotal = rs.getInt(5);
			boolean isFulfilled = rs.getBoolean(6);
			int quantity = rs.getInt(7);
			
			Order o = new Order(id, userID, productID, date, priceTotal, isFulfilled, quantity);
			orders.add(o);
		}
		log.info("Retrieved all orders from the database!");
		return orders;
	}

	
	/**
	 * gets all orders associated with a user via primary key
	 * @param conn
	 * @param om
	 * @param orderID
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Order> getUserOrdersByPrimaryKey(Connection conn, Metamodel<Order> om, int orderID) throws SQLException {
		ArrayList<Order> o = new ArrayList<Order>();
			
		String sql = "SELECT * FROM " + om.getTableName() + " where " + om.getPrimaryKey().getColumnName() + " = " + orderID;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			int uID = rs.getInt(2);
			int productID = rs.getInt(3);
			Date date = rs.getDate(4);
			int priceTotal = rs.getInt(5);
			boolean isFulfilled = rs.getBoolean(6);
			int quantity = rs.getInt(7);
			
			Order order = new Order(id, uID, productID, date, priceTotal, isFulfilled, quantity);
			o.add(order);
		}	
		log.info("Retrieved " + o.size() + " orders from orderID: " + orderID);
		return o;
	}
	
	/**
	 * gets all orders associated with the user via userID
	 * @param conn
	 * @param om
	 * @param userID
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Order> getUserOrdersByUserID(Connection conn, Metamodel<Order> om, int userID) throws SQLException {
		ArrayList<Order> o = new ArrayList<Order> ();
		
		String sql = "SELECT * FROM " + om.getTableName() + " where " + om.getForeignKeys().get(0).getColumnName() + " = " + userID;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			int uID = rs.getInt(2);
			int productID = rs.getInt(3);
			Date date = rs.getDate(4);
			int priceTotal = rs.getInt(5);
			boolean isFulfilled = rs.getBoolean(6);
			int quantity = rs.getInt(7);
			
			Order order = new Order(id, uID, productID, date, priceTotal, isFulfilled, quantity);
			o.add(order);
		}	
		log.info("Retrieved " + o.size() + " Orders from the server.");
		return o;
		
	}
	
	/**
	 * gets all orders associated with the product id
	 * @param conn
	 * @param om
	 * @param userID
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Order> getUserOrdersByProductID(Connection conn, Metamodel<Order> om, int userID) throws SQLException {
		ArrayList<Order> o = new ArrayList<Order> ();
		
		String sql = "SELECT * FROM " + om.getTableName() + " WHERE " + om.getForeignKeys().get(1).getColumnName() + " = " + userID;
	
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			int uID = rs.getInt(2);
			int productID = rs.getInt(3);
			Date date = rs.getDate(4);
			int priceTotal = rs.getInt(5);
			boolean isFulfilled = rs.getBoolean(6);
			int quantity = rs.getInt(7);
			
			Order order = new Order(id, uID, productID, date, priceTotal, isFulfilled, quantity);
			o.add(order);
		}	
		log.info("Retrieved Orders associated with userID: " + userID);
		return o;
		
	}
	
	/**
	 * Gets all category entries in the current database
	 * @param conn
	 * @param cm
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Category> getAllCategories(Connection conn, Metamodel<Category> cm) throws SQLException{
		ArrayList<Category> list = new ArrayList<Category>();
		
		String sql = "SELECT * FROM " + cm.getTableName();
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			String categoryName = rs.getString(2);
			
			Category c = new Category(id, categoryName);
			list.add(c);
		}
		log.info("Retrieved all Categories from the Database!");
		return list;
	}	
	
	/**
	 * Retrieves all categories associated with the primary key
	 * @param conn
	 * @param cm
	 * @param pk
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Category> getAllCategoriesByPrimaryKey(Connection conn, Metamodel<Category> cm, int pk) throws SQLException {
		ArrayList<Category> list = new ArrayList<Category>();
		
		String sql = "SELECT * FROM " + cm.getTableName() + " WHERE " + cm.getPrimaryKey().getColumnName() + " = "+ pk;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			String categoryName = rs.getString(2);
			
			Category c = new Category(id, categoryName);
			list.add(c);
		}
		log.info("Retrieved all Categories associated with Primary key: " + pk);
		return list;
	}
	
	/**
	 * Gets all product entities in the Database
	 * @param conn
	 * @param pm
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Product> getAllProducts(Connection conn, Metamodel<Product> pm) throws SQLException {
		ArrayList<Product> list = new ArrayList<Product>();
		
		String sql = "SELECT * FROM " + pm.getTableName();
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			int cID = rs.getInt(2);
			String prodName = rs.getString(3);
			String prodDesc = rs.getString(4);
			int price = rs.getInt(5);
			int quantity = rs.getInt(6);
			boolean inStock = rs.getBoolean(7);
			
			Product p = new Product(id, cID, prodName, prodDesc, price, quantity, inStock);
			list.add(p);
		}	
		log.info("Retrieved All Products from the Database!");
		return list;
	}
	
	/**
	 * Gets all entities of products associated with given Primary Key
	 * @param conn
	 * @param pm
	 * @param PK
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Product> getAllProductsByPrimaryKey(Connection conn, Metamodel<Product> pm, int PK) throws SQLException {
		ArrayList<Product> list = new ArrayList<Product>();
		
		String sql = "SELECT * FROM " + pm.getTableName() + " WHERE " + pm.getPrimaryKey().getColumnName() + " = " + PK;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			int cID = rs.getInt(2);
			String prodName = rs.getString(3);
			String prodDesc = rs.getString(4);
			int price = rs.getInt(5);
			int quantity = rs.getInt(6);
			boolean inStock = rs.getBoolean(7);
			
			Product p = new Product(id, cID, prodName, prodDesc, price, quantity, inStock);
			list.add(p);
		}
		log.info("Retrieved all products associated with Primary Key: " + PK);
		return list;
	}
	
	/**
	 * Gets all entities of products associated with given Foreign Key
	 * @param conn
	 * @param pm
	 * @param FK
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Product> getAllProductsByForeignKey(Connection conn, Metamodel<Product> pm, int FK) throws SQLException {
		ArrayList<Product> list = new ArrayList<Product>();
		
		String sql = "SELECT * FROM " + pm.getTableName() + " WHERE " + pm.getForeignKeys().get(0).getColumnName() + " = " + FK;
		
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()) {
			int id = rs.getInt(1);
			int cID = rs.getInt(2);
			String prodName = rs.getString(3);
			String prodDesc = rs.getString(4);
			int price = rs.getInt(5);
			int quantity = rs.getInt(6);
			boolean inStock = rs.getBoolean(7);
			
			Product p = new Product(id, cID, prodName, prodDesc, price, quantity, inStock);
			list.add(p);
		}
		log.info("Retrieved all products associated with Foreign Key " + FK);
		return list;
	}
	
	
	
	
	
}
	
	
	





