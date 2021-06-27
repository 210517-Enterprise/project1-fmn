package com.revature.repos;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.exceptions.UnannotatedClassException;
import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.util.Configuration;
import com.revature.util.Metamodel;

/**
 * This class does CRUD work on the database including transaction management
 * and caching
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/23/21
 *
 */
public class Session {

	private static Logger log = Logger.getLogger(Session.class);

	/**
	 * Static cache belonging the Session class
	 */
	private static ObjectCache cacheObject = ObjectCache.getInstance();

	/**
	 * Getter method for an instance of the Transaction class
	 * 
	 * @return an instance of the Transaction class
	 */
	public Transaction getTransaction() {
		return new Transaction();
	}

	/**
	 * This method creates tables in the database according to the information
	 * provided by a configuration object
	 * 
	 * @param cfg  an instance of the Configuration class
	 * @param conn the connection to the database
	 * @return true if tables are successfully created, false if they are not
	 *         created correctly
	 */
	public boolean createTables(Configuration cfg, Connection conn) {
		try {
			return new DatabaseBuilder(cfg).createTables(conn);
		} catch (NoSuchFieldException | SQLException e) {
			log.warn("Failure in Session.createTables: " + e.getMessage());
			return false;
		}
	}

	/**
	 * This method drops tables in the database according to the information
	 * provided by a configuration object
	 * 
	 * @param cfg  an instance of the Configuration class
	 * @param conn the connection to the database
	 * @return true if tables are successfully dropped, false if they are not
	 *         dropped
	 */
	public boolean dropTables(Configuration cfg, Connection conn) {
		try {
			return new DatabaseBuilder(cfg).dropTables(conn);
		} catch (SQLException e) {
			log.warn("Failure in Session.dropTables: " + e.getMessage());
			return false;
		}
	}

