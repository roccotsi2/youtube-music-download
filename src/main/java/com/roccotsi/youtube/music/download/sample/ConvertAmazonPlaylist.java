package com.roccotsi.youtube.music.download.sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ConvertAmazonPlaylist {

	/**
	 * Main method
	 *
	 * @param args command line args.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		try (BufferedReader buffer = new BufferedReader(new FileReader(new File("src/test/resources/playlist3.txt")))) {
			String line = buffer.readLine();
			int lineNo = 1;
			String interpret = null;
			String title = null;
			List<List<String>> list = new ArrayList<>();
			while (line != null) {
				if (lineNo == 1) {
					title = line.trim();
				} else if (lineNo == 2) {
					interpret = line.trim();
				}

				if (lineNo < 4) {
					lineNo++;
				} else {
					list.add(Arrays.asList(interpret, title));
					lineNo = 1;
				}
				line = buffer.readLine();
			}

			CSVPrinter csvFilePrinter = null;
			CSVFormat csvFileFormat = CSVFormat.DEFAULT;
			FileWriter fileWriter = new FileWriter("src/test/resources/playlist3.csv");
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

			csvFilePrinter.printRecords(list);

			fileWriter.flush();
			fileWriter.close();
			csvFilePrinter.close();
		}
	}

}
