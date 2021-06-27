package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Getter;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

/**
 * This class defines a Product class that can be mapped to a database table called products.
 * @author Frank Aurori, Mollie Morrow, Nick Gianino
 *
 */
@Entity(tableName = "products")
public class Product implements AnnotatedClass {
	
	/**
	 * @Id indicates the private field "id" is the primary key of table products
	 */
	@Id(columnName = "id", constraints = { Constraint.PRIMARY_KEY }, dataType = "SERIAL")
	private int id;

	/**
	 * Product has a column name category_id that stores the categoryID value and has a foreign key to the Categories entity, 
	 * has a data type of integer
	 */
	@JoinColumn(columnName = "category_id", constraints = {
			Constraint.FOREIGN_KEY }, dataType = "INT", reference = "REFERENCES categories(id)")
	private int categoryID;

	/**
	 * @column indicates the database column name "product_name" in products table with not null constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "product_name", constraints = { Constraint.NOT_NULL }, dataType = "VARCHAR(50)")
	private String productName;

	/**
	 * @column indicates the database column name "product_description" in products table with not null constraints 
	 * and datatype VARCHAR
	 */
	@Column(columnName = "product_description", constraints = { Constraint.NOT_NULL }, dataType = "VARCHAR(150)")
	private String productDescription;

	/**
	 * @column indicates the database column name "price" in products table with not null constraints 
	 * and datatype NUMERIC
	 */
	@Column(columnName = "price", constraints = { Constraint.NOT_NULL }, dataType = "NUMERIC")
	private double price;

	/**
	 * @column indicates the database column name "quantity" in products table with not null constraints 
	 * and datatype integer
	 */
	@Column(columnName = "quantity", constraints = { Constraint.NOT_NULL }, dataType = "INT")
	private int quantity;

	/**
	 * @column indicates the database column name "in_stock" in products table with not null constraints 
	 * and datatype boolean
	 */
	@Column(columnName = "in_stock", constraints = { Constraint.NOT_NULL }, dataType = "BOOLEAN")
	private boolean inStock;

	/**
	 * Constructor for product object
	 * @param catId
	 * @param productName
	 * @param productDescription
	 * @param price
	 * @param quantity
	 */
	public Product(int catId, String productName, String productDescription, double price, int quantity) {
		super();
		this.categoryID = catId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
	}
	
	/**
	 * Constructor for product object
	 * @param id
	 * @param catId
	 * @param productName
	 * @param productDescription
	 * @param price
	 * @param quantity
	 * @param inStock
	 */
	public Product(int id, int catId, String productName, String productDescription, double price, int quantity,
			boolean inStock) {
		super();
		this.id = id;
		this.categoryID = catId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
		this.inStock = inStock;
	}

	/**
	 * constructor for product object
	 * @param productName
	 * @param productDescription
	 * @param price
	 * @param quantity
	 */
	public Product(String productName, String productDescription, double price, int quantity) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
	}

	/**
	 * constructor for product object
	 */
	public Product() {
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
	 * Method that returns category_id (foreign key)
	 * @return
	 */
	@Getter(name = "category_id")
	public int getCategoryID() {
		return categoryID;
	}

	/**
	 * Method that sets cateogry_id (foreign key) 
	 * @param categoryID
	 */
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	/**
	 * Method that returns product name
	 * @return
	 */
	@Getter(name = "product_name")
	public String getProductName() {
		return productName;
	}
	
	/**
	 * Method that sets the product name
	 * @param productName
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
	 * Method that returns the product description
	 * @return
	 */
	@Getter(name = "product_description")
	public String getProductDescription() {
		return productDescription;
	}
	
	/**
	 * Method that sets the product description
	 * @param productDescription
	 */
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	
	/**
	 * Method that returns the product price
	 * @return
	 */
	@Getter(name = "price")
	public double getPrice() {
		return price;
	}
	
	/**
	 * Method that sets the product price
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Method that returns the product quantity
	 * @return
	 */
	@Getter(name = "quantity")
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Method that sets the product quantity
	 * @param quantity
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	/**
	 * Method that returns true if the product is in_stock
	 * @return
	 */
	@Getter(name = "in_stock")
	public boolean isInStock() {
		if (this.getQuantity() > 0)
			return true;
		else
			return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + (inStock ? 1231 : 1237);
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((productDescription == null) ? 0 : productDescription.hashCode());
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + quantity;
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
		Product other = (Product) obj;
		if (id != other.id)
			return false;
		if (inStock != other.inStock)
			return false;
		if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price))
			return false;
		if (productDescription == null) {
			if (other.productDescription != null)
				return false;
		} else if (!productDescription.equals(other.productDescription))
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "----------------------------------------------------------------------------------------------\n"
				+ id + ": " + productName + ", \'" + productDescription + "\', $" + price + ", "
				+ quantity + " in stock"
				+ "\n----------------------------------------------------------------------------------------------";
	}

}