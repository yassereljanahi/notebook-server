package com.ytakashi.notebook.service.storage.impl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.ytakashi.notebook.service.storage.StorageService;

/**
 * 
 * This is a base class for all session storages.
 * 
 * @author Takashi
 *
 * @param <T>
 *            the type of item object.
 */
public abstract class BaseSessionStorage<T> implements StorageService<String, T> {

	private final Map<String, T> sessionStorage = new ConcurrentHashMap<>();

	public Map<String, T> getSessionStorage() {
		return sessionStorage;
	}

	@Override
	public void clear() {
		sessionStorage.clear();
	}

	@Override
	public void evict(String key) {
		sessionStorage.remove(key);
	}

	@Override
	public Optional<T> find(String key) {
		if (key != null) {
			T t = sessionStorage.get(key);
			if (t != null) {
				return Optional.of(t);
			}
		}
		return Optional.empty();
	}

}
