package com.roccotsi.youtube.music.download;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.LongestCommonSubsequence;

import com.roccotsi.youtube.music.download.model.VideoItem;

public class AnalyzeVideo {

	public static final int MIN_VIEW_COUNT = 1000;
	public static final int MIN_SIMILARITY_PERCENT = 80;

	public VideoItem calculateBestFitVideo(List<VideoItem> list, String originalSearchText) {
		list = filterVideosByViewCount(list);
		ExtractVideoUtil.sortListVideoItems(list);
		if (!list.isEmpty()) {
			return list.get(list.size() - 1);
		} else {
			return null;
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

	private void calculateTitleSimilarity(VideoItem videoItem, String originalSearchText) {
		String videoTitleCleaned = ExtractVideoUtil.cleanVideoTitle(videoItem.getVideoTitle());
		Integer score = calculateScore(videoTitleCleaned, originalSearchText);
		if (score != null) {
			int similarityPercent = calculateSimilarityPercent(score, videoTitleCleaned, originalSearchText);
			videoItem.setSimilarityPercent(similarityPercent);
		}
	}

	private Integer calculateScore(String videoTitle, String originalSearchText) {
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

	private int calculateSimilarityPercent(int score, String videoTitle, String originalSearchText) {
		int baseLength = Math.max(videoTitle.length(), originalSearchText.length());
		if (!StringUtils.isBlank(originalSearchText)) {
			return (int) Math.round(((double) score / baseLength) * 100);
		} else {
			return 0;
		}
	}

}