	// Overload update methods
	/**
	 * This method updates a User object in database
	 * 
	 * @param user the object to be updated in the database
	 * @param conn the connection to the database
	 * @return true if the user's information is successfully updated, false if the
	 *         user does not exist in the database or cannot be updated
	 */
	public boolean update(User user, Connection conn) {
		try {
			cacheObject.put(user);
			return DatabaseUpdater.updateUser(Metamodel.of(User.class), user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("Failure in Session.update  (User): " + e.getMessage());
			return false;
		}
	}

	/**
	 * This method updates a Category object in database
	 * 
	 * @param category the object to be updated in the database
	 * @param conn     the connection to the database
	 * @return true if the category's information is successfully updated, false if
	 *         the category does not exist in the database or cannot be updated
	 */
	public boolean update(Category category, Connection conn) {
		cacheObject.put(category);
		return DatabaseUpdater.updateCategory(Metamodel.of(Category.class), category, conn);
	}

	/**
	 * This method updates a Product object in database
	 * 
	 * @param product the object to be updated in the database
	 * @param conn    the connection to the database
	 * @return true if the product's information is successfully updated, false if
	 *         the product does not exist in the database or cannot be updated
	 */
	public boolean update(Product product, Connection conn) {
		try {
			cacheObject.put(product);
			return DatabaseUpdater.updateProduct(Metamodel.of(Product.class), product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("Failure in Session.update  (Product): " + e.getMessage());
			return false;
		}
	}

	/**
	 * This method updates a Order object in database
	 * 
	 * @param order the object to be updated in the database
	 * @param conn  the connection to the database
	 * @return true if the order's information is successfully updated, false if the
	 *         user does not exist in the database or cannot be updated
	 */
	public boolean update(Order order, Connection conn) {
		try {
			cacheObject.put(order);
			return DatabaseUpdater.updateOrder(Metamodel.of(Order.class), order, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("Failure in Session.update  (Order): " + e.getMessage());
			return false;
		}
	}

	/**
	 * This method deletes an object from the database
	 * 
	 * @param clazz the class of the object being deleted
	 * @param id    the id of the object being deleted (primary key in the database)
	 * @param conn  the connection to the database
	 * @return
	 */
	public boolean delete(Class<?> clazz, int id, Connection conn) {
		if (clazz.equals(Category.class))
			return DatabaseDeleter.deleteCategoryById(Metamodel.of(Category.class), id, conn);
		else if (clazz.equals(Order.class))
			return DatabaseDeleter.deleteOrderById(Metamodel.of(Order.class), id, conn);
		else if (clazz.equals(Product.class))
			return DatabaseDeleter.deleteProductById(Metamodel.of(Product.class), id, conn);
		else if (clazz.equals(User.class))
			return DatabaseDeleter.deleteUserById(Metamodel.of(User.class), id, conn);
		else {
			throw new UnannotatedClassException("Failure to delete object from class that contains no annotations");
		}
	}

	// overload insert methods
	/**
	 * This method inserts a User object from the database
	 * 
	 * @param user the object to insert into the database
	 * @param conn the connection to the database
	 */
	public void insert(User user, Connection conn) {
		try {
			cacheObject.put(user);
			InsertDB.insertUser(Metamodel.of(User.class), user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("Failure in Session.insert  (User): " + e.getMessage());
		}
	}

	/**
	 * This method inserts a Product object from the database
	 * 
	 * @param product the object to insert into the database
	 * @param conn    the connection to the database
	 */
	public void insert(Product product, Connection conn) {
		try {
			cacheObject.put(product);
			InsertDB.insertProduct(Metamodel.of(Product.class), product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("Failure in Session.insert  (Product): " + e.getMessage());
		}
	}

	/**
	 * This method inserts a Order object from the database
	 * 
	 * @param order the object to insert into the database
	 * @param conn  the connection to the database
	 */
	public void insert(Order order, Connection conn) {
		try {
			cacheObject.put(order);
			InsertDB.insertOrder(Metamodel.of(Order.class), order, conn);
			System.out.println("Order Sucessfully Placed for product number: #" + order.getProductID() + "\n");
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("Failure in Session.insert  (Order): " + e.getMessage());
		}
	}

	/**
	 * This method inserts a Category object from the database
	 * 
	 * @param category the object to insert into the database
	 * @param conn     the connection to the database
	 * @return the new id of the category created by the database, or -1 if there is
	 *         an issue inserting the object
	 */
	public int insert(Category category, Connection conn) {
		try {
			cacheObject.put(category);
			int id = InsertDB.insertCategory(Metamodel.of(Category.class), category, conn);
			return id;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn("Failure in Session.insert  (Category): " + e.getMessage());
			return -1;
		}
	}

	// overload select * methods
	/**
	 * This method selects all users from the database
	 * 
	 * @param u    the kind of object to select from the database
	 * @param conn the connection to the database
	 * @return list of users in database
	 */
	public List<User> selectAll(Connection conn, User u) {
		try {
			return GetDB.getAllUsers(conn, Metamodel.of(User.class));

		} catch (SQLException e) {
			log.warn("Failure in Session.selectAll  (User): " + e.getMessage());
			return null;
		}
	}

	/**
	 * This method selects all orders from the database
	 * 
	 * @param o    the kind of object to select from the database
	 * @param conn the connection to the database
	 * @return list of orders in database
	 */
	public List<Order> selectAll(Connection conn, Order o) {
		try {
			return GetDB.getAllOrders(conn, Metamodel.of(Order.class));
		} catch (SQLException e) {
			log.warn("Failure in Session.selectAll  (Order): " + e.getMessage());
			return null;
		}
	}

	/**
	 * This method selects all categories from the database
	 * 
	 * @param c    the kind of object to select from the database
	 * @param conn the connection to the database
	 * @return list of categories in database
	 */
	public List<Category> selectAll(Connection conn, Category c) {
		try {
			return GetDB.getAllCategories(conn, Metamodel.of(Category.class));
		} catch (SQLException e) {
			log.warn("Failure in Session.selectAll  (Category): " + e.getMessage());
			return null;
		}
	}

	/**
	 * This method selects all products from the database
	 * 
	 * @param p    the kind of object to select from the database
	 * @param conn the connection to the database
	 * @return list of products in database
	 */
	public List<Product> selectAll(Connection conn, Product p) {
		try {
			return GetDB.getAllProducts(conn, Metamodel.of(Product.class));
		} catch (SQLException e) {
			log.warn("Failure in Session.selectAll  (Product): " + e.getMessage());
			return null;
		}
	}

	/**
	 * This method selects an object from the database by its ID
	 * 
	 * @param conn  the connection to the database
	 * @param id    the id of the object to select
	 * @param clazz the class of the object to select
	 * @return the object if it exists in the database, null if it does not
	 */
	public Object selectAllById(Connection conn, int id, Class<?> clazz) {
		try {
			if (clazz.equals(User.class)) {
				if (cacheObject.contains(clazz, id))
					return cacheObject.get(clazz, cacheObject.indexOf(clazz, id));

				return GetDB.getByUserID(conn, Metamodel.of(User.class), id);
			} else if (clazz.equals(Order.class)) {
				if (cacheObject.contains(clazz, id))
					return cacheObject.get(clazz, cacheObject.indexOf(clazz, id));

				return GetDB.getUserOrdersByPrimaryKey(conn, Metamodel.of(Order.class), id);
			} else if (clazz.equals(Category.class)) {
				if (cacheObject.contains(clazz, id))
					return cacheObject.get(clazz, cacheObject.indexOf(clazz, id));

				return GetDB.getAllCategoriesByPrimaryKey(conn, Metamodel.of(Category.class), id);
			} else if (clazz.equals(Product.class)) {
				if (cacheObject.contains(clazz, id))
					return cacheObject.get(clazz, cacheObject.indexOf(clazz, id));

				return GetDB.getAllProductsByPrimaryKey(conn, Metamodel.of(Product.class), id);
			} else
				return null;
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	// override select * by foreign key methods
	/**
	 * This method selects all order objects from the database that belong to a
	 * specific user, which is referenced by the foreign key, user_id
	 * 
	 * @param conn           the connection to the database
	 * @param userID         the id of the user the order objects belong to
	 * @param tableClass     the kind of object to select from the database
	 * @param referenceClass the kind of object referenced with the foreign key
	 * @return list of orders belonging to the user, null if the user has no orders
	 */
	public List<Order> selectAllByForeignKey(Connection conn, int userID, Order tableClass, User referenceClass) {
		try {
			return GetDB.getUserOrdersByUserID(conn, Metamodel.of(Order.class), userID);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method selects all order objects from the database that contain a
	 * specific product, which is referenced by the foreign key, product_id
	 * 
	 * @param conn           the connection to the database
	 * @param userID         the id of the product the order objects contain
	 * @param tableClass     the kind of object to select from the database
	 * @param referenceClass the kind of object referenced with the foreign key
	 * @return list of orders containing the product, null if no orders contain the
	 *         product
	 */
	public List<Order> selectAllByForeignKey(Connection conn, int productID, Order tableClass, Product referenceClass) {
		try {
			return GetDB.getUserOrdersByProductID(conn, Metamodel.of(Order.class), productID);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This method selects all product objects from the database that contain a
	 * specific category, which is referenced by the foreign key, category_id
	 * 
	 * @param conn           the connection to the database
	 * @param userID         the id of the category the product objects contain
	 * @param tableClass     the kind of object to select from the database
	 * @param referenceClass the kind of object referenced with the foreign key
	 * @return list of products containing the category, null if no products contain
	 *         the category
	 */
	public List<Product> selectAllByForeignKey(Connection conn, int categoryID, Product tableClass,
			Category referenceClass) {
		try {
			return GetDB.getAllProductsByForeignKey(conn, Metamodel.of(Product.class), categoryID);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
