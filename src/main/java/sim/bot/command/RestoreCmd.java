package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The restore command attempts to restore a queue of songs that would have been dumped when the bot was
 * disconnected either deliberately (by command) or due to an error
 * usage: -restore
 */
public class RestoreCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        ArrayList<String> video_ids = manager.get_db_accessor().get_restore_point(mce);

        if (video_ids == null || video_ids.size() == 0) {
            mce.getMessage().reply("Nothing to restore :(");
            return ;
        }

        if (!manager.is_initialised() && mce.getMessageAuthor().getConnectedVoiceChannel().isPresent())
            manager.initialise(mce.getMessageAuthor().getConnectedVoiceChannel().get());

        for (String vid_id : video_ids)
            (new PlayCmd()).execute(manager, mce, new ArrayList<>(Arrays.asList("https://www.youtube.com/watch?v=" + vid_id)));

        manager.write_verbose_message("Enqueued " + video_ids.size() + " videos");

        mce.getMessage().addReaction(Emoji.SCROLL.getCharCode());
    }
}
