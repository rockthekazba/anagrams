package com.eline.anagrams.cache;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CacheManager {

	private static HashMap<String, String[]> corpusListBySession = new HashMap<String, String[]>();
	private static HashMap<String, List<String>> dictionaryMap = new HashMap<String, List<String>>();

	private CacheManager() {
	}

	public static HashMap<String, List<String>> getDictionaryMap() {
		return CacheManager.dictionaryMap;
	}

	public static void setDictionaryMap(HashMap<String, List<String>> dictionaryMap) {
		CacheManager.dictionaryMap = dictionaryMap;
	}

	public static void setCorpusListBySessionHM(HashMap<String, String[]> corpusListBySession) {
		CacheManager.corpusListBySession = corpusListBySession;
	}

	public static HashMap<String, String[]> getCorpusListBySessionHM() {
		return CacheManager.corpusListBySession;
	}

	public static String[] getCorpusListBySession(String sessionId) {
		String[] corpusListArray = null;
		if (CacheManager.corpusListBySession != null) {
			corpusListArray = corpusListBySession.get(sessionId);
		}
		return corpusListArray;
	}

}
