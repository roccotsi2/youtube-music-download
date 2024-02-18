package com.roccotsi.youtube.music.download;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LongestCommonSubsequence;

import com.roccotsi.youtube.music.download.model.VideoItem;

public class AnalyzeVideo {

	public static final int MIN_VIEW_COUNT = 10000;
	public static final int MIN_SIMILARITY_PERCENT = 80;

	public VideoItem calculateBestFitVideo(List<VideoItem> list, String originalSearchText) {
		List<VideoItem> copiedList = new ArrayList<VideoItem>(list);
		copiedList = filterVideosByViewCount(copiedList);
		copiedList = filterVideosBySimilarity(copiedList);
		ExtractVideoUtil.sortListVideoItemsFirstSimilarityThenViewCount(copiedList);
		if (!copiedList.isEmpty()) {
			return copiedList.get(copiedList.size() - 1);
		} else {
			// no video found with strong criteria, weaken the criteria a bit and check
			// again
			copiedList = new ArrayList<VideoItem>(list);
			copiedList = filterVideosByViewCount(copiedList);
			copiedList = filterVideosBySimilaritySearchText(copiedList);
			ExtractVideoUtil.sortListVideoItemsByViewCount(copiedList);
			if (!copiedList.isEmpty()) {
				return copiedList.get(copiedList.size() - 1);
			} else {
				return null;
			}
		}
	}

	public void calculateTitleSimilarities(List<VideoItem> list, String originalSearchText) {
		for (VideoItem videoItem : list) {
			calculateTitleSimilarity(videoItem, originalSearchText);
		}
	}

	public List<VideoItem> filterVideosBySimilarity(List<VideoItem> list) {
		// filter out all videos that title does not match
		if (list != null) {
			return list.stream().filter(v -> (v.getSimilarityPercent() >= MIN_SIMILARITY_PERCENT))
					.collect(Collectors.toList());
		}
		return null;
	}

	public List<VideoItem> filterVideosBySimilaritySearchText(List<VideoItem> list) {
		// filter out all videos that title does not match
		if (list != null) {
			return list.stream().filter(v -> (v.getSimilarityPercentSearchText() >= MIN_SIMILARITY_PERCENT))
					.collect(Collectors.toList());
		}
		return null;
	}

	private void calculateTitleSimilarity(VideoItem videoItem, String originalSearchText) {
		String videoTitleCleaned = ExtractVideoUtil.cleanVideoTitle(videoItem.getVideoTitle());
		Integer longestCommonSubsequenceLength = calculateLongestCommonSubsequenceLength(videoTitleCleaned,
				originalSearchText);
		if (longestCommonSubsequenceLength != null) {
			int similarityPercent = calculateSimilarityPercent(longestCommonSubsequenceLength, videoTitleCleaned,
					originalSearchText, true);
			int similarityPercentMin = calculateSimilarityPercent(longestCommonSubsequenceLength, videoTitleCleaned,
					originalSearchText, false);
			videoItem.setSimilarityPercent(similarityPercent);
			videoItem.setSimilarityPercentSearchText(similarityPercentMin);
		}
	}

	private Integer calculateLongestCommonSubsequenceLength(String videoTitle, String originalSearchText) {
		LongestCommonSubsequence algorithm = new LongestCommonSubsequence();
		Integer longestSubsequenceLength = algorithm.apply(videoTitle.toLowerCase(), originalSearchText.toLowerCase());
		if (longestSubsequenceLength != null) {
			return longestSubsequenceLength;
		} else {
			return null;
		}
	}

	private List<VideoItem> filterVideosByViewCount(List<VideoItem> list) {
		// filter out all videos that have not enough views
		if (list != null) {
			return list.stream().filter(v -> (v.getViewCount() >= MIN_VIEW_COUNT)).collect(Collectors.toList());
		}
		return null;
	}

	private int calculateSimilarityPercent(int score, String videoTitle, String originalSearchText,
			boolean calculateFromMaxLength) {
		int baseLength;
		if (calculateFromMaxLength) {
			baseLength = Math.max(videoTitle.length(), originalSearchText.length());
		} else {
			baseLength = originalSearchText.length();
		}
		if (!StringUtils.isBlank(originalSearchText)) {
			return Math.min(100, (int) Math.round(((double) score / baseLength) * 100));
		} else {
			return 0;
		}
	}

}
