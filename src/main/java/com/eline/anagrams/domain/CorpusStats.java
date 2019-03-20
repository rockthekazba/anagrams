package com.eline.anagrams.domain;

import org.springframework.stereotype.Component;

import com.google.gson.annotations.SerializedName;

@Component
public class CorpusStats {

	@SerializedName("wordCount")
	private int wordCount;

	@SerializedName("minWordLength")
	private int minWordLength;

	@SerializedName("maxWordLength")
	private int maxWordLength;

	@SerializedName("medianWordLength")
	private double medianWordLength;

	@SerializedName("averageWordLength")
	private double averageWordLength;

	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	public int getWordCount() {
		return this.wordCount;
	}

	public void setMinWordLength(int minWordLength) {
		this.minWordLength = minWordLength;
	}

	public int getMinWordLength() {
		return this.minWordLength;
	}

	public void setMaxWordLength(int maxWordLength) {
		this.maxWordLength = maxWordLength;
	}

	public int getMaxWordLength() {
		return this.maxWordLength;
	}

	public void setMedianWordLength(double medianWordLength) {
		this.medianWordLength = medianWordLength;
	}

	public double getMedianWordLength() {
		return this.medianWordLength;
	}

	public void setAverageWordLength(double averageWordLength) {
		this.averageWordLength = averageWordLength;
	}

	public double getAverageWordLength() {
		return this.averageWordLength;
	}

}
