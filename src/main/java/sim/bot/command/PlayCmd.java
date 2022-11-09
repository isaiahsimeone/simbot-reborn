package sim.bot.command;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.util.Emoji;
import sim.bot.util.YoutubeSearchResolver;

import java.util.ArrayList;

public class PlayCmd implements Executable {

    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        String song_name_raw = String.join(" ", args);

        String song_url = YoutubeSearchResolver.resolve_if_required(song_name_raw);

        if (song_url.contains("Exception"))
            player.write_verbose_message("Got exception: " + song_url);
        if (song_name_raw.equals(song_url))
            player.write_verbose_message("Song already a URL so does not need resolution");
        else
            player.write_verbose_message("Resolved '" + song_name_raw + "' to " + song_url);

        player.get_audio_manager().loadItem(song_url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.get_track_scheduler().enqueue(track);
                player.write_verbose_message("Track: '" + track.getInfo().title + "' enqueued");
                /* React to message */
                mce.getMessage().addReaction(Emoji.THUMBS_UP.get_char_code());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks())
                    player.get_track_scheduler().enqueue(track);
                player.write_verbose_message("Loaded playlist with " + playlist.getTracks().size() + " tracks");
                /* React to message */
                mce.getMessage().addReaction(Emoji.THUMBS_UP.get_char_code());
                mce.getMessage().addReaction(Emoji.ONE_TWO_THREE_FOUR.get_char_code());
            }

            @Override
            public void noMatches() {
                player.write_verbose_message("No matches were found for this song");
                /* React to message */
                mce.getMessage().addReaction(Emoji.RED_X.get_char_code());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                player.write_verbose_message("Failed to load song with exception:\n" + exception.getMessage());
                /* React to message */
                mce.getMessage().addReaction(Emoji.RED_X.get_char_code());
            }
        });
    }

    public String help() {
        return "- [play | pla | pl | p] Song name/URL";
    }
}
