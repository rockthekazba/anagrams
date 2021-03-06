package com.eline.anagrams.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import com.eline.anagrams.cache.CacheManager;
import com.eline.anagrams.domain.CorpusWords;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnagramRestControllerTest {

	@Autowired
	AnagramRestController restController;

	@Autowired
	MockHttpServletRequest request;

	@Autowired
	MockHttpServletResponse response;

	@Autowired
	Gson gson;

	private String sessionId = null;

	@Before
	public void createCorpus() throws IOException {
		CorpusWords words = new CorpusWords();
		words.setWords(mockWords());
		restController.createCorpus(words, request, response);
		sessionId = response.getHeader("sessionId");
	}

	@Test
	public void testAutoWiring() {
		assertNotNull(restController);
	}

	@Test
	public void testCorpusWasCreated() throws IOException {
		assertTrue(CacheManager.getCorpusListBySession(sessionId).length == 5);
	}

	@Test
	public void testGetCorpus() {
		request.addHeader("sessionId", sessionId);
		assertTrue(restController.getCorpus(request, response).length == 5);

	}

	@Test
	public void testGetCorpusById() {
		assertNotNull(CacheManager.getCorpusListBySession(sessionId));
	}

	@Test
	public void testGetAnagramsForWord() {
		List<String> anagrams = restController.getAnagramsForWordSet("least", request, response);
		assertTrue(anagrams.size() > 0);
	}

	@Test
	public void testGetWordsWithMostAnagrams() {
		CacheManager.getCorpusListBySessionHM().put(request.getHeader("sessionId"), mockWords());
		request.addHeader("sessionId", sessionId);
		HashMap<String, List<String>> wordsWithMostAnagrams = restController.getWordsWithMostAnagrams(request,
				response);
		assertTrue(wordsWithMostAnagrams.get("least") != null);

	}

	@Test
	public void testDeleteWordFromCorpus() throws IOException {
		request.setParameter("deleteAnagrams", "false");
		request.addHeader("sessionId", sessionId);
		restController.deleteAnagramFromCorpus("resent", request, response);
		assertTrue(CacheManager.getCorpusListBySession(sessionId).length == 4);
	}

	@Test
	public void testDeleteWordAndAnagramsFromCorpus() throws IOException {
		CorpusWords words = new CorpusWords();
		words.setWords(mockWordsAsAntagrams());
		restController.createCorpus(words, request, response);
		request.setParameter("deleteAnagrams", "true");
		request.addHeader("sessionId", sessionId);
		restController.deleteAnagramFromCorpus("spear", request, response);
		assertTrue(CacheManager.getCorpusListBySession(response.getHeader("sessionId")).length == 2);
	}

	@Test
	public void testDeleteAllFromCorpus() {
		request.addHeader("sessionId", sessionId);
		restController.deleteAllFromCorpus(request, response);
		assertTrue(CacheManager.getCorpusListBySession(sessionId) == null);
	}

	@Test
	public void testAnagramCompareCheckAreAnagrams() {
		CorpusWords words = new CorpusWords();
		words.setWords(mockWordsAsAntagrams());
		List<List<String>> anagramList = restController.anagramCompareCheck(words, request, response);
		assertTrue(anagramList != null && anagramList.size() > 0);
	}

	@Test
	public void testAnagramCompareCheckAreNotAnagrams() {
		CorpusWords words = new CorpusWords();
		words.setWords(mockWords());
		List<List<String>> anagramList = restController.anagramCompareCheck(words, request, response);
		assertTrue(anagramList != null && anagramList.size() == 0);
	}

	@Test
	public void testGetAnagramGroupBySize() {
		request.addHeader("sessionId", sessionId);
		HashMap<String, List<String>> anagramGroups = restController.getAnagramGroupBySize(String.valueOf(1), request,
				response);

		boolean oneValueListsOnly = true;
		for (List<String> value : anagramGroups.values()) {
			if (value.size() > 1)
				oneValueListsOnly = false;
		}
		assertTrue(oneValueListsOnly);

	}

	private String[] mockWords() {
		String[] words = new String[5];
		words[0] = "spear";
		words[1] = "least";
		words[2] = "retains";
		words[3] = "insert";
		words[4] = "resent";

		return words;
	}

	private String[] mockWordsAsAntagrams() {
		String[] words = new String[4];
		words[0] = "spear";
		words[1] = "least";
		words[2] = "reaps";
		words[3] = "stale";
		return words;
	}

}
