package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;

import java.util.ArrayList;

/**
 * Classes implementing this interface are Executable from the context of a discord text chat
 */
public interface Executable {
    void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args);
}
