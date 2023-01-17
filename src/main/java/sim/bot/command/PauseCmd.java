package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class PauseCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        boolean is_paused = manager.get_player().isPaused();
        manager.get_player().setPaused(!is_paused);

        String reaction = (is_paused ? Emoji.RESUME.getCharCode() : Emoji.PAUSE.getCharCode());
        mce.getMessage().addReaction(reaction);
    }

    @Override
    public String help() {
        return "- [pause | hold | wait]";
    }
}
