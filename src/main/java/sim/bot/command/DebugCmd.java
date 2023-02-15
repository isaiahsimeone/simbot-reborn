package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class DebugCmd implements Executable {

    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        manager.toggle_debugging();
        mce.getMessage().addReaction(Emoji.THUMBS_UP.getCharCode());
    }

    @Override
    public String help() {
        return "- [debug | sdb]";
    }
}
