package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

/**
 * The Debug command allows for the enabling/disabling of verbose message logging to the discord
 * server specified.
 * Usage:   -debug
 */
public class DebugCmd implements Executable {

    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        manager.toggle_debugging();
        mce.getMessage().addReaction(Emoji.THUMBS_UP.getCharCode());
    }
}
