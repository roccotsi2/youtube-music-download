package com.roccotsi.youtube.music.download.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class VideoItem {

	private String id;
	private String videoTitle;
	private long viewCount;
	private int similarityPercent;

	public VideoItem(String id, String videoTitle, long viewCount) {
		this.id = id;
		this.videoTitle = videoTitle;
		this.viewCount = viewCount;
	}

}
