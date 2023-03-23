package sim.bot.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeSearchResolver {

    /**
     * Determine whether the specified string is a YouTube link
     * @param target - The string to check
     * @return True if the provided string is a valid YouTube link, false otherwise
     */
    public static boolean is_youtube_link(String target) {
        return target.contains("youtube.com/watch?v") || target.contains("youtu.be");
    }

    /**
     * Determine whether the specified string is a SoundCloud link
     * @param target - The string to check
     * @return True if the provided string is a valid SoundCloud link, false otherwise
     */
    public static boolean is_soundcloud_link(String target) {
        return target.contains("soundcloud.com") || target.contains("snd.sc");
    }

    /**
     * Given a string, determine whether it is a valid YouTube or soundcloud
     * link. If it is not, attempt resolution to a YouTube video URI
     * @param songID - A string potentially containing a YouTube or SoundCloud
     *               URI. Otherwise, it will be interpreted as a search term for YouTube
     * @return - If songID is a valid YouTube or SoundCloud URI, it is returned without
     *          modification, otherwise, it is resolved to a YouTube URI
     */
    public static String resolve_if_required(String songID) {
        if (!is_youtube_link(songID) && !is_soundcloud_link(songID))
            songID = get_identifier_from_name(songID);

        return songID;
    }

    /**
     * Given some potential YouTube video name, find the first video
     * in the result list and return a YouTube URI to that video
     * @param query - The searchterm to provide to YouTube
     * @return A YouTube URI corresponding to the first search result
     * on YouTube with the provided search query
     */
    public static String get_identifier_from_name(String query) {
        String targetVidID;
        try {
            /* URL Encode */
            query = URLEncoder.encode(query, StandardCharsets.UTF_8);

            Document doc = Jsoup.connect("https://www.youtube.com/results?search_query=" + query)
                    .userAgent("Mozilla")
                    .get();

            String pageSrc = doc.outerHtml();
            Pattern videoIDPattern = Pattern.compile("/watch\\?v=\\S{11}");
            Matcher m = videoIDPattern.matcher(pageSrc);

            if (m.find())
                targetVidID = m.group(0);
            else
                targetVidID = "failure";

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

        return "https://www.youtube.com" + targetVidID;
    }
}
