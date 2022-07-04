package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.command.PlayCmd;
import sim.bot.parse.Command;


public class DiscordServer {

    private SimPlayer player;
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

        /* Execute */
        switch (command.get_type()) {
            case PLAY:
                if (!author_is_connected_to_vc(mce)) {
                    mce.getChannel().sendMessage("Join a voice channel");
                    return ;
                }
                if (!player.is_initialised())
                    player.initialise(mce.getMessageAuthor().getConnectedVoiceChannel().get());
                //PlayCmd.execute();
                break;
            case STOP:
                break;
            case PAUSE:
                break;
            case RESUME:
                break;
            case QUEUE:
                break;
            case SKIP:
                break;
            case QUEUELIST:
                break;
            case FASTFORWARD:
                break;
            case REWIND:
                break;
            case NOWPLAYING:
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
