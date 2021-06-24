package com.revature.repos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.models.AnnotatedClass;

public class ObjectCache {

	private final static ObjectCache cacheObject = new ObjectCache();
	private final Map<Class<?>, List<AnnotatedClass>> cache;

	private ObjectCache() {
		super();
		cache = new HashMap<>();
	}

	public static ObjectCache getInstance() {
		return cacheObject;
	}

	public Map<Class<?>, List<AnnotatedClass>> getCache() {
		return cache;
	}

	public void put(AnnotatedClass object) {
		if (!cache.containsKey(object.getClass()))
			cache.put(object.getClass(), new ArrayList<AnnotatedClass>());
		cache.get(object.getClass()).add(object);

	}

	public AnnotatedClass get(Class<?> clazz, int index) {
		if (!cache.containsKey(clazz))
			return null;
		List<AnnotatedClass> objects = cache.get(clazz);
		if (index >= objects.size())
			return null;
		return objects.get(index);

	}
	
	public List<AnnotatedClass> getAllCachedObjectsForClass(Class<?> clazz){
		return cache.get(clazz);
	}
	
	public void remove(AnnotatedClass object) {
		if(cache.containsKey(object.getClass()))
			if(cache.get(object.getClass()).contains(object))
				cache.get(object.getClass()).remove(object);
	}
	
	public int indexOf(AnnotatedClass object) {
		if(cache.containsKey(object.getClass()))
			if(cache.get(object.getClass()).contains(object))
				return cache.get(object.getClass()).indexOf(object);
		
		return -1; //error: not present in stack-->search db
	}
	
	public int indexOf(Class<?> clazz, int id) {
		if(cache.containsKey(clazz)) {
			for(int i = 0; i < cache.get(clazz).size(); i++)
				if(cache.get(clazz).get(i).getId() == id)
					return i;
		}
		
		return -1;
	}
	
	public boolean contains(Class<?> clazz, int id) {
		if(cache.containsKey(clazz)) {
			for(AnnotatedClass obj : cache.get(clazz))
				if(obj.getId() == id)
					return true;
		}
		
		return false;
	}
	
	

}
