package com.eline.anagrams.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eline.anagrams.cache.CacheManager;
import com.eline.anagrams.domain.CorpusStats;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnagramUtilTest {

	@Autowired
	AnagramUtil util;

	@Test
	public void testAutoWiring() {
		assertNotNull(util);
	}

	@Test
	public void testLoadDictionaryMap() {
		assertTrue(CacheManager.getDictionaryMap() != null && CacheManager.getDictionaryMap().size() > 0);
	}

	@Test
	public void testCorpusStatsWithEvenList() {
		CorpusStats corpusStats = util.buildCorpusStats(mockWordListEven());
		assertTrue(corpusStats.getWordCount() == 8);
		assertTrue(corpusStats.getMinWordLength() == 6);
		assertTrue(corpusStats.getMaxWordLength() == 14);
		assertTrue(corpusStats.getAverageWordLength() == 10.125);
		assertTrue(corpusStats.getMedianWordLength() == 10);

	}

	@Test
	public void testCorpusStatsWithOddList() {
		CorpusStats corpusStats = util.buildCorpusStats(mockWordListOdd());
		assertTrue(corpusStats.getWordCount() == 5);
		assertTrue(corpusStats.getMinWordLength() == 6);
		assertTrue(corpusStats.getMaxWordLength() == 10);
		assertTrue(corpusStats.getAverageWordLength() == 8.4);
		assertTrue(corpusStats.getMedianWordLength() == 9);
	}

	@Test
	public void testFilterProperNounSet() {
		assertTrue(util.filterProperNouns(this.mockWordListWithProperNouns()).size() == 2);
	}

	private List<String> mockWordListWithProperNouns() {
		List<String> properNounSet = new ArrayList<String>();
		properNounSet.add("Denver");
		properNounSet.add("Boulder");
		properNounSet.add("city");
		properNounSet.add("state");
		properNounSet.add("team");
		properNounSet.add("read");

		return properNounSet;

	}

	private String[] mockWordListEven() {
		String[] wordSet = new String[8];
		wordSet[0] = "wretch"; // 6
		wordSet[1] = "wretched"; // 8
		wordSet[2] = "wretchedly"; // 10
		wordSet[3] = "wretchedness"; // 12
		wordSet[4] = "wretchless"; // 10
		wordSet[5] = "wretchlessly"; // 12
		wordSet[6] = "wretchlessness"; // 14
		wordSet[7] = "wretchock"; // 9
		return wordSet;
	}

	private String[] mockWordListOdd() {
		String[] wordSet = new String[5];
		wordSet[0] = "expert"; // 6
		wordSet[1] = "expertly"; // 8
		wordSet[2] = "expertism"; // 9
		wordSet[3] = "expertize"; // 9
		wordSet[4] = "expertness"; // 10

		return wordSet;
	}

}
