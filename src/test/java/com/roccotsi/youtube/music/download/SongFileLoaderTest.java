package com.roccotsi.youtube.music.download;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.roccotsi.youtube.music.download.SongFileLoader;
import com.roccotsi.youtube.music.download.model.Song;

public class SongFileLoaderTest {

	@Test
	public void testLoadSongs() throws IOException {
		SongFileLoader songFileLoader = new SongFileLoader();
		List<Song> listSongs = songFileLoader.loadSongs("src/test/resources/songs.csv");
		assertNotNull(listSongs);
		assertEquals(3, listSongs.size());
	}

}
