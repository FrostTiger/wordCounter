package ui;

import java.util.HashMap;

import info.debatty.java.stringsimilarity.JaroWinkler;

public class JaroWinklerHashMap<K, V> extends HashMap<K, V> {
	
	public K containsKeyWithJaro(Object word, double jaroValue) {
		JaroWinkler jaroWinkler = new JaroWinkler();
		K result=null;
		for (K key : keySet()) {
			if(jaroWinkler.similarity(key.toString().trim().toLowerCase(), word.toString().trim().toLowerCase())>jaroValue)
				result = key;
			
		}
		
		return result;
	}

}
