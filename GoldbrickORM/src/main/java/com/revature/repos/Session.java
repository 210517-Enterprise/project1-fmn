package com.revature.repos;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.util.Configuration;
import com.revature.util.Metamodel;

/**
 * This class ................
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/23/21
 *
 */
public class Session {

	private static Logger log = Logger.getLogger(Session.class);

	public boolean createTables(Configuration cfg, Connection conn) {
		try {
			return new DatabaseBuilder(cfg).createTables(conn);
		} catch (NoSuchFieldException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// Overload update methods
	public boolean update(User user, Connection conn) {
		try {
			return DatabaseUpdater.updateUser(Metamodel.of(User.class), user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Category category, Connection conn) {
		return DatabaseUpdater.updateCategory(Metamodel.of(Category.class), category, conn);
	}

	public boolean update(Product product, Connection conn) {
		try {
			return DatabaseUpdater.updateProduct(Metamodel.of(Product.class), product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Order order, Connection conn) {
		try {
			return DatabaseUpdater.updateOrder(Metamodel.of(Order.class), order, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// overload delete methods
	public boolean delete(Category category, Connection conn) {
		return DatabaseDeleter.deleteCategoryById(Metamodel.of(Category.class), category.getId(), conn);
	}

	public boolean delete(Order order, Connection conn) {
		return DatabaseDeleter.deleteOrderById(Metamodel.of(Order.class), order.getId(), conn);
	}

	public boolean delete(Product product, Connection conn) {
		return DatabaseDeleter.deleteProductById(Metamodel.of(Product.class), product.getId(), conn);
	}

	public boolean delete(User user, Connection conn) {
		return DatabaseDeleter.deleteUserById(Metamodel.of(User.class), user.getId(), conn);
	}

	// overload insert methods
	public void insert(User user, Connection conn) {
		try {
			InsertDB.insertUser(Metamodel.of(User.class), user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Product product, Connection conn) {
		try {
			InsertDB.insertProduct(Metamodel.of(Product.class), product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Order order, Connection conn) {
		try {
			InsertDB.insertOrder(Metamodel.of(Order.class), order, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Category category, Connection conn) {
		try {
			InsertDB.insertCategory(Metamodel.of(Category.class), category, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	// overload select * methods
	public List<User> selectAll(Connection conn, User user) {
		try {
			return GetDB.getAllUsers(conn, Metamodel.of(User.class));

		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Order> selectAll(Connection conn, Order order) {
		try {
			return GetDB.getAllOrders(conn, Metamodel.of(Order.class));
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Category> selectAll(Connection conn, Category category) {
		try {
			return GetDB.getAllCategories(conn, Metamodel.of(Category.class));
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public List<Product> selectAll(Connection conn, Product product) {
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
			if (clazz.equals(User.class))
				return GetDB.getByUserID(conn, Metamodel.of(User.class), id);
			else if (clazz.equals(Order.class))
				return GetDB.getUserOrdersByPrimaryKey(conn, Metamodel.of(Order.class), id);
			else if (clazz.equals(Category.class))
				return GetDB.getAllCategoriesByPrimaryKey(conn, Metamodel.of(Category.class), id);
			else if (clazz.equals(Product.class))
				return GetDB.getAllProductsByPrimaryKey(conn, Metamodel.of(Product.class), id);
			else
				return null;
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	//override select * by foreign key methods
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
	
	public List<Product> selectAllByForeignKey(Connection conn, int categoryID, Product tableClass, Category referenceClass) {
		try {
			return GetDB.getAllProductsByForeignKey(conn, Metamodel.of(Product.class), categoryID);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
