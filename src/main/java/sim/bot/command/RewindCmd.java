package sim.bot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class RewindCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        long seek_size = (args.size() >= 1 ? Integer.parseInt(args.get(0)) * 1000L : 5000);

        AudioTrack playing = manager.get_player().getPlayingTrack();

        if (playing == null)
            return ;

        long current_position = playing.getPosition();
        long set_position = 0;

        /* Don't under-run the song */
        if (current_position >= seek_size)
            set_position = current_position - seek_size;

        manager.get_player().getPlayingTrack().setPosition(set_position);

        mce.getMessage().addReaction(Emoji.REWIND.getCharCode());
    }
}
