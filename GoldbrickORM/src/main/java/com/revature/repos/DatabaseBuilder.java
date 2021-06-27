package com.revature.repos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.revature.annotations.Entity;
import com.revature.models.Category;
import com.revature.models.Constraint;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;
import com.revature.util.ColumnField;
import com.revature.util.Configuration;
import com.revature.util.ConnectionUtil;
import com.revature.util.ForeignKeyField;
import com.revature.util.IdField;
import com.revature.util.Metamodel;

/**
 * This class creates the tables in the database based on the meta-models in
 * configuration
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/18/21
 *
 */
public class DatabaseBuilder {

	/**
	 * The configuration object that provides the meta-models to create tables with
	 */
	private Configuration config;

	private static Logger log = Logger.getLogger(DatabaseBuilder.class);

	/**
	 * Constructor for an instance of a Database Builder
	 * 
	 * @param cfg The Configuration object
	 */
	public DatabaseBuilder(Configuration cfg) {
		this.config = cfg;
	}

	/**
	 * This method creates tables from the information provided in each meta-model
	 * in the configuration object
	 * 
	 * @param conn The connection from the connection pool
	 * @return true if a table has been successfully create for each meta-model,
	 *         false if not
	 * @throws NoSuchFieldException
	 * @throws SQLException
	 */
	public boolean createTables(Connection conn) throws NoSuchFieldException, SQLException {
		// check if tables exist in db
		for (Metamodel<Class<?>> mm : this.config.getMetamodels()) {

			IdField pk = mm.getPrimaryKey();
			if (pk == null)
				throw new NoSuchFieldException("Class " + mm.getClassName() + " has no primary key");

			StringBuilder str = new StringBuilder("CREATE TABLE " + mm.getTableName() + "(");
			// append PK+constraints
			str.append(getPrimaryKey(mm) + ", ");

			// append any FKs
			for (String fk : getForeignKeys(mm)) {
				str.append(fk + ", ");
			}
			// appends all other columns
			for (String col : getAttributes(mm)) {
				str.append(col + ", ");
			}
			str.deleteCharAt(str.length() - 1); // get rid of extra " "
			str.deleteCharAt(str.length() - 1); // get rid of extra ","
			str.append(");");

			// System.out.println(str);

			try {
				PreparedStatement ps = conn.prepareStatement(str.toString());

				ps.execute();

			} catch (SQLException e) {
				log.warn("Failure in DatabaseBuilder.createTables: " + e.getMessage());
				e.printStackTrace();
				return false;
			}

		}

		conn.close();
		return true;
	}

	public boolean dropTables(Connection conn) throws SQLException {
		String sql = "";
		sql += "DROP TABLE " + Metamodel.of(Order.class).getTableName() + ";\n";
		sql += "DROP TABLE " + Metamodel.of(Product.class).getTableName() + ";\n";
		sql += "DROP TABLE " + Metamodel.of(Category.class).getTableName() + ";\n";
		sql += "DROP TABLE " + Metamodel.of(User.class).getTableName() + ";\n";

		boolean isDropped = false;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);

			isDropped = !ps.execute(); // false == return row change not object

		} catch (SQLException e) {
			log.warn(e.getMessage());
			e.printStackTrace();
			return false;
		}

		conn.close();
		return isDropped;
	}

	/**
	 * This method is used within the createTable method to grab the Primary Key and
	 * its constraints of an object for table creation
	 * 
	 * @param model The meta-model for the object
	 * @return a string containing the primary key's column name and constraints
	 */
	private String getPrimaryKey(Metamodel<Class<?>> model) {
		String nameAndConstraints = model.getPrimaryKey().getColumnName();
		nameAndConstraints += " " + model.getPrimaryKey().getSQLDataType();

		for (Constraint cons : model.getPrimaryKey().getConstraints()) {
			nameAndConstraints += " " + Constraint.stringRepresentation(cons);
		}

		return nameAndConstraints;

	}

	/**
	 * This method is used within the createTable method to grab the Foreign Keys
	 * and their constraints of an object for table creation
	 * 
	 * @param model The meta-model for the object
	 * @return a string array containing the foreign keys' column names and
	 *         constraints
	 */
	private String[] getForeignKeys(Metamodel<Class<?>> model) {

		List<ForeignKeyField> fks = model.getForeignKeys();
		String[] namesAndConstraints = new String[fks.size()];

		for (int i = 0; i < fks.size(); i++) {
			namesAndConstraints[i] = fks.get(i).getColumnName();
			namesAndConstraints[i] += " " + fks.get(i).getSQLDataType();
			for (Constraint cons : fks.get(i).getConstraints()) {
				namesAndConstraints[i] += " " + Constraint.stringRepresentation(cons);
			}
			namesAndConstraints[i] += " " + fks.get(i).getReference();
		}

		return namesAndConstraints;
	}

	/**
	 * This method is used within the createTable method to grab the attributes and
	 * constraints of an object for table creation
	 * 
	 * @param model The meta-model for the object
	 * @return a string array containing the attributes' column names and
	 *         constraints
	 */
	private String[] getAttributes(Metamodel<Class<?>> model) {

		List<ColumnField> atts = model.getAttributes();
		String[] columns = new String[atts.size()];

		for (int i = 0; i < atts.size(); i++) {
			columns[i] = atts.get(i).getColumnName();
			columns[i] += " " + atts.get(i).getSQLDataType();
			for (Constraint cons : atts.get(i).getConstraints()) {
				columns[i] += " " + Constraint.stringRepresentation(cons);
			}
		}

		return columns;
	}

