package com.ytakashi.notebook.service.storage;

import java.util.Optional;

public interface SessionStorage<K, T> {

	public T create(K key);

	public Optional<T> find(K key);

	public T findOrCreate(K key);

	public void clear();

	public void evict(K key);
	
	public void freeResources(T item);

}
