package com.roccotsi.youtube.music.download;

import java.io.IOException;
import java.util.List;

import com.roccotsi.youtube.music.download.interfaces.IYoutubeInterface;
import com.roccotsi.youtube.music.download.interfaces.YoutubeApiInterface;
import com.roccotsi.youtube.music.download.model.VideoItem;

public class ExtractVideo {
	private static final int NUMBER_OF_VIDEOS_RETURNED = 25;

	private IYoutubeInterface youtubeInterface = new YoutubeApiInterface();

	public List<VideoItem> search(String searchText) throws IOException {
		return youtubeInterface.search(searchText, NUMBER_OF_VIDEOS_RETURNED);
	}

	public void setViewCounts(List<VideoItem> list) throws IOException {
		((YoutubeApiInterface) youtubeInterface).setViewCounts(list);
	}
}
