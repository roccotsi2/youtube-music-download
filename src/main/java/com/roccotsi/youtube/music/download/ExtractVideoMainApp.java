package com.roccotsi.youtube.music.download;

import java.io.IOException;
import java.util.List;

import com.roccotsi.youtube.music.download.model.VideoItem;

public class ExtractVideoMainApp {

	/**
	 * Main method
	 *
	 * @param args command line args.
	 */
	public static void main(String[] args) {
		String filePathCsv = "src/test/resources/songs.csv";
		String targetFolder = "/home/patrick/Downloads/test/";
		SongProcessor songProcessor = new SongProcessor();
		try {
			songProcessor.process(filePathCsv, targetFolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Prints out all results in the Iterator. For each result, print the title,
	 * video ID, and thumbnail.
	 *
	 * @param iteratorSearchResults Iterator of SearchResults to print
	 *
	 * @param query Search query (String)
	 */
	private static void prettyPrint(List<VideoItem> listVideoItems) throws IOException {

		if (listVideoItems != null) {
			for (VideoItem videoItem : listVideoItems) {
				System.out.println(" Video Id: " + videoItem.getId());
				System.out.println(" Title: " + videoItem.getVideoTitle());
				System.out.println(" Views: " + videoItem.getViewCount());
				System.out.println("\n-------------------------------------------------------------\n");
			}
		}
	}

}
