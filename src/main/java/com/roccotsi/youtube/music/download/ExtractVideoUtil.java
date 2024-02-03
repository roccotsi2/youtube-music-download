package com.roccotsi.youtube.music.download;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.roccotsi.youtube.music.download.model.Song;
import com.roccotsi.youtube.music.download.model.VideoItem;

public class ExtractVideoUtil {

	private static final List<String> TITLE_POSTFIX_TO_REMOVE = Arrays.asList("(Original Video)", "(Original Video HD)",
			"(Official Video)", "(Official Video HD)");

	public static String generateSongTitle(Song song) {
		return song.getInterpret() + " - " + song.getTitle();
	}

	public static void sortListVideoItems(List<VideoItem> list) {
		Collections.sort(list, (d1, d2) -> {
			VideoItem item1 = (VideoItem) d1;
			VideoItem item2 = (VideoItem) d2;
			if (item1.getSimilarityPercent() == item2.getSimilarityPercent()) {
				return (int) item1.getViewCount() - (int) item2.getViewCount();
			} else {
				return item1.getSimilarityPercent() - item2.getSimilarityPercent();
			}
		});
	}

	public static String cleanVideoTitle(String title) {
		if (StringUtils.isBlank(title)) {
			return null;
		}
		for (String removeString : TITLE_POSTFIX_TO_REMOVE) {
			title = StringUtils.removeIgnoreCase(title, removeString);
		}
		return title.trim();
	}

}
