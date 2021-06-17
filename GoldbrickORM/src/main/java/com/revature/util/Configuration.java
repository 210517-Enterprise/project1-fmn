package com.revature.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.revature.models.Category;
import com.revature.models.Order;
import com.revature.models.Product;
import com.revature.models.User;

public class Configuration {
	
	private String dbUrl;
	private String dbUsername;
	private String dbPassword;
	private List<Metamodel<Class<?>>> metamodelList;
	
	public Configuration addAnnotatedClass(Class annotatedClass) {
		if(this.metamodelList == null)
			this.metamodelList = new ArrayList<>();
		
		//The of() method transforms a class into an appropriate data model
		//to be transposed into a relation db object
		this.metamodelList.add(Metamodel.of(annotatedClass));
		
		return this;
	}
	
	//need a method to configure db info (url/user/pass)--> write to application.properties
	
	public List<Metamodel<Class<?>>> getMetamodels(){
		return (this.metamodelList == null) ? Collections.emptyList() : this.metamodelList;
	}

}
