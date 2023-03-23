package sim.bot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;

import java.util.ArrayList;
import java.util.Queue;

/**
 * The queue list command lists out the songs currently in the queue of songs to play. This information
 * is printed in the same text chat that the command was issued.
 * usage: -ql
 */
public class QueueListCmd implements Executable {

    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        Queue<AudioTrack> queue = manager.get_track_scheduler().get_track_queue();

        StringBuilder msg = new StringBuilder();

        if (queue.size() == 0 && manager.get_player().getPlayingTrack() == null) {
            msg.append("Nothing in queue");
            mce.getChannel().sendMessage(msg.toString());
            return ;
        }

        msg.append("```");

        /* Song playing now */
        msg.append("Now Playing: ").append(manager.get_player().getPlayingTrack().getInfo().title).append("\n");

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
}
