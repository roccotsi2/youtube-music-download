package com.roccotsi.youtube.music.download.interfaces;

import java.io.IOException;
import java.util.List;

import com.roccotsi.youtube.music.download.model.VideoItem;

public interface IYoutubeInterface {
	List<VideoItem> search(String searchText, int maxVideoCount) throws IOException;
}
