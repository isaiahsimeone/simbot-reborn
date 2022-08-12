package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class VerbosityCmd implements Executable {

    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        if (args.size() != 1) {
            mce.getMessage().reply("Command requires a single integral argument (1 for more verbose)");
            return ;
        }
        int set_to;
        try {
            set_to = Integer.parseInt(args.get(0));
        } catch (Exception ignored) {
            mce.getMessage().reply("Command requires a single integral argument (1 for more verbose)");
            return ;
        }

        player.set_verbosity(set_to);
        mce.getMessage().addReaction(Emoji.THUMBS_UP.get_char_code());
    }

    @Override
    public String help() {
        return "- [verbose | verboseness] int:level";
    }
}
