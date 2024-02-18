package com.roccotsi.youtube.music.download;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.roccotsi.youtube.music.download.model.Song;
import com.roccotsi.youtube.music.download.model.VideoItem;

public class SongProcessor {
	private SongFileLoader songFileLoader = new SongFileLoader();
	private AnalyzeVideo analyzeVideo = new AnalyzeVideo();
	private ExtractVideo extractVideo = new ExtractVideo();
	private SongDownloader songDownloader = new SongDownloader();

	public void process(String filePathCsv, String targetFolder) throws IOException, InterruptedException {
		List<Song> listSong = songFileLoader.loadSongs(filePathCsv);
		for (Song song : listSong) {
			VideoItem videoItem = searchBestVideoItem(song);
			if (videoItem != null) {
				System.out.println(ExtractVideoUtil.generateSongTitle(song) + ": " + song.getYoutubeLink());
				songDownloader.downloadSongs(Arrays.asList(song), targetFolder);
			} else {
				System.out.println(ExtractVideoUtil.generateSongTitle(song) + ": not found");
			}
		}
	}

	private VideoItem searchBestVideoItem(Song song) throws IOException {
		// try to find song on Youtube
		String searchTextPlain = ExtractVideoUtil.generateSongTitle(song);
		List<VideoItem> listVideoItemPlain = extractVideo.search(searchTextPlain);

		extractVideo.setViewCounts(listVideoItemPlain);

		analyzeVideo.calculateTitleSimilarities(listVideoItemPlain, searchTextPlain);

		// ExtractVideoUtil.writeVideoItemsAsJsonToFile(listVideoItemPlain);

		// Select the video that fits best
		VideoItem videoItemPlain = calculateBestFitVideoAndSetYoutubeLink(song, listVideoItemPlain, searchTextPlain);

		return videoItemPlain;
	}

	private VideoItem calculateBestFitVideoAndSetYoutubeLink(Song song, List<VideoItem> listVideoItem,
			String searchText) {
		if (!listVideoItem.isEmpty()) {
			// at least one video found -> select the best fit
			VideoItem videoItem = analyzeVideo.calculateBestFitVideo(listVideoItem, searchText);
			if (videoItem != null) {
				// best fit found -> set it in Song
				song.setYoutubeLink("https://www.youtube.com/watch?v=" + videoItem.getId());
				return videoItem;
			}
		}
		return null;
	}

}
