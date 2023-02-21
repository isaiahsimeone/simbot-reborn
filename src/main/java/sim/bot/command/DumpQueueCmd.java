package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class DumpQueueCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        manager.get_track_scheduler().dump_queue();
        mce.addReactionsToMessage(Emoji.THUMBS_UP.getCharCode());
    }
}
