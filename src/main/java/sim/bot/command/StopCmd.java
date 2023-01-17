package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class StopCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        if (manager.is_initialised()) {
            manager.destroy();
            mce.getMessage().addReaction(Emoji.WAVE.getCharCode());
        }
    }

    @Override
    public String help() {
        return "- [stop | s | leave | exit | dc | x | shutup | fuckoff]";
    }
}
