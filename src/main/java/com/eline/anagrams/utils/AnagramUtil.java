package com.eline.anagrams.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.eline.anagrams.cache.CacheManager;
import com.eline.anagrams.domain.CorpusStats;

@Component
public class AnagramUtil {

	@Autowired
	ResourceLoader loader;

	public static final String DICTIONARY_FILE = "dictionary.txt";

	private static final Logger log = LoggerFactory.getLogger(AnagramUtil.class);

	@Autowired
	private ApplicationContext appContext;

	public void loadDictionaryMap() {
		log.info("**********Staring loading of dictionary****************");
		Resource resource = appContext.getResource("classpath:dictionary.txt");
		try {
			InputStream is = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String word;
			String key = null;
			while ((word = br.readLine()) != null) {
				key = sortedString(word);
				List<String> wordSet = CacheManager.getDictionaryMap().get(key);
				if (wordSet == null) {
					wordSet = new ArrayList<String>();
				}
				wordSet.add(word);
				CacheManager.getDictionaryMap().put(key, wordSet);

			}
			br.close();
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		log.info("Finish loading dictionary.txt to memory. # Of Records " + CacheManager.getDictionaryMap().size());
	}

	public HashMap<String, List<String>> buildAnagramDictionaryByList(String[] wordsAnagramDictionary) {

		HashMap<String, List<String>> anagramWordList = new HashMap<String, List<String>>();

		if (wordsAnagramDictionary != null && wordsAnagramDictionary.length > 0) {
			String key = null;
			for (String word : wordsAnagramDictionary) {
				key = sortedString(word);
				List<String> wordSet = anagramWordList.get(key);
				if (wordSet == null) {
					wordSet = new ArrayList<String>();
				}
				wordSet.add(word);
				anagramWordList.put(key, wordSet);

			}
		}
		return anagramWordList;

	}

	// assumes proper nouns are words that are capitalized
	public List<String> filterProperNouns(List<String> wordSet) {
		List<String> properNounSet = new ArrayList<String>(wordSet.size());
		if (wordSet != null) {
			for (String word : wordSet) {
				char firstLetter = word.charAt(0);
				if (Character.isUpperCase(firstLetter)) {
					properNounSet.add(word);
				}
			}
		}
		return properNounSet;
	}

	public CorpusStats buildCorpusStats(String[] corpusWordList) {
		CorpusStats corpusStats = new CorpusStats();
		if (corpusWordList != null) {
			List<Integer> wordStatList = new ArrayList<Integer>(corpusWordList.length);
			int sumOfWordLengths = 0;
			int[] numberArray = new int[corpusWordList.length];
			int counter = 0;
			for (String word : corpusWordList) {
				wordStatList.add(word.length());
				sumOfWordLengths = sumOfWordLengths + word.length();
				numberArray[counter] = word.length();
				counter++;
			}
			corpusStats.setWordCount(corpusWordList.length);
			corpusStats.setMinWordLength(Collections.min(wordStatList));
			corpusStats.setMaxWordLength(Collections.max(wordStatList));
			corpusStats.setAverageWordLength((double) sumOfWordLengths / (double) (corpusWordList.length));
			// calculate median
			double median = 0.0;
			Arrays.sort(numberArray);
			if (numberArray.length % 2 == 0) {
				median = ((double) numberArray[numberArray.length / 2]
						+ (double) numberArray[numberArray.length / 2 - 1]) / 2;
			} else {
				median = (double) numberArray[numberArray.length / 2];
			}
			corpusStats.setMedianWordLength(median);

		}

		return corpusStats;
	}

	public String sortedString(String origString) {
		origString = origString.toLowerCase();
		char[] wordCharArray = origString.toCharArray();
		Arrays.sort(wordCharArray);
		return new String(wordCharArray).toLowerCase();
	}

	public Integer intFromString(String num) {
		int intFromString = -1;
		try {
			intFromString = Integer.valueOf(num);
		} catch (NumberFormatException nfe) {
			// eat is and return -1
		}
		return intFromString;
	}

}
