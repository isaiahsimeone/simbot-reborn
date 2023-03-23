package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

/**
 * The dump queue command will remove any AudioTrack objects that are currently queued for playback,
 * any currently playing track will not be affected.
 * usage: -dumpqueue
 */
public class DumpQueueCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        manager.get_track_scheduler().dump_queue();
        mce.addReactionsToMessage(Emoji.THUMBS_UP.getCharCode());
    }
}
