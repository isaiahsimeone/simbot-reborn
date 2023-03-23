package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

/**
 * The skip command will invoke the track scheduler to play the next song in the track
 * usage: -skip
 */
public class SkipCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        if (manager.is_initialised()) {
            manager.get_track_scheduler().next_track();
            mce.getMessage().addReaction(Emoji.ARROW_RIGHT.getCharCode());
        }
    }
}
