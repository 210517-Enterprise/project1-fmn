package com.revature;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.repos.Session;
import com.revature.repos.Transaction;
import com.revature.util.Configuration;
import com.revature.util.ConnectionUtil;

public class Driver {

	public static final DataSource connPool = setUpConnection();
	public static Logger log = Logger.getLogger(Driver.class);
	public static Scanner scan = new Scanner(System.in);
	public static final User admin = new User(1, "Admin", "Admin", "admin@theshop.com", "secret", Role.ADMIN);
	public static final User customer = new User(2, "Frank", "Aurori", "faurori@gmail.com", "mypass123", Role.CUSTOMER);

	public static void main(String[] args) {
		initializeStore();

		// static login as an admin or a customer
		boolean keepRunning = true;
		while (keepRunning) {
			System.out.println("Would you like to explore functionality for an Admin or Customer?");
			String userLogin = scan.next();

			if (userLogin.equalsIgnoreCase("admin")) {
				runAdminFunctionality();
				keepRunning = continueOrQuit();
				
			}

			if (userLogin.equalsIgnoreCase("customer")) {
				runCustomerFunctionality();
				keepRunning = continueOrQuit();
			}

		}
		
		System.out.println("Thanks for visiting Daintree");

	}
	
	private static boolean continueOrQuit() {
		System.out.println("[1] Quit");
		System.out.println("[2] Explore different funnctionality");
		int request = scan.nextInt();
		
		if(request == 1)
			return false;
		else
			return true;
	}

