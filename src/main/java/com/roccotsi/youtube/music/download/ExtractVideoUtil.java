package com.roccotsi.youtube.music.download;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.roccotsi.youtube.music.download.model.Song;
import com.roccotsi.youtube.music.download.model.VideoItem;

public class ExtractVideoUtil {

	private static int COUNTER = 29;

	public static String generateSongTitle(Song song) {
		return song.getInterpret() + " - " + song.getTitle();
	}

	public static void sortListVideoItemsFirstSimilarityThenViewCount(List<VideoItem> list) {
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

	public static void sortListVideoItemsByViewCount(List<VideoItem> list) {
		Collections.sort(list, (d1, d2) -> {
			VideoItem item1 = (VideoItem) d1;
			VideoItem item2 = (VideoItem) d2;
			return (int) item1.getViewCount() - (int) item2.getViewCount();
		});
	}

	public static String cleanVideoTitle(String title) {
		if (StringUtils.isBlank(title)) {
			return null;
		}
		title = StringEscapeUtils.unescapeHtml4(title);

		title = title.trim();

		String contentLastBrackets = extractLastBrackets(title);
		if (contentLastBrackets != null) {
			if (StringUtils.containsIgnoreCase(contentLastBrackets, "original")
					|| StringUtils.containsIgnoreCase(contentLastBrackets, "official")) {
				// remove the last brackets
				title = StringUtils.removeIgnoreCase(title, contentLastBrackets);
			}
		}

		return title.trim();
	}

	public static String removeLastBrackets(String title) {
		int indexLastLeftBracket = -1;
		if (title.endsWith(")")) {
			indexLastLeftBracket = title.lastIndexOf("(");
		} else if (title.endsWith("]")) {
			indexLastLeftBracket = title.lastIndexOf("[");
		}
		if (indexLastLeftBracket > -1) {
			return title.substring(0, indexLastLeftBracket);
		} else {
			return title;
		}
	}

	private static String extractLastBrackets(String title) {
		if (title.endsWith(")")) {
			int indexLastLeftBracket = title.lastIndexOf("(");
			return title.substring(indexLastLeftBracket, title.length());
		} else if (title.endsWith("]")) {
			int indexLastLeftBracket = title.lastIndexOf("[");
			return title.substring(indexLastLeftBracket, title.length());
		}
		return null;
	}

	public static void writeVideoItemsAsJsonToFile(List<VideoItem> list) throws IOException {
		String fileName = "src/test/resources/videoitems" + COUNTER + ".json";
		COUNTER++;

		Path path = Paths.get(fileName);

		try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonElement tree = gson.toJsonTree(list);
			gson.toJson(tree, writer);
		}
	}

}
