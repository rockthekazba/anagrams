package com.eline.anagrams.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnagramServiceTest {

	@Autowired
	AnagramService service;

	@Test
	public void testAutoWiring() {
		assertNotNull(service);
	}

	@Test
	public void testGetAnagramsForWord() {
		List<String> anagramSet = service.getAnagramsForWord("read", -1, false);

		assertTrue(anagramSet != null && anagramSet.size() > 0);
	}

	@Test
	public void testGetAnagramsForWordWithLimits() {
		List<String> anagramSet = service.getAnagramsForWord("read", 2, false);
		assertTrue(anagramSet != null && anagramSet.size() == 2);
	}

	@Test
	public void testGetAnagramsIncludeProperNouns() {
		List<String> anagramSet = service.getAnagramsForWord("bale", -1, false);

		assertTrue(anagramSet != null && anagramSet.size() == 6);
	}

	@Test
	public void testGetAnagramsExcludeProperNouns() {
		List<String> anagramSet = service.getAnagramsForWord("bale", -1, true);
		assertTrue(anagramSet != null && anagramSet.size() == 5);
	}

}
