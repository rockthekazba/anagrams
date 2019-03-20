package com.eline.anagrams.domain;

import org.springframework.stereotype.Component;

import com.google.gson.annotations.SerializedName;

@Component
public class CorpusWords {

	@SerializedName("words")
	private String[] words;

	public void setWords(String[] words) {
		this.words = words;
	}

	public String[] getWords() {
		return this.words;
	}

}
