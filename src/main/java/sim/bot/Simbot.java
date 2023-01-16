package sim.bot;

import org.javacord.api.*;
import org.javacord.api.entity.user.UserStatus;

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

        DiscordApi d_api = new DiscordApiBuilder().setToken(api_token).setAllIntents().login().join();

        // Hopefully Keyanuish will notice this...
        d_api.updateActivity("-keyspeak");

        System.out.println(d_api.getStatus());

        HashMap<Integer, DiscordServer> server_map = new HashMap<>();

        d_api.addMessageCreateListener(message -> {
            int server_uid = message.getServer().hashCode();

            /* We have not encountered this discord server in the current session. Map the server */
            server_map.putIfAbsent(server_uid, new DiscordServer(message, d_api));

            /* Pass message to the discord server handler from which the message originated */
            server_map.get(server_uid).process_message(message);
        });

        System.out.println("Simbot Listener Created...");
    }

}