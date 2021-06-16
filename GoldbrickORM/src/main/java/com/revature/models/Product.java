package com.revature.models;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;

@Entity(tableName = "products")
public class Product {
	
	@Id(columnName = "id", constraint = {Constraint.PRIMARY_KEY, Constraint.SERIAL})
	private int id;
	
	@JoinColumn(columnName = "category_id")
	private int categoryID;
	
	@Column(columnName = "product_name")
	private String productName;
	
	@Column(columnName = "product_description")
	private String productDescription;
	
	@Column(columnName = "price")	
	private double price;
	
	@Column(columnName = "quantity")
	private int quantity;
	
	@Column(columnName = "in_stock")
	private boolean inStock;
	
	public Product(int id, String productName, String productDescription, double price, int quantity, boolean inStock) {
		super();
		this.id = id;
		this.productName = productName;
		this.productDescription = productDescription;
		this.price = price;
		this.quantity = quantity;
		this.inStock = inStock;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

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