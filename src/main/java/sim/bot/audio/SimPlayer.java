package sim.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.javacord.api.DiscordApi;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.ServerVoiceChannel;

public class SimPlayer {
    private DiscordApi api;
    private AudioPlayerManager manager;
    private AudioPlayer player;
    private AudioSource source;
    private AudioConnection audio_connection;
    private TrackScheduler sched;
    private ServerVoiceChannel voice_channel;
    private boolean is_init;

    public SimPlayer(DiscordApi api, AudioPlayerManager manager) {
        this.api = api;
        this.manager = manager;
        this.player = manager.createPlayer();
        this.source = new sim.bot.audio.AudioSource(api, player);
        this.sched = new TrackScheduler(player);
        player.addListener(sched);
        this.is_init = false;
        this.voice_channel = null;
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


}