//	public static void main(String[] args) {
//
//		ConnectionUtil jdbcObj = new ConnectionUtil();
//		ResultSet rs = null;
//		PreparedStatement ps = null;
//		Connection connObj = null;
//
//		try {
//			DataSource datasource = jdbcObj.setUpPool();
//			jdbcObj.printDBStatus();
//
//			System.out.println("\n");
//			connObj = datasource.getConnection();
//
////			Configuration cfg = new Configuration();
////			cfg.addAnnotatedClass(User.class)
////			.addAnnotatedClass(Category.class)
////			.addAnnotatedClass(Product.class)
////			.addAnnotatedClass(Order.class);
////			
////			
////			DatabaseBuilder dbb = new DatabaseBuilder(cfg);
////			dbb.createTables(connObj); 
////			
//			GetDB db = new GetDB();
//
//			Metamodel<User> mm = new Metamodel<>(User.class);
//			Metamodel<Order> om = new Metamodel<>(Order.class);
//			Metamodel<Product> pm = new Metamodel<>(Product.class);
//
//			System.out.println("\n==================Get All Users=======================\n");
//			db.getAllUsers(connObj, mm);
//
//			// =============================================================================
//			User u = db.getByUserID(connObj, mm, 1);
//			System.out.println(u.toString());
//			// =============================================================================
//			System.out.println("\n==================Order by Primary Key=======================\n");
//
//			ArrayList<Order> o = new ArrayList<Order>();
//			o = db.getUserOrdersByPrimaryKey(connObj, om, 2);
//			if (o.size() == 0) {
//				System.out.println("No orders");
//			}
//			for (Order order : o) {
//				System.out.println(order.toString());
//			}
//
//			// ==============================================================================
//			System.out.println("\n=================Get All Orders====================\n");
//			List<Order> orders = new ArrayList<Order>();
//			orders = db.getAllOrders(connObj, om);
//			for (Order oo : orders) {
//				System.out.println(oo.toString());
//			}
//
//			// ==============================================================================
//			System.out.println("\n==============Get order by user ID=========================\n");
//			ArrayList<Order> odr = new ArrayList<Order>();
//			odr = db.getUserOrdersByUserID(connObj, om, 2);
//			for (Order d : odr) {
//				System.out.println(d.toString());
//			}
//
//			System.out.println("\n==============get order by product id======================\n");
//			ArrayList<Order> oder = new ArrayList<Order>();
//			oder = db.getUserOrdersByProductID(connObj, om, 2);
//			for (Order e : oder) {
//				System.out.println(e.toString());
//			}
//
//			System.out.println("\n==============Get all Categories======================\n");
//			Metamodel<Category> cm = new Metamodel<>(Category.class);
//			ArrayList<Category> cate = new ArrayList<Category>();
//			cate = db.getAllCategories(connObj, cm);
//			for (Category ca : cate) {
//				System.out.println(ca.toString());
//			}
//
//			System.out.println("\n=============Categories by Primary Key======================\n");
//			ArrayList<Category> categ = new ArrayList<Category>();
//			categ = db.getAllCategoriesByPrimaryKey(connObj, cm, 1);
//			for (Category caz : categ) {
//				System.out.println(caz.toString());
//			}
//
//			System.out.println("\n=============All products=====================\n");
//			ArrayList<Product> prod = new ArrayList<Product>();
//			prod = db.getAllProducts(connObj, pm);
//			for (Product p : prod) {
//				System.out.println(p.toString());
//			}
//
//			System.out.println("\n============Products By Primary Key=====================\n");
//			Product prod1 = null;
//			prod1 = db.getAllProductsByPrimaryKey(connObj, pm, 1);
//			
//			System.out.println(prod1.toString());
//			
//			
//
//			System.out.println("\n=============Products by Foreign Key=====================\n");
//			ArrayList<Product> prod2 = new ArrayList<Product>();
//			prod2 = db.getAllProductsByForeignKey(connObj, pm, 2);
//			for (Product p : prod2) {
//				System.out.println(p.toString());
//			}
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}

}
