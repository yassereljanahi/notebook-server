package com.ytakashi.notebook.service.storage;

import java.util.Optional;

/**
 * Storage service for manipulating stored objects.
 * 
 * @author Takashi
 *
 * @param <K>
 *            the type of key.
 * @param <T>
 *            the type of item object.
 */
public interface StorageService<K, T> {

	/**
	 * Create and store object.
	 * 
	 * @param key
	 *            item key.
	 * @return stored item object.
	 */
	public T create(K key);

	/**
	 * Find and return item object using key.
	 * 
	 * @param key
	 *            item key.
	 * @return
	 */
	public Optional<T> find(K key);

	/**
	 * Find item object using key, if not found then create a new one.
	 * 
	 * @param key
	 *            item key
	 * @return found/created item object.
	 */
	public T findOrCreate(K key);

	/**
	 * Reset store.
	 */
	public void clear();

	/**
	 * Remove item from store using the given key.
	 * 
	 * @param key
	 *            item key
	 */
	public void evict(K key);

	/**
	 * Free item resources.
	 * 
	 * @param item
	 *            item object
	 */
	public void freeResources(T item);

}
