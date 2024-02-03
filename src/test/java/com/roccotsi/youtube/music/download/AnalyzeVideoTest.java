package com.roccotsi.youtube.music.download;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
		VideoItem bestFit = analyzeVideo.calculateBestFitVideo(listVideoItems, originalText);
		assertNull(bestFit);
	}

	@ParameterizedTest
	@MethodSource("generateVideoItemStreamFit")
	void testCalculateBestFitVideo_Fit(List<VideoItem> listVideoItems, String originalText, String expectedId) {
		AnalyzeVideo analyzeVideo = new AnalyzeVideo();
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

}
