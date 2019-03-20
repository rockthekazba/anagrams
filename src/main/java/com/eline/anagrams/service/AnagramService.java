package com.eline.anagrams.service;

import java.util.HashMap;
import java.util.List;

public interface AnagramService {

	public List<String> getAnagramsForWord(String word, int limit, boolean excludeProperNouns);

	public HashMap<String, List<String>> getAnagramGroupsBySize(String[] wordList, int groupSize);

	public HashMap<String, List<String>> getWordsWithMostAnagrams(String[] wordList);

}
