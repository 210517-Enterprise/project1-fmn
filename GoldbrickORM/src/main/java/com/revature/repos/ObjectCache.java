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

	public Map<Class<?>, List<Object>> getCache() {
		return cache;
	}

	public void put(Object object) {
		if (!cache.containsKey(object.getClass()))
			cache.put(object.getClass(), new ArrayList<Object>());
		cache.get(object.getClass()).add(object);

	}

	public Object get(Class<?> clazz, int index) {
		if (!cache.containsKey(clazz))
			return null;
		List<Object> objects = cache.get(clazz);
		if (index >= objects.size())
			return null;
		return objects.get(index);

	}
	
	public List<Object> getAllCachedObjectsForClass(Class<?> clazz){
		return cache.get(clazz);
	}
	
	public void remove(Object object) {
		if(cache.containsKey(object.getClass()))
			if(cache.get(object.getClass()).contains(object))
				cache.get(object.getClass()).remove(object);
	}
	
	public int indexOf(Object object) {
		if(cache.containsKey(object.getClass()))
			if(cache.get(object.getClass()).contains(object))
				return cache.get(object.getClass()).indexOf(object);
		
		return -1; //error: not present in stack-->search db
	}
	
	

}
