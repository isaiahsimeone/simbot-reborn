package sim.bot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

/**
 * The fast-forward command allows a currently playing track to be sought forward through. An optional argument
 * may be specified which indicates the amount to seek through the track by (in seconds)
 * usage: -ff [time in seconds]
 */
public class FastForwardCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        long seek_size = (args.size() >= 1 ? Integer.parseInt(args.get(0)) * 1000L : 5000);

        AudioTrack playing = manager.get_player().getPlayingTrack();

        if (playing == null)
            return ;

        long current_position = playing.getPosition();
        long set_position;

        if (current_position + seek_size >= playing.getDuration())
            set_position = playing.getDuration() - 1;
        else
            set_position = current_position + seek_size;

        manager.get_player().getPlayingTrack().setPosition(set_position);

        mce.getMessage().addReaction(Emoji.FAST_FORWARD.getCharCode());
    }
}
