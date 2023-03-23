package sim.bot;

import org.javacord.api.*;
import sim.bot.database.DBAccessor;

import java.util.HashMap;

/**
 * The Simbot class is the first class invoked on bot startup. All discord text-channel originate
 * from this class, and are delivered to the relevant DiscordServerManager.
 */
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

        System.out.println(d_api.getStatus());

        DBAccessor accessor = null;
        try {
            accessor = new DBAccessor();
        } catch (Exception e) {
            System.err.println("Unable to initiate a connection with the MySQL database.\n" + e + "\n\nContinuing");
        }
        DBAccessor finalAccessor = accessor;

        HashMap<Integer, DiscordServer> server_map = new HashMap<>();

        d_api.addMessageCreateListener(message -> {
            int server_uid = message.getServer().hashCode();

            /* We have not encountered this discord server in the current session. Map the server */
            server_map.putIfAbsent(server_uid, new DiscordServer(message, d_api, finalAccessor));

            /* Pass message to the discord server handler from which the message originated */
            server_map.get(server_uid).process_message(message);
        });

        System.out.println("Simbot Listener Created...");
    }

}