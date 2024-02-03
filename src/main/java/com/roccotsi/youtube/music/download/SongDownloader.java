package com.roccotsi.youtube.music.download;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.roccotsi.youtube.music.download.model.Song;

public class SongDownloader {

	public void downloadSongs(List<Song> listSong, String targetFolder) throws IOException, InterruptedException {
		for (Song song : listSong) {
			if (song.getYoutubeLink() != null) {
				List<String> command = generateYoutubeDlCommmand(song, targetFolder);

//				ProcessBuilder builder = new ProcessBuilder();
//				builder.command(command);
//				// builder.directory(new File(System.getProperty("user.home")));
//				Process process = builder.start();
//				String output = IOUtils.toString(builder.start().getInputStream(), StandardCharsets.UTF_8);
//				int exitCode = process.waitFor();

				ProcessBuilder ps = new ProcessBuilder(command);

				// From the DOC: Initially, this property is false, meaning that the
				// standard output and error output of a subprocess are sent to two
				// separate streams
				ps.redirectErrorStream(true);

				Process pr = ps.start();

				BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}
				int exitCode = pr.waitFor();
				System.out.println("ok!");

				if (exitCode == 0) {
					System.out.println("Song successfully downloaded: " + ExtractVideoUtil.generateSongTitle(song));
				} else {
					System.out.println("Error downloading Song: " + ExtractVideoUtil.generateSongTitle(song));
//					System.out.println(output);
				}
			} else {
				System.out.println("No video found for: " + ExtractVideoUtil.generateSongTitle(song));
			}
		}
	}

	private List<String> generateYoutubeDlCommmand(Song song, String targetFolder) {
		// TODO: remove invalid filename characters
//		return "/usr/local/bin/youtube-dl --extract-audio --audio-format mp3 -o \"" + targetFolder
//				+ ExtractVideoUtil.generateSongTitle(song) + ".%(ext)s\" " + song.getYoutubeLink();
		return Arrays.asList("youtube-dl", "--extract-audio", "--audio-format", "mp3", "-o",
				"" + targetFolder + ExtractVideoUtil.generateSongTitle(song) + ".%(ext)s", song.getYoutubeLink());
	}

}
