package astronomia.feature.musicplayer;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import static astronomia.constant.ApplicationConstants.APPLICATION_NAME;
import static astronomia.constant.ApplicationConstants.YOUTUBE_API_KEY;

public class YoutubeHandler {
    private static final Logger log = LoggerFactory.getLogger(YoutubeHandler.class);
    private static final long DEFAULT_MAX_RESULT = 3;

    private YouTube youtubeService = null;
    private long userDefinedMaxResult = 0;

    public YoutubeHandler() {
        try {
            youtubeService = getService();
        } catch (Exception e) {
            log.info("Unable to initialize youtube handler. Error: {}", e.getMessage());
        }
    }

    public String getUrlBySearchTerms(String searchString) throws IOException {
        SearchListResponse response = search(searchString);
        Optional<SearchResult> searchResultOptional = response.getItems().stream().findFirst();

        if (searchResultOptional.isPresent()) {
            return youtubeUrlBuilder(searchResultOptional.get().getId().getVideoId());
        }
        return null;
    }


    private SearchListResponse search(String searchString) throws IOException {
        YouTube.Search.List request = youtubeService.search()
                .list(List.of("id", "snippet"))
                .setKey(YOUTUBE_API_KEY)
                .setMaxResults(getMaxResultOrDefault())
                .setQ(searchString);
        return request.execute();
    }

    private YouTube getService() throws GeneralSecurityException, IOException {
        return new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private long getMaxResultOrDefault() {
        return userDefinedMaxResult == 0 ? DEFAULT_MAX_RESULT : userDefinedMaxResult;
    }

    private String youtubeUrlBuilder(String videoId) {
        return String.format("https://www.youtube.com/watch?v=%s", videoId);
    }
}
