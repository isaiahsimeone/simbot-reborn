package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class MasterCmd implements Executable {
    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        /* Only owner can activate */
        if (!mce.getMessageAuthor().isBotOwner()) {
            mce.getMessage().reply("You aren't Sim...");
            return ;
        }
        /* Disable */
        if (player.in_master_mode())
            mce.getMessage().addReaction(Emoji.FREE.get_char_code());
        else /* Enable */
            mce.getMessage().addReaction(Emoji.BOWING.get_char_code());
        player.toggle_master_mode();
    }

    @Override
    public String help() {
        return "- [iamyourmaster | iaym]";
    }
}
