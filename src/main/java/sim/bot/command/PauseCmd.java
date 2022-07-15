package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;

import java.util.ArrayList;

public class PauseCmd implements Executable {
    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        boolean is_paused = player.get_player().isPaused();
        player.get_player().setPaused(!is_paused);
    }

    @Override
    public String help() {
        return "- [pause | hold | wait]";
    }
}
