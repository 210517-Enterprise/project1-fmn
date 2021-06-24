package com.revature.repos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectCache {
	
	private final static ObjectCache cacheObject = new ObjectCache();
	private final Map<Class<?>, List<Object>> cache;
	
	private ObjectCache() {
		super();
		cache = new HashMap<>();
	}
	
	public static ObjectCache getInstance() {
		return cacheObject;
	}
	
	public Map<Class<?>, List<Object>> getCache(){
		return cache;
	}
	
	public void putInCache(Object object) {
		if(!cache.containsKey(object.getClass()))
			cache.put(object.getClass(), new ArrayList<Object>());
		cache.get(object.getClass()).add(object);
		
	}

}
