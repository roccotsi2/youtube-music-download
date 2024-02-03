package com.roccotsi.youtube.music.download;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.roccotsi.youtube.music.download.model.Song;

public class SongFileLoader {

	public static final String HEADER_INTERPRET = "Interpret";
	public static final String HEADER_TITLE = "Title";
	private static final String[] HEADERS = { HEADER_INTERPRET, HEADER_TITLE };

	public List<Song> loadSongs(String filePathCsv) throws IOException {
		List<Song> list = new ArrayList<>();

		Reader in = new FileReader(filePathCsv);

		CSVFormat csvFormat = CSVFormat.DEFAULT.builder().setHeader(HEADERS).setSkipHeaderRecord(true).build();

		Iterable<CSVRecord> records = csvFormat.parse(in);

		for (CSVRecord record : records) {
			String interpret = record.get(HEADER_INTERPRET);
			String title = record.get(HEADER_TITLE);
			Song song = new Song(interpret, title, null);
			list.add(song);
		}

		return list;
	}

}
