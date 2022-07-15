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

        System.out.println("Resolved " + song_name_raw + " to " + song_url);

        player.get_audio_manager().loadItem(song_url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.get_track_scheduler().enqueue(track);
                /* React to message */
                mce.getMessage().addReaction(Emoji.THUMBS_UP.get_char_code());
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks())
                    player.get_track_scheduler().enqueue(track);
                /* React to message */
                mce.getMessage().addReaction(Emoji.THUMBS_UP.get_char_code());
                mce.getMessage().addReaction(Emoji.ONE_TWO_THREE_FOUR.get_char_code());
            }

            @Override
            public void noMatches() {
                System.err.println("Found no matches for specified song");
                /* React to message */
                mce.getMessage().addReaction(Emoji.RED_X.get_char_code());
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.err.println("Failed to load song");
                /* React to message */
                mce.getMessage().addReaction(Emoji.RED_X.get_char_code());
            }
        });
    }

    public String help() {
        return "- [play | pla | pl | p] Song name/URL";
    }
}
