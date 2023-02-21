package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class MasterCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        /* Only owner can activate */
        if (!mce.getMessageAuthor().isBotOwner()) {
            mce.getMessage().reply("You aren't Sim...");
            return ;
        }
        /* Disable */
        if (manager.in_master_mode())
            mce.getMessage().addReaction(Emoji.FREE.getCharCode());
        else /* Enable */
            mce.getMessage().addReaction(Emoji.BOWING.getCharCode());
        manager.toggle_master_mode();
    }
}
