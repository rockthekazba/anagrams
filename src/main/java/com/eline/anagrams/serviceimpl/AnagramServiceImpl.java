package com.eline.anagrams.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eline.anagrams.cache.CacheManager;
import com.eline.anagrams.service.AnagramService;
import com.eline.anagrams.utils.AnagramUtil;

@Service
public class AnagramServiceImpl implements AnagramService {

	@Autowired
	AnagramUtil util;

	@Override
	public List<String> getAnagramsForWord(String word, int limit, boolean excludeProperNouns) {
		List<String> anagramsForWordSet = new ArrayList<String>();
		if (word != null) {
			List<String> wordList = CacheManager.getDictionaryMap().get(util.sortedString(word));
			anagramsForWordSet.addAll(wordList);
			// remove the original word from the anagram set because a word is not an
			// anagram of itself
			anagramsForWordSet.remove(word);
			if (limit > 0 && limit < anagramsForWordSet.size()) {
				List<String> limitedAnagramSet = new ArrayList<String>();
				if (limit < anagramsForWordSet.size()) {
					for (String anagramWord : anagramsForWordSet) {
						if (limit > limitedAnagramSet.size()) {
							limitedAnagramSet.add(anagramWord);
						}
					}
				}
				anagramsForWordSet = limitedAnagramSet;
			}
			if (excludeProperNouns) {
				anagramsForWordSet.removeAll(util.filterProperNouns(anagramsForWordSet));
			}
		}

		return anagramsForWordSet;

	}

	@Override
	public HashMap<String, List<String>> getAnagramGroupsBySize(String[] wordList, int groupSize) {
		HashMap<String, List<String>> mapOfWordsWithGroupSize = new HashMap<String, List<String>>();
		for (String word : wordList) {
			List<String> anagramSet = getAnagramsForWord(word.toLowerCase(), -1, false);
			if (anagramSet != null && anagramSet.size() == groupSize) {
				mapOfWordsWithGroupSize.put(word, anagramSet);
			}
		}
		return mapOfWordsWithGroupSize;

	}

	@Override
	public HashMap<String, List<String>> getWordsWithMostAnagrams(String[] wordList) {
		int largestList = 0;
		for (String word : wordList) {
			List<String> anagramSet = getAnagramsForWord(word, -1, false);
			if (anagramSet != null && anagramSet.size() > largestList)
				largestList = anagramSet.size();
		}

		return getAnagramGroupsBySize(wordList, largestList);
	}

}