	private static void initializeStore() {
		Session ses = new Session();
		Transaction tx = ses.getTransaction();

		ConnectionUtil util = new ConnectionUtil();
		util.printDBStatus();

		Configuration cfg = new Configuration();
		cfg.addAnnotatedClass(User.class);
		cfg.addAnnotatedClass(Category.class);
		cfg.addAnnotatedClass(Product.class);
		cfg.addAnnotatedClass(Order.class);

		try {
			// create tables
			if (ses.createTables(cfg, connPool.getConnection()))
				System.out.println("Tables for Daintree successfully created...");
			else
				log.error("Failure to create tables for Congo");

			util.printDBStatus();

			// insert users
			ses.insert(admin, connPool.getConnection());
			ses.insert(customer, connPool.getConnection());

			util.printDBStatus();

			// create categories for store
			Category healthAndBeauty = new Category("Heath & Beauty");
			Category clothes = new Category("Clothes");
			Category electronics = new Category("Electronics");
			Category homeAndGarden = new Category("Home & Garden");

			// insert categories
			int habID = ses.insert(healthAndBeauty, connPool.getConnection());
			int cID = ses.insert(clothes, connPool.getConnection());
			int eID = ses.insert(electronics, connPool.getConnection());
			int hagID = ses.insert(homeAndGarden, connPool.getConnection());

			util.printDBStatus();

			// check for errors in insert method
			if (habID == -1 || cID == -1 || eID == -1 || hagID == -1)
				log.warn("Failure to insert category");

			// set category id's from DB
			healthAndBeauty.setId(habID);
			clothes.setId(cID);
			electronics.setId(eID);
			homeAndGarden.setId(hagID);

			// create products for store
			Product moisturizer = new Product(healthAndBeauty.getId(), "Daily Moistuizer", "for dry skin", 12.99, 10);
			Product dailyVitamins = new Product(healthAndBeauty.getId(), "Daily Vitamins", "chewable, fruit flavored",
					21.99, 17);
			Product tshirt = new Product(clothes.getId(), "T-Shirt", "color: black, size: medium", 17.99, 14);
			Product sweatShirt = new Product(clothes.getId(), "Sweat Shirt", "color: navy, size: medium", 23.99, 4);
			Product iPad = new Product(electronics.getId(), "iPad", "Apple product", 1300.99, 20);
			Product keyboard = new Product(electronics.getId(), "Razor Hunter Keyboard", "clicky", 270.69, 10);
			Product instaPot = new Product(homeAndGarden.getId(), "InstaPot", "pressure cooker", 419.68, 3);
			Product waterfall = new Product(homeAndGarden.getId(), "Waterfall", "backyard feature", 300.99, 2);

			ses.insert(moisturizer, connPool.getConnection());
			ses.insert(dailyVitamins, connPool.getConnection());
			ses.insert(tshirt, connPool.getConnection());
			ses.insert(sweatShirt, connPool.getConnection());
			ses.insert(iPad, connPool.getConnection());
			ses.insert(keyboard, connPool.getConnection());
			ses.insert(instaPot, connPool.getConnection());
			ses.insert(waterfall, connPool.getConnection());

			util.printDBStatus();

			// tx.commitTrans(null); //figure out later
		} catch (SQLException e) {
			log.warn("Failure: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static DataSource setUpConnection() {
		try {
			return ConnectionUtil.setUpPool();
		} catch (Exception e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	private static void runAdminFunctionality() {
		// admin: update/remove/insert products/categories select
		// users/orders/products/categories
		int request = 0;
		while (request != 8) {
			System.out.println("Would you like to:");
			System.out.println("[1] Insert a product or category");
			System.out.println("[2] Update a product or category");
			System.out.println("[3] Remove a product or category");
			System.out.println("[4] View all users");
			System.out.println("[5] View all orders");
			System.out.println("[6] View all products");
			System.out.println("[7] View all categories");
			System.out.println("[8] Exit Admin View");
			request = scan.nextInt();

			switch (request) {
			case 1:
				insert();
				break;
			case 2:
				
				break;
			case 3:
				remove();
				break;
			case 4:
				viewAllUsers();
				break;
			case 5:
				viewAllOrders();
				break;
			case 6:
				viewAllProducts();
				break;
			case 7:
				viewAllCategories();
				break;
			default:
				break;
			}
		}

	}
	
	private static void remove() {
		boolean flag = false;
		while (!flag) {
			System.out.println("Would you like to remove a product or category?");
			String table = scan.next();
			if (table.equalsIgnoreCase("product")) {
				flag = true;
				removeProduct();

			} else if (table.equalsIgnoreCase("category")) {
				flag = true;
				removeCategory();
			} else {
				System.out.println("Please choose product or category");
			}
		}
	}
	
	private static void removeProduct() {
		System.out.print("ID of the product to remove: ");
		int id = scan.nextInt();
		
		Session ses = new Session();
		Connection conn;
		try {
			conn = connPool.getConnection();
			boolean deleted = ses.delete(Product.class, id, conn);
			
			if(deleted)
				System.out.println("Product #" + id + " successfully deleted");
			else
				log.warn("Failure to remove product #" + id);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}

	}
	
	private static void removeCategory() {
		System.out.print("ID of the category to remove: ");
		int id = scan.nextInt();
		
		Session ses = new Session();
		try {
			boolean deleted = ses.delete(Category.class, id, connPool.getConnection());
			
			if(deleted)
				System.out.println("Category #" + id + " successfully deleted");
			else
				log.warn("Failure to remove category #" + id + ", ensure no dependencies exist and try again");
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}

	}

	private static void insert() {
		boolean flag = false;
		while (!flag) {
			System.out.println("Would you like to insert a product or category?");
			String table = scan.next();
			if (table.equalsIgnoreCase("product")) {
				flag = true;
				insertProduct();

			} else if (table.equalsIgnoreCase("category")) {
				flag = true;
				insertCategory();
			} else {
				System.out.println("Please choose product or category");
			}
		}
	}
	private static void insertProduct() {
		System.out.print("Product name: ");
		String name = scan.next();
		System.out.print("Product description: ");
		String desc = scan.next();
		System.out.print("Price: ");
		double price = scan.nextDouble();
		System.out.println("How many do you have in stock?");
		int quantity = scan.nextInt();
		System.out.println("at is the ID of the category this product belongs in?");
		int catID = scan.nextInt();

		Product product = new Product(catID, name, desc, price, quantity);
		Session ses = new Session();
		
		try {
			ses.insert(product, connPool.getConnection());
		} catch (SQLException e) {
			log.warn("Failure to insert new product " + name);
			e.printStackTrace();
		}

	}
	
	private static void insertCategory() {
		System.out.print("Category name: ");
		String name = scan.next();

		Category cat = new Category(name);
		Session ses = new Session();
		try {
			ses.insert(cat, connPool.getConnection());
		} catch (SQLException e) {
			log.warn("Failure to insert new product " + name);
			e.printStackTrace();
		}

	}
	
	private static void viewAllUsers() {
		System.out.println("Printing all users: ");
		
		User user = null;
		Session ses = new Session();
		
		try {
			List<User> users = new ArrayList<User>();
			users = ses.selectAll(connPool.getConnection(), user);
			for(User u : users) {
				System.out.println(u.toString());
			}
		} catch(SQLException e) {
			log.warn("Failure to retrieve all users from the databse.");
			e.printStackTrace();
		}
	}
	
	private static void viewAllOrders() {
		System.out.println("Printing all Orders: ");
		
		Order order = null;
		Session ses = new Session();
		
		try {
			List<Order> orders = new ArrayList<Order>();
			orders = ses.selectAll(connPool.getConnection(), order);
			for(Order o : orders) {
				System.out.println(o.toString());
			}
		} catch(SQLException e) {
			log.warn("Failure to retrieve all orders from the databse.");
			e.printStackTrace();
		}
	}
	
	private static void viewAllProducts() {
		System.out.println("Printing all Products: ");
		
		Product product = null;
		Session ses = new Session();
		
		try {
			List<Product> products = new ArrayList<Product>();
			products = ses.selectAll(connPool.getConnection(), product);
			for(Product p : products) {
				System.out.println(p.toString());
			}
		} catch(SQLException e) {
			log.warn("Failure to retrieve all products from the databse.");
			e.printStackTrace();
		}
	}
	
	private static void viewAllCategories() {
		System.out.println("Printing all Categories: ");
		
		Category category = null;
		Session ses = new Session();
		try {
			List<Category> categories = new ArrayList<Category>();
			categories = ses.selectAll(connPool.getConnection(), category);
			for(Category c : categories) {
				System.out.println(c.toString());
			}
		} catch(SQLException e) {
			log.warn("Failure to retrieve all categories from the databse.");
			e.printStackTrace();
		}
	}

	private static void runCustomerFunctionality() {
		// customer: insert order select products/their user info/their
		// orders/categories
		
		int request = 0;
		while (request != 6) {
			System.out.println("Would you like to:");
			System.out.println("[1] View all products");
			System.out.println("[2] View products by category"); //display categories in here
			System.out.println("[3] Order a product");
			System.out.println("[4] View all your orders");
			System.out.println("[5] View your account information");
			System.out.println("[6] Exit Customer View");
			request = scan.nextInt();

			switch (request) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			default:
				break;
			}
		}

	}

}
