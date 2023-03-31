package sim.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.command.*;
import sim.bot.database.DBAccessor;
import sim.bot.parse.Command;
import sim.bot.util.Emoji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import static sim.bot.parse.CommandType.*;


public class DiscordServer {
    /**
     * The DiscordServerManager object by which the bot
     * interacts and actions commands upon the Discord server
     */
    private final DiscordServerManager serverManager;
    /**
     * The Discord server
     */
    private Server server;
    /**
     * The unique Discord identifier of Simbot
     */
    private final long SIMBOT_ID = 881509848838180884L;

    /**
     * Executed when the bot is deemed inactive, the bot will be disconnected from the respective
     * discord servers voice channel and the Player object will be destroyed
     */
    class InactivityTimeout extends TimerTask {
        @Override
        public void run() {
            if (serverManager.is_initialised() && serverManager.is_inactive()) {
                serverManager.write_verbose_message("I am inactive. Goodbye");
                serverManager.destroy();
            }
        }
    }

    /**
     * For each discord server that the bot is a member in, the DiscordServer class provides
     * an interface to that discord server. All incoming text messages destined to that discord server
     * are passed through this class and actioned appropriately
     * @param mce - A discord server text message sent in the respective discord server
     * @param api - Simbot's DiscordAPI object
     * @param accessor - An accessor to a MySQL database allowing for record keeping
     */
    public DiscordServer(MessageCreateEvent mce, DiscordApi api, DBAccessor accessor) {
        this.server = mce.getServer().get();
        this.serverManager = new DiscordServerManager(api, this.server, accessor);

        /*
         * Javacord has a bug where if the bot is kicked/moved, it will break audio playback.
         * This listens and determines whether the bot has been moved/kicked/etc
         */
        api.addServerVoiceChannelMemberLeaveListener(event -> {
            /* Ignore if it's for a different server */
            if (event.getServer() != server)
                return ;

            /*
             * Disregard if someone other than the bot is leaving.
             */
            if (event.getUser().getId() != SIMBOT_ID)
                return ;

            serverManager.write_verbose_message("Someone just moved me or they called stop. So, I am disconnecting");
            serverManager.destroy();
        });

        /* Inactivity timer */
        Timer timer = new Timer();
        TimerTask task = new InactivityTimeout();
        // Check every 45 minutes
        timer.schedule(task, 0, 1000 * 60 * 45);
    }

    /**
     * Process a Discord server text message potentially containing a bot command.
     * @param mce - The discord server text message potentially containing a bot command
     */
    public void process_message(MessageCreateEvent mce) {
        String message = mce.getMessageContent();

        if (!message.startsWith("-"))
            return ;

        /* Parse command */
        Command command = Command.parse_command(message);

        /* Must be in a voice channel if the command requires so */
        if (!author_is_connected_to_vc(mce) && command_requires_voice_connection(command)) {
            mce.getChannel().sendMessage("Join a voice channel");
            return ;
        }

        /* Don't join AFK voice channel */
        if (command_requires_voice_connection(command) && server.getAfkChannel().isPresent()
                && mce.getMessageAuthor().getConnectedVoiceChannel().get() == server.getAfkChannel().get()) {
            mce.getChannel().sendMessage("I will not join AFK...");
            return ;
        }

        /* Check master mode */
        if (serverManager.in_master_mode() && !mce.getMessageAuthor().isBotOwner()) {
            mce.getMessage().reply("I serve only Sim");
            return ;
        }

        ArrayList<String> args = command.get_args();

        serverManager.write_verbose_message(mce.getMessageAuthor().getDisplayName() + " issued a command of type "
                + command.get_type());

        /* Execute */
        switch (command.get_type()) {
            case PLAY:
            /* FALLTHROUGH */
            case QUEUE:
                if (!serverManager.is_initialised() && author_is_connected_to_vc(mce))
                    serverManager.initialise(mce.getMessageAuthor().getConnectedVoiceChannel().get());
                (new PlayCmd()).execute(serverManager, mce, args);
                break;
            case STOP:
                (new StopCmd()).execute(serverManager, mce, args);
                break;
            case PAUSE:
            /* FALLTHROUGH */
            case RESUME:
                (new PauseCmd()).execute(serverManager, mce, args);
                break;
            case SKIP:
                (new SkipCmd()).execute(serverManager, mce, args);
                break;
            case QUEUELIST:
            /* FALLTHROUGH */
            case NOWPLAYING:
                (new QueueListCmd()).execute(serverManager, mce, args);
                break;
            case FASTFORWARD:
                (new FastForwardCmd()).execute(serverManager, mce, args);
                break;
            case REWIND:
                (new RewindCmd()).execute(serverManager, mce, args);
                break;
            case MASTER:
                (new MasterCmd()).execute(serverManager, mce, args);
                break;
            case DEBUG:
                (new DebugCmd()).execute(serverManager, mce, args);
                break;
            case KEYSPEAK:
                (new KeySpeakCmd()).execute(serverManager, mce, args);
                break;
            case DUMPQUEUE:
                (new DumpQueueCmd()).execute(serverManager, mce, args);
                break;
            case HELP:
                (new HelpCmd()).execute(serverManager, mce, args);
                break;
            case SHUFFLE:
                (new ShuffleCmd()).execute(serverManager, mce, args);
                break;
            case RESTORE:
                (new RestoreCmd()).execute(serverManager, mce, args);
                break;
            case UNKNOWN:
            /* FALLTHROUGH */
            default:
                mce.getMessage().addReaction(Emoji.GREY_QUESTION.getCharCode());
        }
    }

    /**
     * Determine whether the specified command can be used without first connecting to a voice channel
     * @param command - The command to check
     * @return True if the command can be used without the issuer being connected to a voice channel
     */
    private boolean command_requires_voice_connection(Command command) {
        return !Arrays.asList(HELP, KEYSPEAK, DEBUG, MASTER, QUEUELIST, NOWPLAYING, UNKNOWN)
                .contains(command.get_type());
    }

    /**
     * Determines whether the user who sent a message is connected
     * to a voice channel
     */
    private boolean author_is_connected_to_vc(MessageCreateEvent mce) {
        return mce.getMessageAuthor().getConnectedVoiceChannel().isPresent();
    }
}
