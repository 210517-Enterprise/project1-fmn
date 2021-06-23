package com.revature.repos;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.util.Configuration;
import com.revature.util.Metamodel;

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
	public boolean update(Metamodel<User> mm, User user, Connection conn) {
		try {
			return DatabaseUpdater.updateUser(mm, user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Metamodel<Category> mm, Category category, Connection conn) {
		return DatabaseUpdater.updateCategory(mm, category, conn);
	}

	public boolean update(Metamodel<Product> mm, Product product, Connection conn) {
		try {
			return DatabaseUpdater.updateProduct(mm, product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Metamodel<Order> mm, Order order, Connection conn) {
		try {
			return DatabaseUpdater.updateOrder(mm, order, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	// overload delete methods
	public boolean delete(Metamodel<Category> mm, Category category, Connection conn) {
		return DatabaseDeleter.deleteCategoryById(mm, category.getId(), conn);
	}

	public boolean delete(Metamodel<Order> mm, Order order, Connection conn) {
		return DatabaseDeleter.deleteOrderById(mm, order.getId(), conn);
	}

	public boolean delete(Metamodel<Product> mm, Product product, Connection conn) {
		return DatabaseDeleter.deleteProductById(mm, product.getId(), conn);
	}

	public boolean delete(Metamodel<User> mm, User user, Connection conn) {
		return DatabaseDeleter.deleteUserById(mm, user.getId(), conn);
	}

	// overload insert methods
	public void insert(Metamodel<User> mm, User user, Connection conn) {
		try {
			InsertDB.insertUser(mm, user, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Metamodel<Product> mm, Product product, Connection conn) {
		try {
			InsertDB.insertProduct(mm, product, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Metamodel<Order> mm, Order order, Connection conn) {
		try {
			InsertDB.insertOrder(mm, order, conn);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
		}
	}

	public void insert(Metamodel<Category> mm, Category category, Connection conn) {
		try {
			InsertDB.insertCategory(mm, category, conn);
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
			return GetDB.getAllProducts(conn, Metamodel.of(Category.class));
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
	
	public List<Order> selectAllByForeignKey(Connection conn, int userID, Order tableClass, Product referenceClass) {
		try {
			return GetDB.getUserOrdersByProductID(conn, Metamodel.of(Order.class), userID);
		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

}
