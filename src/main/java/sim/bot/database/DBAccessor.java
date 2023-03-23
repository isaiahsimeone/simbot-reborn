package sim.bot.database;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.javacord.api.event.message.MessageCreateEvent;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * The DBAccessor class acts as the bots interface to the MySQL database.
 */
public class DBAccessor {
    /**
     * Tracks the MySQL database connection
     */
    private Connection connection = null;

    /**
     * Constructs a DBAccessor object to allow the bot to interface with the MySQL database
     * @throws Exception - Any exception that occurred when attempting to connect with the
     * MySQL database
     */
    public DBAccessor() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        /* Initialise database connection */
        String user = System.getenv("simbotdbuser");
        String pass = System.getenv("simbotdbpass");
        String tabl = System.getenv("simbotdbtable");

        if (user == null || pass == null || tabl == null)
            throw new Exception("One of user, pass or table are null");

        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + tabl, user, pass);
        if (connection == null)
            throw new Exception("A connection could not be made to the MySQL service with the specified parameters");

        /* DB Connection is ready */
    }

    /**
     * Record the issuance of the play command
     * @param track - The AudioTrack object which the issuance of the play command loaded
     * @param mce - The message which contained the play command
     * @return - True if recording to the database was successful, false otherwise
     */
    public boolean insert_play_event(AudioTrack track, MessageCreateEvent mce) {
        try {
            String sql = "INSERT INTO play_history (media_id, user_id, server_id, time_played) VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, track.getIdentifier());
            stmt.setLong(2, mce.getMessage().getUserAuthor().get().getId());
            stmt.setLong(3, mce.getMessage().getServer().get().getId());
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log play " + e);
            return false;
        }

        return true;
    }

    /**
     * Given a queue of AudioTrack objects, save the AudioTrack identifiers (YouTube URIs) in
     * the MySQL database for later retrieval.
     * @param tracks - The tracks to be saved in the database
     * @param mce - The message event which requested tracks to be saved
     * @return - True if recording to the database was successful, false otherwise
     */
    public boolean update_or_create_restore_point(Queue<AudioTrack> tracks, MessageCreateEvent mce) {
        try {
            String sql = "INSERT INTO restore_point (server_id, video_ids) VALUES (?, ?) ON DUPLICATE KEY UPDATE video_ids = ?";
            String video_ids = "";
            for (AudioTrack track : tracks)
                video_ids += track.getIdentifier() + ",";

            // Remove last comma
            if (tracks.size() != 0)
                video_ids = video_ids.substring(0, video_ids.length() - 1);

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, mce.getMessage().getServer().get().getId());
            stmt.setString(2, video_ids);
            stmt.setString(3, video_ids);

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Failed to log restore point " + e);
            return false;
        }

        return true;
    }

    /**
     * Return a list of strings specifying which YouTube video IDs that were saved
     * when the was last disconnected
     * @param mce - The message event which issued the request for restore point retrieval
     * @return - An arraylist of strings containing YouTube video IDs or null if no record exists
     */
    public ArrayList<String> get_restore_point(MessageCreateEvent mce) {
        try {
            String sql = "SELECT video_ids FROM restore_point WHERE server_id = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, mce.getMessage().getServer().get().getId());

            ResultSet result = stmt.executeQuery();

            List<String> video_ids = Arrays.asList(result.getString(1).split(","));

            return new ArrayList<>(video_ids);
        } catch (SQLException e) {
            System.err.println("Failed to restore " + e);
        }
        return null;
    }

    /**
     * TODO: implement
     * Record the issuance of a command in the MySQL database
     * @return true if the command was recorded successfully, false otherwise
     */
    public boolean insert_command_event() {
        return true;
    }
}
