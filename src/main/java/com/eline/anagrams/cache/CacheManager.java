package com.eline.anagrams.cache;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CacheManager {

	private static String[] corpusList = null;
	private static HashMap<String, List<String>> dictionaryMap = new HashMap<String, List<String>>();

	private CacheManager() {
	}

	public static String[] getCorpusList() {
		return CacheManager.corpusList;
	}

	public static void setCorpusList(String[] corpusList) {
		CacheManager.corpusList = corpusList;
	}

	public static HashMap<String, List<String>> getDictionaryMap() {
		return CacheManager.dictionaryMap;
	}

	public static void setDictionaryMap(HashMap<String, List<String>> dictionaryMap) {
		CacheManager.dictionaryMap = dictionaryMap;

	}
}
