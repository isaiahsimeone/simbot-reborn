package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

/**
 * The shuffle command will shuffle the queue of audio tracks to be played. The currently
 * playing song is unaffected
 * usage: -shuffle
 */
public class ShuffleCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        manager.get_track_scheduler().shuffle();
        mce.getMessage().addReaction(Emoji.SHUFFLE.getCharCode());
    }
}
