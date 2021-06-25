package com.revature.repos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.revature.models.AnnotatedClass;

/**
 * This class manages the cache for Sessions in GoldbrickORM framework
 * 
 * @author Mollie Morrow, Nick Gianino, Frank Aurori
 * @version 1.0 6/24/21
 */
public class ObjectCache {

	/**
	 * The static cache object
	 */
	private final static ObjectCache cacheObject = new ObjectCache();
	/**
	 * The map that stores the cache
	 */
	private final Map<Class<?>, List<AnnotatedClass>> cache;

	private ObjectCache() {
		super();
		cache = new HashMap<>();
	}

	/**
	 * This method grabs the static instance of ObjectCache
	 * 
	 * @return the static instance of ObjectCache
	 */
	public static ObjectCache getInstance() {
		return cacheObject;
	}

	/**
	 * This method returns the map used to store the cache in the ObjectCache
	 * 
	 * @return the cache map
	 */
	public Map<Class<?>, List<AnnotatedClass>> getCache() {
		return cache;
	}

	/**
	 * This method adds an object capable of being stored in the database to the
	 * Session cache
	 * 
	 * @param object the object to add to the cache
	 */
	public void put(AnnotatedClass object) {
		if (!cache.containsKey(object.getClass()))
			cache.put(object.getClass(), new ArrayList<AnnotatedClass>());
		cache.get(object.getClass()).add(object);

	}

	/**
	 * This method grabs an object out of the cache
	 * 
	 * @param clazz the class type of the object to grab
	 * @param index the object's ID
	 * @return the object if it exists in the cache, null if not
	 */
	public AnnotatedClass get(Class<?> clazz, int index) {
		if (!cache.containsKey(clazz))
			return null;
		List<AnnotatedClass> objects = cache.get(clazz);
		if (index >= objects.size())
			return null;
		return objects.get(index);

	}

	/**
	 * This method grabs all the objects of a specific class being stored in the
	 * cache
	 * 
	 * @param clazz
	 * @return all the objects of a specific class being stored in the cache, null
	 *         if there are none
	 */
	public List<AnnotatedClass> getAllCachedObjectsForClass(Class<?> clazz) {
		return cache.get(clazz);
	}

	/**
	 * This method removes an object from the cache
	 * 
	 * @param object the object to remove;
	 */
	public void remove(AnnotatedClass object) {
		if (cache.containsKey(object.getClass()))
			if (cache.get(object.getClass()).contains(object))
				cache.get(object.getClass()).remove(object);
	}

	/**
	 * This method finds the index of an object within the cache map
	 * 
	 * @param object the object to find the index of
	 * @return the index or -1 if the object is not in the cache
	 */
	public int indexOf(AnnotatedClass object) {
		if (cache.containsKey(object.getClass()))
			if (cache.get(object.getClass()).contains(object))
				return cache.get(object.getClass()).indexOf(object);

		return -1; // error: not present in stack-->search db
	}

	/**
	 * This method finds the index of an object within the cache map
	 * 
	 * @param id the id of the object to find the index of
	 * @return the index or -1 if the object is not in the cache
	 */
	public int indexOf(Class<?> clazz, int id) {
		if (cache.containsKey(clazz)) {
			for (int i = 0; i < cache.get(clazz).size(); i++)
				if (cache.get(clazz).get(i).getId() == id)
					return i;
		}

		return -1;
	}

	/**
	 * This method determines if a object is contained in the cache
	 * 
	 * @param clazz the class of the object
	 * @param id    the id of the object
	 * @return true if it is contained in the cache, false if not
	 */
	public boolean contains(Class<?> clazz, int id) {
		if (cache.containsKey(clazz)) {
			for (AnnotatedClass obj : cache.get(clazz))
				if (obj.getId() == id)
					return true;
		}

		return false;
	}

}
