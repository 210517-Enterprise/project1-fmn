package com.revature;

import java.sql.SQLException;
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
		boolean choseFunctionality = false;
		while (!choseFunctionality) {
			System.out.println("Would you like to explore functionality for an Admin or Customer?");
			String userLogin = scan.next();

			if (userLogin.equalsIgnoreCase("admin")) {
				choseFunctionality = true;
				runAdminFunctionality();
			}

			if (userLogin.equalsIgnoreCase("customer")) {
				choseFunctionality = true;
				runCustomerFunctionality();
			}

		}

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
				System.out.println("Tables for Congo successfully created...");
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
		System.out.println("Would you like to:");
		System.out.println("[1] Insert a product or category");
		System.out.println("[2] Update a product or category");
		System.out.println("[2] Remove a product or category");
		System.out.println("[4] View all users");
		System.out.println("[5] View all orders");
		System.out.println("[6] View all products");
		System.out.println("[7] View all categories");
		int request = scan.nextInt();

		switch (request) {
		case 1:
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
			break;
		case 2:
			flag = false;
			while(!flag) {
				System.out.println("Would you like to update a product or category");
				String table = scan.next();
				if (table.equalsIgnoreCase("product")) {
					flag = true;
					updateProduct();
				} else if (table.equalsIgnoreCase("category")) {
					flag = true;
					updateCategory();
				} else {
					System.out.println("Please choose product or category");
				}
			}
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			break;
		}

	}
	
	public static void updateProduct() {
		
		System.out.print("Update product name: ");
		String pName = scan.next();
		System.out.println("\nUpdate product description: ");
		String pDesc = scan.next();
		System.out.println("\nUpdate product price: ");
		double pPrice = scan.nextDouble();
		System.out.println("Update the quantity in stock: ");
		int pQuantity = scan.nextInt();
		System.out.println("What is the product ID: ");
		int pId = scan.nextInt();
		
	//	Product product = new Product();
		
	}
	
	public static void updateCategory() {
		System.out.println("Update the category name: ");
		String cName = scan.next();
		System.out.println("What is the category Id: ");
		int cId = scan.nextInt();
		
		// should we enter the the id and retrieve the record by PK?? or just create a new object
		Category category = new Category(cId, cName);
		Session ses = new Session();
		
		try {
			ses.update(category, connPool.getConnection());
		} catch (SQLException e) {
			log.warn("Failed to update category");
			e.printStackTrace();
		}
		
	}
	
	private static void insertProduct() {
		System.out.print("Product name: ");
		String name = scan.next();
		System.out.print("\nProduct description: ");
		String desc = scan.next();
		System.out.print("\nPrice: ");
		double price = scan.nextDouble();
		System.out.println("\nHow many do you have in stock?");
		int quantity = scan.nextInt();
		System.out.println("\nWhat is the ID of the category this product belongs in?");
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

	private static void runCustomerFunctionality() {
		// customer: insert order select products/their user info/their
		// orders/categories

	}

}
