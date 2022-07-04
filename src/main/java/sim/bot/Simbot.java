package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;

import org.javacord.api.*;
import org.javacord.api.audio.AudioSource;
import sim.bot.audio.TrackScheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Simbot {

    /*
     * Bot launch
     */
    public static void main(String[] args) {
        //String api_token = System.getenv("simbottoken");

        System.out.println(api_token);
        DiscordApi d_api = new DiscordApiBuilder().setToken(api_token).login().join();

        AudioPlayerManager player_manager = new DefaultAudioPlayerManager();
        register_sources(player_manager);

        HashMap<Integer, DiscordServer> server_map = new HashMap<>();

        d_api.addMessageCreateListener(message -> {
            int server_uuid = message.getServer().hashCode();

            /* We have not encountered this discord server in the current session. Map the server */
            server_map.putIfAbsent(server_uuid, new DiscordServer(d_api, player_manager));

            /* Pass message to discord server handler */
            server_map.get(server_uuid).process_message(message);
        });
    }

    private static void register_sources(AudioPlayerManager manager) {
        manager.registerSourceManager(new YoutubeAudioSourceManager());
        manager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }
}