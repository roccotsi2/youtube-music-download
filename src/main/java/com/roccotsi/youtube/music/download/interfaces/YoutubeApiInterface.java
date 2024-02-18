package com.roccotsi.youtube.music.download.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.common.collect.Lists;
import com.roccotsi.youtube.music.download.auth.Auth;
import com.roccotsi.youtube.music.download.model.VideoItem;

public class YoutubeApiInterface implements IYoutubeInterface {

	private YouTube youtube = null;

	@Override
	public List<VideoItem> search(String searchText, int maxVideoCount) throws IOException {
		YouTube youtube = createYouTubeInstance();
		YouTube.Search.List search = youtube.search().list(Arrays.asList("id,snippet"));
		search.setQ(searchText);
		search.setType(Arrays.asList("video"));
		search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
		search.setMaxResults(Long.valueOf(maxVideoCount));

		// Call the API and print results.
		SearchListResponse searchResponse = search.execute();
		List<SearchResult> searchResultList = searchResponse.getItems();
		if (searchResultList != null) {
			List<VideoItem> listVideoItems = extractVideoInformation(searchResultList.iterator(), youtube);
			return listVideoItems;
		} else {
			return new ArrayList<>();
		}
	}

	public void setViewCounts(List<VideoItem> list) throws IOException {
		for (VideoItem videoItem : list) {
			setViewCount(videoItem);
		}
	}

	private YouTube createYouTubeInstance() throws IOException {
		if (youtube != null) {
			return youtube;
		}

		// This OAuth 2.0 access scope allows for full read/write access to the
		// authenticated user's account.
		List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

		Credential credential = Auth.authorize(scopes, "addsubscription");

		youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.GSON_FACTORY, credential)
				.setApplicationName("youtube-cmdline-addsubscription-sample").build();
		return youtube;
	}

	private List<VideoItem> extractVideoInformation(Iterator<SearchResult> iteratorSearchResults, YouTube youtube)
			throws IOException {
		ArrayList<VideoItem> listVideoItems = new ArrayList<>();

		while (iteratorSearchResults.hasNext()) {
			SearchResult singleVideo = iteratorSearchResults.next();
			ResourceId rId = singleVideo.getId();

			// Confirm that the result represents a video. Otherwise, the
			// item will not contain a video ID.
			if (rId.getKind().equals("youtube#video")) {

				VideoItem videoItem = new VideoItem();
				videoItem.setId(rId.getVideoId());
				videoItem.setVideoTitle(singleVideo.getSnippet().getTitle());
				listVideoItems.add(videoItem);
			}
		}

		return listVideoItems;
	}

	private void setViewCount(VideoItem videoItem) throws IOException {
		YouTube youtube = createYouTubeInstance();
		YouTube.Videos.List list = youtube.videos().list(Arrays.asList("statistics"));
		list.setId(Arrays.asList(videoItem.getId()));
		Video ytVideo = list.execute().getItems().get(0);
		videoItem.setViewCount(ytVideo.getStatistics().getViewCount().longValue());
	}

}
