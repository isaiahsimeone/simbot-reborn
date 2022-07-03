package sim.bot;

import com.sedmelluq.discord.lavaplayer.player.*;
import org.javacord.api.*;

public class Simbot {
    static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger("simbot-log");

    /*
     * Bot launch
     */
    public static void main(String[] args) {
        String api_token = System.getenv("simbot-token");
        DiscordApi d_api = new DiscordApiBuilder().setToken(api_token).login().join();




    }
}