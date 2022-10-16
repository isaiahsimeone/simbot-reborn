package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.util.Emoji;

import java.util.ArrayList;

public class StopCmd implements Executable {
    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        if (player.is_initialised()) {
            player.get_track_scheduler().dump_queue();
            player.get_track_scheduler().next_track(); // Drop current track
            player.get_player().setPaused(false);
            player.destroy();
            mce.getMessage().addReaction(Emoji.WAVE.get_char_code());
        }
    }

    @Override
    public String help() {
        return "- [stop | s | leave | exit | dc | x | shutup | fuckoff]";
    }
}
