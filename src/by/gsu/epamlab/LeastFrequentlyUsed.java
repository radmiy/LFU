package by.gsu.epamlab;

import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class LeastFrequentlyUsed {
	private class StringValue {
		private String key;
		private int value;
		private int level = -1;
		
		public StringValue(String key, int value) {
			this.key = key == null ? "" : key;
			this.value = value;
		}
	}
	private final double ENVICTION_FACTOR;
	//number remove item than cache is full
	private final int REMOVE_ITEMS; 
	private final int CACHE_SIZE;
	//number of level
	private final int FREQ = 5;
	Map<String, StringValue> cacheMap = new HashMap<>();
	LinkedList<String>[] arrayLFU;
	
	@SuppressWarnings("unchecked")
	public LeastFrequentlyUsed(int cacheSize, double evictioFactor) {
		CACHE_SIZE = cacheSize;
		ENVICTION_FACTOR = evictioFactor;
		REMOVE_ITEMS = (int) (cacheSize * ENVICTION_FACTOR);
		
		arrayLFU = new LinkedList[FREQ];
		for(int numLevel = 0; numLevel < FREQ; numLevel++) {
			arrayLFU[numLevel] = new LinkedList<String>();
		}
	}
	
	// check capacity and remove items if it is needed
	private void checkCapacity(StringValue item) {
		int count = 0;
		//count all items
		for(LinkedList<String> linkedList : arrayLFU) {
			if(linkedList != null) {
				count += linkedList.size();
			}
		}
		
		double curEvi = (double)count / CACHE_SIZE;
		if(curEvi >= ENVICTION_FACTOR) {
			int remove = REMOVE_ITEMS;
			for(LinkedList<String> linkedList : arrayLFU) {
				while(!linkedList.isEmpty() && remove != 0) {
					//remove item from the cache and the table
					cacheMap.remove(linkedList.getFirst());
					linkedList.removeFirst();
					remove--;
				}
			}
		}
	}
	
	//put item to the cache
	public void putItem(String key, int value) {
		StringValue item = cacheMap.get(key) != null ? cacheMap.get(key) : new StringValue(key, value);
		checkCapacity(item);
		changeLevel(item);
	}
	
	//change level of item
	private void changeLevel(StringValue item) {
		if(item.level != -1) {
			arrayLFU[item.level].remove(item.key);
		}
		if(item.level < FREQ - 1) {
			item.level++;
		}
		arrayLFU[item.level].add(item.key);
		cacheMap.put(item.key, item);
	}
	
	//return level of item
	public int getValue(String key) throws NullPointerException{
		StringValue item = cacheMap.get(key);
		if(item != null) {
			changeLevel(item);
			return item.value;
		}
		throw new NullPointerException();
	}
	
	//remove item from cache
	public void removeItem(String key) {
		if(cacheMap.get(key) != null) {
			arrayLFU[cacheMap.get(key).level].remove(key);
			cacheMap.remove(key);
		}
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		String[][] items = new String[FREQ][CACHE_SIZE];
		for(int numLevel = 0; numLevel < arrayLFU.length; numLevel++) {
			Iterator<String> iterator = arrayLFU[numLevel].iterator();
			int numItem = 0;
			while (iterator.hasNext()) {
				items[numLevel][numItem] = iterator.next();
				numItem++;
			}
		}
		
		for(int numItem = 0; numItem < CACHE_SIZE; numItem++) {
			for(int numLevel = 0; numLevel < arrayLFU.length; numLevel++) {
				@SuppressWarnings("resource")
				Formatter formatter = new Formatter();
				formatter.format("%10s;", items[numLevel][numItem] != null && !items[numLevel][numItem].isEmpty() ? items[numLevel][numItem].trim() : "");
				stringBuilder.append(formatter.toString());
			}
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
}
