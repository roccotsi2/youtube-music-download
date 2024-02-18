package com.roccotsi.youtube.music.download;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.roccotsi.youtube.music.download.model.VideoItem;

public class AnalyzeVideoTest {

	private static final String TITLE_VIDEO = "My favorite video title!";
	private static final String TITLE_ORIGINAL_VIDEO = TITLE_VIDEO + " (Original Video)";
	private static final String TITLE_OTHER = "Any other title - yeah";
	private static final int MIN_VIEW_COUNT = AnalyzeVideo.MIN_VIEW_COUNT;

	@ParameterizedTest
	@MethodSource("generateVideoItemStreamNoFit")
	void testCalculateBestFitVideo_NoFit(List<VideoItem> listVideoItems, String originalText) {
		AnalyzeVideo analyzeVideo = new AnalyzeVideo();
		analyzeVideo.calculateTitleSimilarities(listVideoItems, originalText);
		VideoItem bestFit = analyzeVideo.calculateBestFitVideo(listVideoItems, originalText);
		assertNull(bestFit);
	}

	@ParameterizedTest
	@MethodSource("generateVideoItemStreamFit")
	void testCalculateBestFitVideo_Fit(List<VideoItem> listVideoItems, String originalText, String expectedId) {
		AnalyzeVideo analyzeVideo = new AnalyzeVideo();
		analyzeVideo.calculateTitleSimilarities(listVideoItems, originalText);
		VideoItem bestFit = analyzeVideo.calculateBestFitVideo(listVideoItems, originalText);
		assertNotNull(bestFit);
		assertEquals(expectedId, bestFit.getId());
	}

	@ParameterizedTest
	@MethodSource("generateVideoItemStreamJsonFiles")
	void testCalculateBestFitVideo_JsonFiles(List<VideoItem> listVideoItems, String originalText, String expectedId)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		AnalyzeVideo analyzeVideo = new AnalyzeVideo();
		analyzeVideo.calculateTitleSimilarities(listVideoItems, originalText);
		VideoItem bestFit = analyzeVideo.calculateBestFitVideo(listVideoItems, originalText);
		assertNotNull(bestFit);
		assertEquals(expectedId, bestFit.getId());
	}

	private static List<VideoItem> generateListVideoItemsIterateViews(String title, List<Integer> listViewCount) {
		return generateListVideoItemsIterateTitleAndViews(Collections.nCopies(listViewCount.size(), title),
				listViewCount);
	}

	private static List<VideoItem> generateListVideoItemsIterateTitleAndViews(List<String> listTitle,
			List<Integer> listViewCount) {
		assertNotNull(listTitle);
		assertNotNull(listViewCount);
		assertEquals(listTitle.size(), listViewCount.size());
		List<VideoItem> list = new ArrayList<VideoItem>();
		char id = 'A';
		for (int index = 0; index < listTitle.size(); index++) {
			list.add(new VideoItem(String.valueOf(id++), listTitle.get(index), listViewCount.get(index)));
		}
		return list;
	}

	private static Stream<Arguments> generateVideoItemStreamNoFit() {
		List<Arguments> listOfArguments = new LinkedList<>();
		listOfArguments.add(Arguments.of(
				generateListVideoItemsIterateViews(TITLE_VIDEO, Arrays.asList(MIN_VIEW_COUNT - 2, MIN_VIEW_COUNT - 1)),
				TITLE_VIDEO));
//		listOfArguments.add(Arguments.of(generateListVideoItemsIterateViews(StringUtils.reverse(TITLE_VIDEO),
//				Arrays.asList(MIN_VIEW_COUNT, 2 * MIN_VIEW_COUNT)), TITLE_VIDEO));
		listOfArguments.add(Arguments.of(new ArrayList<VideoItem>(), TITLE_VIDEO));
		return listOfArguments.stream();
	}

	private static Stream<Arguments> generateVideoItemStreamFit() {
		List<Arguments> listOfArguments = new LinkedList<>();
		listOfArguments.add(Arguments.of(
				generateListVideoItemsIterateViews(TITLE_VIDEO, Arrays.asList(MIN_VIEW_COUNT, 2 * MIN_VIEW_COUNT)),
				TITLE_VIDEO, "B"));
		listOfArguments.add(Arguments.of(
				generateListVideoItemsIterateViews(TITLE_VIDEO, Arrays.asList(2 * MIN_VIEW_COUNT, MIN_VIEW_COUNT)),
				TITLE_VIDEO, "A"));
		listOfArguments.add(Arguments
				.of(generateListVideoItemsIterateTitleAndViews(Arrays.asList(TITLE_ORIGINAL_VIDEO, TITLE_VIDEO),
						Arrays.asList(MIN_VIEW_COUNT, MIN_VIEW_COUNT)), TITLE_VIDEO, "B"));
		listOfArguments.add(Arguments
				.of(generateListVideoItemsIterateTitleAndViews(Arrays.asList(TITLE_VIDEO, TITLE_ORIGINAL_VIDEO),
						Arrays.asList(MIN_VIEW_COUNT + 1, MIN_VIEW_COUNT)), TITLE_VIDEO, "A"));
		return listOfArguments.stream();
	}

	private static Stream<Arguments> generateVideoItemStreamJsonFiles()
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		List<Arguments> listOfArguments = new LinkedList<>();

		// old working before
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems23.json"),
				"Miley Cyrus - Flowers", "G7KNmW9a75Y"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems24.json"),
				"Culcha Candela - Hamma! (Single Edit)", "oREIJNXvhlo"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems25.json"),
				"Restricted - Big Jet Plane", "SmFYF2emHu8"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems26.json"),
				"Bryan Adams - Summer Of '69", "eFjjO_lhf9c"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems27.json"),
				"Gigi D'Agostino - The Riddle", "cvvd-9azD1M"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems28.json"),
				"Lou Bega - Mambo No. 5 (A Little Bit Of...)", "EK_LN3XEcnw"));

		// old not working before
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems1.json"),
				"ClockClock - Someone Else", "lID03yN2bU0"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems2.json"),
				"Journey - Don't Stop Believin'", "1k8craCGpgs"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems3.json"),
				"Michael Schulte - Better Me", "Kwb5Lfh51JA"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems4.json"),
				"Meduza - Bad Memories [feat. Elley Duhé & FAST BOY]", "T-jNkwesjpk"));
		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems5.json"),
				"Topic - All Or Nothing", "I1Fu8mjaC94"));
//		listOfArguments.add(Arguments.of(readVideoItemsFromFile("src/test/resources/videoitems6.json"),
//				"David Bowie - Under Pressure (1994 Remastered Version)", "v-GGpuuqfy0"));
		return listOfArguments.stream();
	}

	private static List<VideoItem> readVideoItemsFromFile(String path)
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		Gson gson = new Gson();
		Type listType = new TypeToken<ArrayList<VideoItem>>() {
		}.getType();
		List<VideoItem> list = gson.fromJson(new FileReader(path), listType);
		for (VideoItem item : list) {
			item.setVideoTitle(StringEscapeUtils.unescapeHtml4(item.getVideoTitle()));
		}
		return list;
	}

}
