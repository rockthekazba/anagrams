package com.eline.anagrams.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eline.anagrams.cache.CacheManager;
import com.eline.anagrams.domain.CorpusStats;
import com.eline.anagrams.domain.CorpusWords;
import com.eline.anagrams.service.AnagramService;
import com.eline.anagrams.utils.AnagramUtil;

@RestController
public class AnagramRestController {

	@Autowired
	AnagramUtil util;

	@Autowired
	AnagramService service;

	private static final Logger log = LoggerFactory.getLogger(AnagramRestController.class);

	@RequestMapping(value = "/createcorpus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public String[] createCorpus(@RequestBody CorpusWords corpusWords, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		log.info("AnagramRestController createCorpus: Creating corpus in AnagramRestController");

		CacheManager.setCorpusList(corpusWords.getWords());

		return CacheManager.getCorpusList();
	}

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public String[] getCorpus(HttpServletRequest request, HttpServletResponse response) {

		log.info("AnagramRestController.getCorpus: Retrieving all corpus records");
		return CacheManager.getCorpusList();

	}

	@RequestMapping(value = "/{word}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<String> getAnagramsForWordSet(@PathVariable String word, HttpServletRequest request,
			HttpServletResponse response) {

		log.info("AnagramRestController.getAnagramsForWordSet: Retrieveing wordSet for " + word);
		boolean excludeProperNouns = false;

		int maxresults = -1;
		if (request.getParameter("limit") != null)
			maxresults = util.intFromString(request.getParameter("limit"));

		if (request.getParameter("excludeProper") != null)
			excludeProperNouns = Boolean.valueOf(request.getParameter("excludeProper"));

		return service.getAnagramsForWord(word, maxresults, excludeProperNouns);

	}

	@RequestMapping(value = "/{word}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAnagramFromCorpus(@PathVariable String word, HttpServletRequest request,
			HttpServletResponse response) {

		log.info("AnagramRestController.deleteAnagramFromCorpus: Deleting word " + word);
		if (CacheManager.getCorpusList() != null) {
			List<String> newList = new ArrayList<String>();
			Collections.addAll(newList, CacheManager.getCorpusList());
			newList.remove(word);
			String deleteAnagrams = request.getParameter("deleteAnagrams");
			// delete anagrams if indicated
			if (deleteAnagrams != null && deleteAnagrams.equalsIgnoreCase("true")) {
				List<String> anagramList = util.buildAnagramDictionaryByList(CacheManager.getCorpusList())
						.get(util.sortedString(word));
				if (anagramList != null && anagramList.size() > 0)
					newList.removeAll(anagramList);
			}

			CacheManager.setCorpusList(newList.stream().toArray(String[]::new));
		}
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAllFromCorpus(HttpServletRequest request, HttpServletResponse response) {
		log.info("AnagramRestController.deleteAllFromCorpus: Deleting all words from corpus");
		CacheManager.setCorpusList(null);
	}

	@RequestMapping(value = "/corpusstats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public CorpusStats getCorpusStats(HttpServletRequest request, HttpServletResponse response) {

		log.info("AnagramRestController.getCorpusStats: Retrieving corpus stats");
		CorpusStats stats = util.buildCorpusStats(CacheManager.getCorpusList());
		return stats;
	}

	@RequestMapping(value = "/mostanagrams", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public HashMap<String, List<String>> getWordsWithMostAnagrams(HttpServletRequest request,
			HttpServletResponse response) {
		log.info("AnagramRestController.mostanagrams: Retrieving most anagrams");

		return service.getWordsWithMostAnagrams(CacheManager.getCorpusList());
	}

	@RequestMapping(value = "/anagramcomparecheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public List<List<String>> anagramCompareCheck(@RequestBody CorpusWords words, HttpServletRequest request,
			HttpServletResponse response) {

		log.info("AnagramRestController.anagramCompareCheck: Comparing anagrams");
		HashMap<String, List<String>> anagramDictionaryHM = util.buildAnagramDictionaryByList(words.getWords());
		List<List<String>> anagramList = new ArrayList<List<String>>();
		if (anagramDictionaryHM != null && anagramDictionaryHM.size() > 0) {
			for (List<String> value : anagramDictionaryHM.values()) {
				// only include a list that has more than one word as the one word will be
				// itself
				if (value.size() > 1)
					anagramList.add(value);
			}
		}
		return anagramList;
	}

	@RequestMapping(value = "/anagramgroup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public HashMap<String, List<String>> getAnagramGroupBySize(@RequestParam String groupsize,
			HttpServletRequest request, HttpServletResponse response) {

		log.info("AnagramRestController.getAnagramGroupBySize: Group size: " + groupsize);
		int groupSize = 5; // default
		if (groupsize != null)
			groupSize = util.intFromString(groupsize);

		return service.getAnagramGroupsBySize(CacheManager.getCorpusList(), groupSize);
	}

}
