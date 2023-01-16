package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.audio.SimPlayer;
import sim.bot.command.*;
import sim.bot.parse.Command;
import sim.bot.util.Emoji;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DiscordServer {

    private final SimPlayer player;
    private Server server;
    private final long SIMBOT_ID = 881509848838180884L;

    class InactivityTimeout extends TimerTask {
        @Override
        public void run() {
            if (player.is_initialised() && player.is_inactive()) {
                player.write_verbose_message("I am inactive. Goodbye");
                player.get_track_scheduler().dump_queue();
                player.get_track_scheduler().next_track();
                player.get_player().setPaused(false);
                player.destroy();
            }
        }
    }

    public DiscordServer(MessageCreateEvent mce, DiscordApi api) {
        this.server = mce.getServer().get();
        AudioPlayerManager player_manager = new DefaultAudioPlayerManager();
        register_sources(player_manager);
        this.player = new SimPlayer(api, player_manager, this.server);

        /*
         * Javacord has a bug where if the bot is kicked/moved, it will break audio playback.
         * This listens and determines whether the bot has been moved/kicked/etc
         */
        api.addServerVoiceChannelMemberLeaveListener(event -> {
            /* Ignore if it's for a different server */
            if (event.getServer() != server)
                return ;

            player.write_verbose_message(event.getUser() + " left a voice channel");

            /*
             * Disregard if someone other than the bot is leaving.
             */
            if (event.getUser().getId() != SIMBOT_ID)
                return ;

            player.write_verbose_message("Someone just moved me or they called stop. So, I am destroying myself");
            player.get_track_scheduler().dump_queue();
            player.get_track_scheduler().next_track();
            player.get_player().setPaused(false);
            player.destroy();
        });

        /* Inactivity timer */
        Timer timer = new Timer();
        TimerTask task = new InactivityTimeout();
        // Check every 45 minutes
        timer.schedule(task, 0, 1000 * 60 * 45);

    }

    /*
     * Process the command (if one is specified) in the message event
     */
    public void process_message(MessageCreateEvent mce) {
        String message = mce.getMessageContent();

        /* React if someone mentions Keyanuish */
        if (message.contains("<@70762467227078656>"))
            mce.getMessage().addReaction(Emoji.UNAMUSED.get_char_code());

        if (!message.startsWith("-"))
            return ;

        /* Parse command */
        Command command = Command.parse_command(message);


        /* Must be in a voice channel */
        if (!author_is_connected_to_vc(mce)) {
            mce.getChannel().sendMessage("Join a voice channel");
            return ;
        }

        /* Don't join AFK voice channel */
        if (server.getAfkChannel().isPresent() && mce.getMessageAuthor().getConnectedVoiceChannel().get()
                == server.getAfkChannel().get()) {
            mce.getChannel().sendMessage("I will not join AFK...");
            return ;
        }

        /* Check master mode */
        if (player.in_master_mode() && !mce.getMessageAuthor().isBotOwner()) {
            mce.getMessage().reply("I serve only Sim");
            return ;
        }

        ArrayList<String> args = command.get_args();

        player.write_verbose_message(mce.getMessageAuthor().getDisplayName() + " issued a command of type "
                + command.get_type());
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
            case DEBUG:
                (new DebugCmd()).execute(player, mce, args);
                break;
            case KEYSPEAK:
                (new KeySpeakCmd()).execute(player, mce, args);
                break;
            case UNKNOWN:
            /* FALLTHROUGH */
            default:
                mce.getMessage().addReaction(Emoji.GREY_QUESTION.get_char_code());
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
     * Register soundcloud and YouTube as audio sources
     */
    private static void register_sources(AudioPlayerManager manager) {
        manager.registerSourceManager(new YoutubeAudioSourceManager());
        manager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }

}
