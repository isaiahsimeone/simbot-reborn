package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import org.javacord.api.*;
import java.util.HashMap;

public class Simbot {

    /*
     * Bot launch
     */
    public static void main(String[] args) {
        System.out.println("Launching...");
        String api_token = System.getenv("simbottoken");
        if (api_token == null)
            System.err.println("No API token specified");

        DiscordApi d_api = new DiscordApiBuilder().setToken(api_token).login().join();

        AudioPlayerManager player_manager = new DefaultAudioPlayerManager();
        register_sources(player_manager);

        HashMap<Integer, DiscordServer> server_map = new HashMap<>();

        d_api.addMessageCreateListener(message -> {
            int server_uid = message.getServer().hashCode();

            /* We have not encountered this discord server in the current session. Map the server */
            server_map.putIfAbsent(server_uid, new DiscordServer(d_api, player_manager));

            /* Pass message to the discord server handler from which the message originated */
            server_map.get(server_uid).process_message(message);
        });
        System.out.println("Exiting...");
    }

    /*
     * Register soundcloud and youtube as audio sources
     */
    private static void register_sources(AudioPlayerManager manager) {
        manager.registerSourceManager(new YoutubeAudioSourceManager());
        manager.registerSourceManager(SoundCloudAudioSourceManager.createDefault());
    }
}