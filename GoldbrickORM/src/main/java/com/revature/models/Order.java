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
 * @author Frank Aurori
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
	 * 
	 */
	@JoinColumn(columnName = "user_id", constraints = {
			Constraint.FOREIGN_KEY }, dataType = "INT", reference = "REFERENCES users(id)")
	private int userID;
	
	/**
	 * 
	 */
	@JoinColumn(columnName = "product_id", constraints = {
			Constraint.FOREIGN_KEY }, dataType = "INT", reference = "REFERENCES products(id)")
	private int productID;

	/**
	 * 
	 */
	@Column(columnName = "order_date", constraints = { Constraint.NOT_NULL }, dataType = "DATE")
	private Date orderDate;

	/**
	 * 
	 */
	@Column(columnName = "total_price", constraints = { Constraint.NOT_NULL }, dataType = "NUMERIC")
	private double totalPrice;

	/**
	 * 
	 */
	@Column(columnName = "fulfilled", constraints = { Constraint.NOT_NULL }, dataType = "BOOLEAN")
	private boolean fulfilled;

	public Order() {

	}

	
	public Order(int userID, int productID, Date orderDate, double totalPrice, boolean fulfilled) {
		super();
		this.userID = userID;
		this.productID = productID;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.fulfilled = fulfilled;
	}

	public Order(int id, int userID, int productID, Date orderDate, double totalPrice, boolean fulfilled) {
		super();
		this.id = id;
		this.userID = userID;
		this.productID = productID;
		this.orderDate = orderDate;
		this.totalPrice = totalPrice;
		this.fulfilled = fulfilled;
	}

	@Getter(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Getter(name = "user_id")
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	@Getter(name = "product_id")
	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	@Getter(name = "order_date")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	@Getter(name = "total_price")
	public double gettotalPrice() {
		return totalPrice;
	}

	public void settotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Getter(name = "fulfilled")
	public boolean isFulfilled() {
		return fulfilled;
	}

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