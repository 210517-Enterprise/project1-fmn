package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Getter;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

@Entity(tableName = "products")
public class Product {
	
	@Id(columnName = "id", constraints = {Constraint.PRIMARY_KEY}, dataType = "SERIAL")
	private int id;
	
	@JoinColumn(columnName = "category_id", constraints = {Constraint.FOREIGN_KEY}, dataType = "INT", reference = "REFERENCES categories(id)")
	private int categoryID;
	
	@Column(columnName = "product_name", constraints = {Constraint.NOT_NULL}, dataType = "VARCHAR(50)")
	private String productName;
	
	@Column(columnName = "product_description", constraints = {Constraint.NOT_NULL}, dataType = "VARCHAR(150)")
	private String productDescription;
	
	@Column(columnName = "price", constraints = {Constraint.NOT_NULL}, dataType = "NUMERIC")	
	private double price;
	
	@Column(columnName = "quantity", constraints = {Constraint.NOT_NULL}, dataType = "INT")
	private int quantity;
	
	@Column(columnName = "in_stock", constraints = {Constraint.NOT_NULL}, dataType = "BOOLEAN")
	private boolean inStock;
	
	public Product(int catId, String productName, String productDescription, double price, int quantity) {
		super();
		this.categoryID = catId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
	}
	
	public Product(int id, int catId, String productName, String productDescription, double price, int quantity, boolean inStock) {
		super();
		this.id = id;
		this.categoryID = catId;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
		this.inStock = inStock;
	}
	
	public Product(String productName, String productDescription, double price, int quantity) {
		super();
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
	}

	public Product() {
	}

	@Getter(name = "id")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Getter(name = "category_id")
	public int getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}

	@Getter(name = "product_name")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Getter(name = "product_description")
	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	@Getter(name = "price")
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Getter(name = "quantity")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Getter(name = "in_stock")
	public boolean isInStock() {
		if(this.getQuantity() > 0)
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
		return "Product [id=" + id + ", productName=" + productName + ", productDescription=" + productDescription
				+ ", price=" + price + ", quantity=" + quantity + ", inStock=" + inStock + "]";
	}

}