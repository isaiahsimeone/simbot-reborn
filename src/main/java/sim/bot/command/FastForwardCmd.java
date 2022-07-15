package sim.bot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class FastForwardCmd implements Executable {
    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        long seek_size = (args.size() >= 1 ? Integer.parseInt(args.get(0)) : 5000);

        AudioTrack playing = player.get_player().getPlayingTrack();

        if (playing == null)
            return ;

        long current_position = playing.getPosition();
        long set_position;

        if (current_position + seek_size >= playing.getDuration())
            set_position = playing.getDuration() - 1;
        else
            set_position = current_position + seek_size;

        player.get_player().getPlayingTrack().setPosition(set_position);

        mce.getMessage().addReaction(Emoji.FAST_FORWARD.get_char_code());
    }

    @Override
    public String help() {
        return "- [fastforward | ff]";
    }
}
