package com.roccotsi.youtube.music.download.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Song {

	private String interpret;
	private String title;
	private String youtubeLink;

}
