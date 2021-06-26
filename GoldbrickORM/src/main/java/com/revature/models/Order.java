package com.revature.models;

import java.sql.Date;
import java.time.LocalDateTime;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Getter;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

/**
 * This class defines a Order class can be mapped to a database table called orders.
 * @author Frank Aurori, Mollie Morrow, Nick Gianino
 *
 */
@Entity(tableName = "orders")
public class Order implements AnnotatedClass {
	
	/**
	 * @ID indicates the private field "id" is the primary key of table orders
	 */
	@Id(columnName = "id", constraints = { Constraint.PRIMARY_KEY }, dataType = "SERIAL")
	private int id;
	
	/**
	 * Order has a column name user_id that stores the userID value and has a foreign key to the User entity, 
	 * has a data type of integer
	 */
	@JoinColumn(columnName = "user_id", constraints = {
			Constraint.FOREIGN_KEY }, dataType = "INT", reference = "REFERENCES users(id)")
	private int userID;
	
	/**
	 * Order has a column name product_id that stores the productID value and has a foreign key to the Product entity, 
	 * has a data type of integer
	 */
	@JoinColumn(columnName = "product_id", constraints = {
			Constraint.FOREIGN_KEY }, dataType = "INT", reference = "REFERENCES products(id)")
	private int productID;

	/**
	 * @column indicates the database column name "order_date" in order table with not null constraints 
	 * and datatype DATE
	 */
	@Column(columnName = "order_date", constraints = { Constraint.NOT_NULL }, dataType = "DATE")
	private Date orderDate;

	/**
	 * @column indicates the database column name "total_price" in order table with not null constraints 
	 * and datatype NUMBERIC
	 */
	@Column(columnName = "total_price", constraints = { Constraint.NOT_NULL }, dataType = "NUMERIC")
	private double totalPrice;

	/**
	 * @column indicates the database column name "fulfilled" in order table with not null constraints 
	 * and datatype Boolean
	 */
	@Column(columnName = "fulfilled", constraints = { Constraint.NOT_NULL }, dataType = "BOOLEAN")
	private boolean fulfilled;

	public Order() {

	}
	
	/**
	 * Constructor for Order Object
	 * @param userID
	 * @param productID
	 * @param orderDate
	 * @param totalPrice
	 * @param fulfilled
	 */
	
	public Order(int userID, int productID, Date orderDate, double totalPrice, boolean fulfilled) {
		super();
		this.userID = userID;
		this.productID = productID;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.fulfilled = fulfilled;
	}
	/**
	 * Constructor for Order Object
	 * @param id
	 * @param userID
	 * @param productID
	 * @param orderDate
	 * @param totalPrice
	 * @param fulfilled
	 */
	public Order(int id, int userID, int productID, Date orderDate, double totalPrice, boolean fulfilled) {
		super();
		this.id = id;
		this.userID = userID;
		this.productID = productID;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.fulfilled = fulfilled;
	}
	/**
	 * Method that returns id (primary key)
	 */
	@Getter(name = "id")
	public int getId() {
		return id;
	}
	
	/**
	 * Method that sets the primary key Id
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Method that returns user_id (foreign key) 
	 * @return
	 */
	@Getter(name = "user_id")
	public int getUserID() {
		return userID;
	}
	
	/**
	 * Method that sets user_id (foreign key) 
	 * @param userID
	 */
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	/**
	 * Method that returns product_id (foreign key) 
	 * @return
	 */
	@Getter(name = "product_id")
	public int getProductID() {
		return productID;
	}
	
	/**
	 * Method that sets product_id (foreign key) 
	 * @param productID
	 */
	public void setProductID(int productID) {
		this.productID = productID;
	}
	
	/**
	 * method that returns order_date
	 * @return
	 */
	@Getter(name = "order_date")
	public Date getOrderDate() {
		return orderDate;
	}
	
	/**
	 * method that sets the order_date
	 * @param orderDate
	 */
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * method that returns the total price
	 * @return
	 */
	@Getter(name = "total_price")
	public double gettotalPrice() {
		return totalPrice;
	}
	
	/**
	 * method that sets the total price
	 * @param totalPrice
	 */
	public void settotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	/**
	 * method that returns if a order is fulfilled 
	 * @return
	 */
	@Getter(name = "fulfilled")
	public boolean isFulfilled() {
		return fulfilled;
	}
	/**
	 * method that sets if a order is fulfilled
	 * @param fulfilled
	 */
	public void setFulfilled(boolean fulfilled) {
		this.fulfilled = fulfilled;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(totalPrice);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (fulfilled ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((orderDate == null) ? 0 : orderDate.hashCode());
		result = prime * result + productID;
		result = prime * result + userID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		if (Double.doubleToLongBits(totalPrice) != Double.doubleToLongBits(other.totalPrice))
			return false;
		if (fulfilled != other.fulfilled)
			return false;
		if (id != other.id)
			return false;
		if (orderDate == null) {
			if (other.orderDate != null)
				return false;
		} else if (!orderDate.equals(other.orderDate))
			return false;
		if (productID != other.productID)
			return false;
		if (userID != other.userID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "----------------------------------------------------------------------------------------------\n"
				+ id + ": placed by user #" + userID + ", containing product #" + productID + ", "
				+ orderDate + ", $" + totalPrice + ", " + (fulfilled ? "fulfilled" : "pending")
				+ "\n----------------------------------------------------------------------------------------------";
	}

}