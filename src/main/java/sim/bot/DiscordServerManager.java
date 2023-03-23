package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import sim.bot.audio.TrackScheduler;
import sim.bot.database.DBAccessor;

import java.sql.Connection;

public class DiscordServerManager {
    private final AudioPlayerManager manager;
    private final AudioPlayer player;
    private final AudioSource source;
    private AudioConnection audio_connection;
    private final TrackScheduler sched;
    private ServerVoiceChannel voice_channel;
    private Server server;
    private TextChannel server_text_channel;
    private boolean is_init;
    private boolean in_master_mode;
    private boolean debug_on;
    private Message debugging_output_field;
    private String debug_content;
    private DBAccessor accessor;

    public DiscordServerManager(DiscordApi api, Server server, DBAccessor accessor) {
        this.server = server;
        this.manager = new DefaultAudioPlayerManager();
        this.manager.registerSourceManager(new YoutubeAudioSourceManager());
        this.manager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());

        this.player = manager.createPlayer();
        this.accessor = accessor;
        this.source = new sim.bot.audio.AudioSource(api, player);
        this.sched = new TrackScheduler(player);
        player.addListener(sched);
        this.is_init = false;
        this.voice_channel = null;
        this.in_master_mode = false;
        this.debug_on = false;
        this.server_text_channel = null;
        this.debugging_output_field = null;
        this.debug_content = "";

        if (server.getTextChannels().isEmpty())
            System.err.println("Server " + server.getName() + " Has no text channels. server_text_channel is null");
        else
            this.server_text_channel = server.getTextChannels().get(0);

    }

    /**
     * Determines whether the bot has been initialised for voice connection
     * @return whether the bot has been initialised
     */
    public boolean is_initialised() {
        return is_init;
    }

    /**
     * Prepares and connects the bot to the specified Discord server voice channel
     * @param vc - The voice channel to connect to
     * @return A boolean indicating whether initialisation was successful
     */
    public boolean initialise(ServerVoiceChannel vc) {
        write_verbose_message("Attempting to connect to VC: " + vc.getName() + " (" + vc.getBitrate()
                + ") with occupants " + vc.getConnectedUsers());
        this.voice_channel = vc;
        vc.connect().thenAccept(audio_connection -> {
           audio_connection.setAudioSource(source);
           this.audio_connection = audio_connection;
           is_init = true;
        }).exceptionally(e -> {
            /* Failed to connect to voice channel */
            write_verbose_message("Failed to connect to voice channel. Permissions?");
            return null;
        });
        return is_init;
    }

    /**
     * Destroys the bot, if it is connected to a voice channel, it will be disconnected,
     * if there are songs in the queue, they will be dumped
     */
    public void destroy() {
        sched.dump_queue();
        sched.next_track();
        player.setPaused(false);

        is_init = false;
        if (voice_channel != null)
            voice_channel.disconnect();
        audio_connection.close();
        write_verbose_message("Player has been destroyed.");
    }

    /**
     * Return the AudioPlayer object for this discord server
     * @return The AudioPlayer object for this discord server
     */
    public AudioPlayer get_player() {
        return player;
    }

    /**
     * Return the AudioPlayerManager for this discord server
     * @return The AudioPlayerManager for this discord server
     */
    public AudioPlayerManager get_audio_manager() {
        return manager;
    }

    /**
     * Return the TrackScheduler for this discord server
     * @return The TrackScheduler for this discord server
     */
    public TrackScheduler get_track_scheduler() {
        return sched;
    }

    /**
     * Return the Database accessor object for this discord server
     * @return The Database accessor object for this discord server
     */
    public DBAccessor get_db_accessor() {
        return accessor;
    }

    /**
     * Enable master mode if it is disabled, disable otherwise
     */
    public void toggle_master_mode() {
        in_master_mode = !in_master_mode;
    }

    /**
     * Determine whether the bot is operating in master mode
     * @return A boolean indicating whether master mode is enabled or not
     */
    public boolean in_master_mode() {
        return in_master_mode;
    }

    /**
     * Determines whether the bot is being actively used for audio playback
     * @return A boolean indicating activity
     */
    public boolean is_inactive() {
        return get_track_scheduler().queue.size() == 0 && get_player().getPlayingTrack() == null;
    }

    /**
     * Determines whether the SQL Database connection accessor is available for use
     */
    public boolean has_db_accessor() {
        return accessor != null;
    }

    /**
     * Enable verbose debugging to the discord server text channel, disable it if it is
     * already enabled
     */
    public void toggle_debugging() {
        if (server_text_channel == null)
            System.err.println("Unable to enable debugging. No text channel present");
        /* Debugging on/off*/
        if (!this.debug_on) {
            debug_content = "```css\n[********* SIMBOT DEBUGGER *********]```";
            debugging_output_field = server_text_channel.sendMessage(debug_content).join();
        } else {
            debugging_output_field.delete();
            debug_content = "";
        }
        this.debug_on = !this.debug_on;
    }

    /**
     * Write the corresponding message to the discord text channel debugging frame (if in master mode)
     * and always write to the local bot log
     * @param message
     */
    public void write_verbose_message(String message) {
        if (debug_on && server_text_channel != null) {
            // Remove last 3 backticks
            debug_content = debug_content.substring(0, debug_content.length() - 3);
            debug_content += "\n(" + server.getName() + ") " + message + "```";
            debugging_output_field.edit(debug_content);
        }
        System.err.println(message);
    }
}
