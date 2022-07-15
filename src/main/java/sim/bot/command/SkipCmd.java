package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;

import java.util.ArrayList;

public class SkipCmd implements Executable {
    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        if (player.is_initialised())
            player.get_track_scheduler().next_track();
    }

    @Override
    public String help() {
        return "- [skip | next | n | thissongshit | nextsong]";
    }
}
