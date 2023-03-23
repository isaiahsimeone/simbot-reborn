package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

/**
 * The stop command will cause the bot to disconnect from the voice channel that it is connected to
 * in the same server context as the text channel the command was issued in. The entire player will
 * be destroyed and marked as uninitialised.
 * usage: -stop
 */
public class StopCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        if (manager.is_initialised()) {
            // If there is stuff in the queue and someone called stop, save the queue here, so it can be restored
            if (manager.has_db_accessor())
                manager.get_db_accessor().update_or_create_restore_point(manager.get_track_scheduler().get_track_queue(), mce);
            manager.destroy();
            mce.getMessage().addReaction(Emoji.WAVE.getCharCode());
        }
    }
}
