package com.revature;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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

	public static Configuration cfg = new Configuration();
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

		dropTables();
		System.out.println("Thanks for visiting Daintree");

	}

	private static boolean continueOrQuit() {
		System.out.println("[1] Quit");
		System.out.println("[2] Explore different funnctionality");
		int request = scan.nextInt();

		if (request == 1)
			return false;
		else
			return true;
	}

	private static void dropTables() {
		Session ses = new Session();
		try {
			ses.dropTables(cfg, connPool.getConnection());
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void initializeStore() {
		Session ses = new Session();
		Transaction tx = ses.getTransaction();

		ConnectionUtil util = new ConnectionUtil();
		util.printDBStatus();

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
				update();
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

	public static void update() {
		boolean flag = false;
		while (!flag) {
			System.out.println("Would you like to update a product or category?");
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
	}

	public static void updateProduct() {

		System.out.println("What is the product ID: ");
		int pId = scan.nextInt();
		Connection conn;

		Session ses = new Session();
		try {

			conn = connPool.getConnection();

			Product p = (Product) ses.selectAllById(conn, pId, Product.class);
<<<<<<< HEAD
			System.out.println("==================================================== Product =====================================================");
			System.out.println("=Product ID=  [1]=CategoryID=  [2]=Product Name=   [3]=Product Description=  [4]=Product Price=  [5]=Product Quantity=");
			System.out.println(p.getId() + "              " + p.getCategoryID() + "               " + p.getProductName()+ "         " + p.getProductDescription() + "            "+  p.getPrice() + "             " + p.getQuantity());
=======
			System.out.println(
					"==================================================== Product =====================================================");
			System.out.println(
					"=Product ID=  [1]=CategoryID=  [2]=Product Name=   [3]=Product Description=  [4]=Product Price=  [5]=Product Quantity=");
			System.out.println(p.getId() + p.getCategoryID() + p.getProductName() + p.getProductDescription()
					+ p.getPrice() + p.getQuantity());
>>>>>>> 45fc4f5a3274fbf1935350a959edc197bdef0033

			System.out.println("What would you like to update?");
			int adminRequest = scan.nextInt();
			switch (adminRequest) {
			case 1:
				System.out.print("Update category id: ");
				int cId = scan.nextInt();
				p.setCategoryID(cId);
				try {
					conn = connPool.getConnection();
					ses.update(p, conn);
					System.out.println("Updated category id to: " + p.getCategoryID());
					//log.info("Successfully updated category id");
					break;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			case 2:
				System.out.print("Update product name: ");
				String pName = scan.next();
				pName += scan.nextLine();
				p.setProductName(pName);
				try {
					conn = connPool.getConnection();
					ses.update(p, conn);
					System.out.println("Updated product name to: " + p.getProductName());
					//log.info("Successfully updated product name");
					break;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			case 3:
				System.out.print("Update product description: ");
				
				String pDesc = scan.next();
				pDesc = scan.nextLine();
				try {
					conn = connPool.getConnection();
					p.setProductDescription(pDesc);
					ses.update(p, conn);
					System.out.println("Updated product description to: " + p.getProductDescription());
					break;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			case 4:
				System.out.print("Update product price: ");
				double pPrice = scan.nextDouble();
				try {
					conn = connPool.getConnection();
					p.setPrice(pPrice);
					ses.update(p, conn);
					System.out.println("Updated product price to: $" + p.getPrice());
					break;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			case 5:
				System.out.print("Update product quantity: ");
				int pQuan = scan.nextInt();
				try {
					conn = connPool.getConnection();
					if (pQuan < 0) {
						System.out.println("Entered a negative quanity please try again!");
						int request2 = scan.nextInt();
						p.setQuantity(request2);
						ses.update(p, conn);
						System.out.println("Updated product quanity to: " + p.getQuantity());
						//log.info("Successfully updated product quantity");
					} else {
						p.setQuantity(pQuan);
						ses.update(p, conn);
						System.out.println("Updated product quantity to: " + p.getQuantity());
						//log.info("Successfully updated product 	quantity");
						break;
					}
				
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
			log.warn("failed to update product!");

		}

	}

	public static void updateCategory() {
		System.out.println("Update the category name: ");
		String cName = scan.next();
		System.out.println("What is the category Id: ");
		int cId = scan.nextInt();

		// should we enter the the id and retrieve the record by PK?? or just create a
		// new object and replace
		Category category = new Category(cId, cName);
		Session ses = new Session();

		try {
			ses.update(category, connPool.getConnection());
		} catch (SQLException e) {
			log.warn("Failed to update category");
			e.printStackTrace();
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

			if (deleted)
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

			if (deleted)
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
			System.out.println("========================================Users========================================");
			// System.out.println("ID First Name Last Name Email Password Role");
			for (User u : users) {
				System.out.println(u.getId() + "        " + u.getFirstName() + "        " + u.getLastName() + "        "
						+ u.getEmail() + "        " + u.getPassword() + "        " + u.getRole().toString() + "\n\n");
			}
		} catch (SQLException e) {
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
			for (Order o : orders) {
				System.out.println(o.toString());
			}
		} catch (SQLException e) {
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
			for (Product p : products) {
				System.out.println(p.toString());
			}
		} catch (SQLException e) {
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
			for (Category c : categories) {
				System.out.println(c.toString());
			}
		} catch (SQLException e) {
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
			System.out.println("[2] View products by category"); // display categories in here
			System.out.println("[3] Order a product");
			System.out.println("[4] View all your orders");
			System.out.println("[5] View your account information");
			System.out.println("[6] Exit Customer View");
			request = scan.nextInt();

			switch (request) {
			case 1:
				viewAllProducts();
				break;
			case 2:
				viewProductsByCategory();
				break;
			case 3:
				order();
				break;
			case 4:
				viewAllCustomerOrders();
				break;
			case 5:
				viewCustomerInfo();
				break;
			default:
				break;
			}
		}

	}

	private static void viewProductsByCategory() {
		viewAllCategories();
		System.out.print("Select a Category ID: ");
		int id = scan.nextInt();

		Session ses = new Session();
		try {
			List<Product> products = ses.selectAllByForeignKey(connPool.getConnection(), id, new Product(),
					new Category());

			for (Product p : products)
				System.out.println(p.toString());

		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void order() {
		System.out.println("ID of product: ");
		int id = scan.nextInt();
		
		
		Session ses = new Session();
		Product toOrder;
		try {
			toOrder = (Product) ses.selectAllById(connPool.getConnection(), id, Product.class);
			Order order = new Order(customer.getId(), id, Date.valueOf(LocalDate.now()), toOrder.getPrice(), false, id);
			ses.insert(order, connPool.getConnection());
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void viewAllCustomerOrders() {
		Session ses = new Session();
		try {
			List<Order> orders = ses.selectAllByForeignKey(connPool.getConnection(), customer.getId(), new Order(), customer);
			
			for(Order o: orders) {
				System.out.println(o.toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void viewCustomerInfo() {
		Session ses = new Session();
		try {
			User user = (User) ses.selectAllById(connPool.getConnection(), customer.getId(), User.class);
			System.out.println(user.toString());
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

}
