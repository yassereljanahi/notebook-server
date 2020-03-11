package com.ytakashi.notebook.service.storage.impl;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.ytakashi.notebook.service.storage.SessionStorage;

public abstract class BaseSessionStorage<K, T> implements SessionStorage<K, T> {

	private final Map<K, T> sessionStorage = new ConcurrentHashMap<>();

	public Map<K, T> getSessionStorage() {
		return sessionStorage;
	}

	@Override
	public void clear() {
		sessionStorage.clear();
	}

	@Override
	public void evict(K key) {
		sessionStorage.remove(key);
	}

	@Override
	public Optional<T> find(K key) {
		if (key != null) {
			T t = sessionStorage.get(key);
			if (t != null) {
				return Optional.of(t);
			}
		}
		return Optional.empty();
	}

}
