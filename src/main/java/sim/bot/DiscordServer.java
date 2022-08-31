package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.command.*;
import sim.bot.parse.Command;
import sim.bot.rust.RustManager;
import sim.bot.util.Emoji;

import java.util.ArrayList;


public class DiscordServer {

    private final SimPlayer player;
    private final RustManager rust_manager;
    private ServerVoiceChannel voice_channel;

    public DiscordServer(DiscordApi api) {
        AudioPlayerManager player_manager = new DefaultAudioPlayerManager();
        register_sources(player_manager);
        this.player = new SimPlayer(api, player_manager);

        this.rust_manager = new RustManager(api);
    }

    /*
     * Process the command (if one is specified) in the message event
     */
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

        /* Check master mode */
        if (player.in_master_mode() && !mce.getMessageAuthor().isBotOwner()) {
            mce.getMessage().reply("I serve only Sim");
            return ;
        }

        ArrayList<String> args = command.get_args();

        /* Someone trying to say simbot? */
        for (String s : args)
            if (s.toLowerCase().equals("simbot"))
                mce.getMessage().addReaction(Emoji.ANGRY.get_char_code());

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
            case MASTER:
                (new MasterCmd()).execute(player, mce, args);
                break;
            case VERBOSE:
                (new VerbosityCmd()).execute(player, mce, args);
            case UNKNOWN:
            /* FALLTHROUGH */
            default:
        }

    }

    /*
     * Determines whether the user who sent the message is connected
     * to a voice channel
     */
    private boolean author_is_connected_to_vc(MessageCreateEvent mce) {
        return mce.getMessageAuthor().getConnectedVoiceChannel().isPresent();
    }

    /*
     * Register soundcloud and youtube as audio sources
     */
    private static void register_sources(AudioPlayerManager manager) {
        manager.registerSourceManager(new YoutubeAudioSourceManager());
        manager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }

}
