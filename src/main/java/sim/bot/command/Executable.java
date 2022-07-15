package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;

import java.util.ArrayList;


public interface Executable {
    void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args);
    String help();
}
