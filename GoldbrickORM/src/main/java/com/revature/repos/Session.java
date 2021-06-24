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
 * This class ................
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/23/21
 *
 */
public class Session {

	private static Logger log = Logger.getLogger(Session.class);
	private static ObjectCache cacheObject = ObjectCache.getInstance();

	public Transaction getTransaction() {
		return new Transaction();
	}

	public boolean createTables(Configuration cfg, Connection conn) {
		try {
			return new DatabaseBuilder(cfg).createTables(conn);
		} catch (NoSuchFieldException | SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean dropTables(Configuration cfg, Connection conn) {
		try {
			return new DatabaseBuilder(cfg).dropTables(conn);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Overload update methods
	public boolean update(User user, Connection conn) {
		try {
			cacheObject.put(user);
			return DatabaseUpdater.updateUser(Metamodel.of(User.class), user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Category category, Connection conn) {
		cacheObject.put(category);
		return DatabaseUpdater.updateCategory(Metamodel.of(Category.class), category, conn);
	}

	public boolean update(Product product, Connection conn) {
		try {
			cacheObject.put(product);
			return DatabaseUpdater.updateProduct(Metamodel.of(Product.class), product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Order order, Connection conn) {
		try {
			cacheObject.put(order);
			return DatabaseUpdater.updateOrder(Metamodel.of(Order.class), order, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// overload delete methods
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
	public void insert(User user, Connection conn) {
		try {
			cacheObject.put(user);
			InsertDB.insertUser(Metamodel.of(User.class), user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Product product, Connection conn) {
		try {
			cacheObject.put(product);
			InsertDB.insertProduct(Metamodel.of(Product.class), product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Order order, Connection conn) {
		try {
			cacheObject.put(order);
			InsertDB.insertOrder(Metamodel.of(Order.class), order, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public int insert(Category category, Connection conn) {
		try {
			cacheObject.put(category);
			int id = InsertDB.insertCategory(Metamodel.of(Category.class), category, conn);
			return id;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return -1;
		}
	}

	// overload select * methods
	public List<User> selectAll(Connection conn, User u) {
		try {
			return GetDB.getAllUsers(conn, Metamodel.of(User.class));

		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Order> selectAll(Connection conn, Order o) {
		try {
			return GetDB.getAllOrders(conn, Metamodel.of(Order.class));
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Category> selectAll(Connection conn, Category c) {
		try {
			return GetDB.getAllCategories(conn, Metamodel.of(Category.class));
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Product> selectAll(Connection conn, Product p) {
		try {
			return GetDB.getAllProducts(conn, Metamodel.of(Product.class));
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

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
	public List<Order> selectAllByForeignKey(Connection conn, int userID, Order tableClass, User referenceClass) {
		try {
			return GetDB.getUserOrdersByUserID(conn, Metamodel.of(Order.class), userID);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Order> selectAllByForeignKey(Connection conn, int productID, Order tableClass, Product referenceClass) {
		try {
			return GetDB.getUserOrdersByProductID(conn, Metamodel.of(Order.class), productID);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

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
