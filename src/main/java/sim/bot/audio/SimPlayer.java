package sim.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;

public class SimPlayer {
    private final AudioPlayerManager manager;
    private final AudioPlayer player;
    private final AudioSource source;
    private AudioConnection audio_connection;
    private final TrackScheduler sched;
    private ServerVoiceChannel voice_channel;
    private boolean is_init;
    private boolean in_master_mode;
    private int verbosity;

    public SimPlayer(DiscordApi api, AudioPlayerManager manager) {
        this.manager = manager;
        this.player = manager.createPlayer();
        this.source = new sim.bot.audio.AudioSource(api, player);
        this.sched = new TrackScheduler(player);
        player.addListener(sched);
        this.is_init = false;
        this.voice_channel = null;
        this.in_master_mode = false;
        this.verbosity = 0;
    }

    public boolean is_initialised() {
        return is_init;
    }

    public boolean initialise(ServerVoiceChannel vc) {
        this.voice_channel = vc;
        vc.connect().thenAccept(audio_connection -> {
           audio_connection.setAudioSource(source);
           this.audio_connection = audio_connection;
           is_init = true;
        }).exceptionally(e -> {
            /* Failed to connect to voice channel */
            System.err.println("Failed to connect to a voice channel");
            return null;
        });
        return is_init;
    }

    public void destroy() {
        is_init = false;
        voice_channel.disconnect();
        audio_connection.close();
    }

    public AudioPlayer get_player() {
        return player;
    }

    public AudioPlayerManager get_audio_manager() {
        return manager;
    }

    public TrackScheduler get_track_scheduler() {
        return sched;
    }

    public void toggle_master_mode() {
        in_master_mode = !in_master_mode;
    }

    public boolean in_master_mode() {
        return in_master_mode;
    }

    public int get_verbosity() {
        return verbosity;
    }

    public void set_verbosity(int verbosity) {
        this.verbosity = verbosity;
    }

}
