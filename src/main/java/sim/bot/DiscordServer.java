package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.command.*;
import sim.bot.parse.Command;

import java.util.ArrayList;


public class DiscordServer {

    private final SimPlayer player;
    private ServerVoiceChannel voice_channel;

    public DiscordServer(DiscordApi api, AudioPlayerManager manager) {
        this.player = new SimPlayer(api, manager);
    }

    public void process_message(MessageCreateEvent mce) {
        String message = mce.getMessageContent();
        if (!message.startsWith("-"))
            return ;

        /* Parse command */
        Command command = Command.parse_command(message);

        /* Must be in a voice channel */
        if (!author_is_connected_to_vc(mce)) {
            mce.getChannel().sendMessage("Join a voice channel");
            return ;
        }

        ArrayList<String> args = command.get_args();

        /* Execute */
        switch (command.get_type()) {
            case PLAY:
            /* FALLTHROUGH */
            case QUEUE:
                if (!player.is_initialised() && author_is_connected_to_vc(mce))
                    player.initialise(mce.getMessageAuthor().getConnectedVoiceChannel().get());
                (new PlayCmd()).execute(player, mce, args);
                break;
            case STOP:
                (new StopCmd()).execute(player, mce, args);
                break;
            case PAUSE:
            /* FALLTHROUGH */
            case RESUME:
                (new PauseCmd()).execute(player, mce, args);
                break;
            case SKIP:
                (new SkipCmd()).execute(player, mce, args);
                break;
            case QUEUELIST:
            /* FALLTHROUGH */
            case NOWPLAYING:
                (new QueueListCmd()).execute(player, mce, args);
                break;
            case FASTFORWARD:
                (new FastForwardCmd()).execute(player, mce, args);
                break;
            case REWIND:
                (new RewindCmd()).execute(player, mce, args);
                break;
            case UNKNOWN:
            /* FALLTHROUGH */
            default:
        }

    }

    private boolean author_is_connected_to_vc(MessageCreateEvent mce) {
        return mce.getMessageAuthor().getConnectedVoiceChannel().isPresent();
    }


}
