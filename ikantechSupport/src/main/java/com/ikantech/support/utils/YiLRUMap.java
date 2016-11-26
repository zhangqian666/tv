package com.ikantech.support.utils;

import java.util.LinkedHashMap;

@SuppressWarnings("serial")
public class YiLRUMap<K, V> extends LinkedHashMap<K, V>
{
	private int mCapacity;

	public YiLRUMap(int initialCapacity)
	{
		super(initialCapacity, 0.75F, true);
		mCapacity = initialCapacity;
	}

	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest)
	{
		return size() > mCapacity;
	}
}
