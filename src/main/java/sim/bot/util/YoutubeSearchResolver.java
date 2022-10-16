package sim.bot.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeSearchResolver {

    public static boolean is_link_formatted(String target) {
        return target.contains("youtube.com/watch?v") || target.contains("youtu.be");
    }

    public static boolean is_soundcloud_link(String target) {
        return target.contains("soundcloud.com") || target.contains("snd.sc");
    }

    public static String resolve_if_required(String songID) {
        // If it's just a song name, then resolve it to a video id
        if (!is_link_formatted(songID) && !is_soundcloud_link(songID))
            songID = get_identifier_from_name(songID);

        return songID;
    }

    /* Given some potential youtube video name, find the first video
     * in the result list and return a youtube link to it
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
