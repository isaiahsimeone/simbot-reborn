package sim.bot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;

import java.util.ArrayList;
import java.util.Queue;

public class QueueListCmd implements Executable {

    @Override
    public void execute(SimPlayer player, MessageCreateEvent mce, ArrayList<String> args) {
        Queue<AudioTrack> queue = player.get_track_scheduler().get_track_queue();

        StringBuilder msg = new StringBuilder();

        if (queue.size() == 0 && player.get_player().getPlayingTrack() == null) {
            msg.append("Nothing in queue");
            mce.getChannel().sendMessage(msg.toString());
            return ;
        }

        msg.append("```");

        /* Song playing now */
        msg.append("Now Playing: ").append(player.get_player().getPlayingTrack().getInfo().title).append("\n");

        int idx = 0;
        for (AudioTrack track : queue) {
            if (idx++ == 0)
                msg.append("Next: ");
            else
                msg.append(idx).append(": ");
            msg.append(track.getInfo().title).append("\n");
        }

        msg.append("```");

        mce.getChannel().sendMessage(msg.toString());
    }

    @Override
    public String help() {
        return "- [queuelist | ql | qlist | list]";
    }
}
