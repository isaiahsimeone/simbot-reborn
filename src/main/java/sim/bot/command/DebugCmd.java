package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class DebugCmd implements Executable {

    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        player.toggle_debugging();
        mce.getMessage().addReaction(Emoji.THUMBS_UP.get_char_code());
    }

    @Override
    public String help() {
        return "- [debug | sdb]";
    }
}
