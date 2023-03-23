package sim.bot.parse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public enum CommandType {
    PLAY(0, "Play", "Play the song specified by name or URL"),
    STOP(1, "Stop", "Disconnect the bot from the voice channel"),
    PAUSE(2, "Pause", "If Simbot is currently playing audio, that playback will be paused. If Simbot is already paused, audio playback will be resumed"),
    RESUME(3, "Resume", "Functions the same as the pause command"),
    QUEUE(4, "Queue", "Queue a song specified by name or URL to be played next. If no song is currently playing, this command functions in the same manner as the play command"),
    SKIP(5, "Skip", "Skip the currently playing song"),
    QUEUELIST(6, "QueueList", "Print out a list of the songs currently queued for playback"),
    FASTFORWARD(7, "FastForward", "Seek forward in the currently playing song. The default seek value is 5 seconds. The command takes one optional argument: the number of seconds to seek by"),
    REWIND(8, "Rewind", "Seek backward in the currently playing song. The default seek value is 5 seconds. The command takes one optional argument: the number of seconds to seek by"),
    NOWPLAYING(9, "NowPlaying", "Functions the same as the queue list command, prints out what song is currently playing and what songs are scheduled for playback next"),
    MASTER(10, "MasterMode", "In master mode, Simbot will only listen to Sim..."),
    DEBUG(11, "Debug", "In debug mode, Simbot will print out verbose information for debugging/troubleshooting"),
    KEYSPEAK(12, "KeySpeak", "Generates real quotes that Keyanuish has absolutely said at some point"),
    SHUFFLE(13, "Shuffle", "Shuffle all songs in the queue"),
    HELP(14, "Help", "Display a list of commands that Simbot knows, or list more information about a specified command"),
    DUMPQUEUE(15, "DumpQueue", "Remove all songs from the queue. Does not remove any currently playing song"),
    RESTORE(16, "Restore", "If the bot was disconnected with a non-empty queue (deliberately), that queue will be restored"),
    UNKNOWN(-1, "Unknown", "Unknown Command");

    /**
     * The ID of the command
     */
    private final int id;

    /**
     * The human-readable name of the command
     */
    private final String name;

    /**
     * A description of what the command does
     */
    private final String description;

    /**
     * Constructs a new CommandType object
     * @param id - The ID of the command
     * @param name - The human-readable name of the command
     * @param description - A description of what the command does
     */
    CommandType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * A map which allows for the conversion of command names and their
     * aliases to be converted to a command identifier
     */
    static final HashMap<String, Integer> aliases = new HashMap<>() {{
            /* Play */
            put("play", PLAY.id);
            put("pla", PLAY.id);
            put("pl", PLAY.id);
            put("p", PLAY.id);
            /* Stop */
            put("stop", STOP.id);
            put("s", STOP.id);
            put("leave", STOP.id);
            put("exit", STOP.id);
            put("dc", STOP.id);
            put("x", STOP.id);
            put("shutup", STOP.id);
            put("fuckoff", STOP.id);
            /* Pause */
            put("pause", PAUSE.id);
            put("hold", PAUSE.id);
            put("wait", PAUSE.id);
            put("resume", PAUSE.id);
            put("res", PAUSE.id);
            /* Queue */
            put("q", QUEUE.id);
            put("queue", QUEUE.id);
            put("enqueue", QUEUE.id);
            /* Skip */
            put("skip", SKIP.id);
            put("next", SKIP.id);
            put("n", SKIP.id);
            put("thissongshit", SKIP.id);
            put("nextsong", SKIP.id);
            /* Now playing */
            put("queuelist", NOWPLAYING.id);
            put("ql", NOWPLAYING.id);
            put("qlist", NOWPLAYING.id);
            put("list", NOWPLAYING.id);
            /* Fast foward */
            put("fastforward", FASTFORWARD.id);
            put("ff", FASTFORWARD.id);
            /* Rewind */
            put("rewind", REWIND.id);
            put("rw", REWIND.id);
            /* Nowplaying */
            put("nowplaying", NOWPLAYING.id);
            put("whatsong", NOWPLAYING.id);
            put("currentsong", NOWPLAYING.id);
            put("whatthis", NOWPLAYING.id);
            /* Master mode */
            put("iamyourmaster", MASTER.id);
            put("iaym", MASTER.id);
            put("mastermode", MASTER.id);
            put("master", MASTER.id);
            /* Debugger */
            put("debug", DEBUG.id);
            put("sdb", DEBUG.id);
            /* Keyspeak */
            put("keyspeak", KEYSPEAK.id);
            put("keysaying", KEYSPEAK.id);
            put("keyemulate", KEYSPEAK.id);
            put("keysimulator", KEYSPEAK.id);
            put("whatissomethingkeywouldsay", KEYSPEAK.id);
            /* Shuffle */
            put("shuffle", SHUFFLE.id);
            put("randomise", SHUFFLE.id);
            put("randomize", SHUFFLE.id);
            put("shuff", SHUFFLE.id);
            put("mix", SHUFFLE.id);
            put("shufflequeue", SHUFFLE.id);
            put("shuffleq", SHUFFLE.id);
            /* Dump queue */
            put("dump", DUMPQUEUE.id);
            put("purge", DUMPQUEUE.id);
            put("dumpqueue", DUMPQUEUE.id);
            put("dumpq", DUMPQUEUE.id);
            put("dq", DUMPQUEUE.id);
            /* Help */
            put("help", HELP.id);
            put("?", HELP.id);
            put("helpme", HELP.id);
            /* Restore */
            put("restore", RESTORE.id);
            put("rstr", RESTORE.id);
            put("revert", RESTORE.id);
    }};

    /**
     * If a supplied string is found to be an alias of a known bot
     * command, its representative (or leader) name will be returned
     * e.g. get_normalised_name("ql") returns "QueueList"
     * @param command - The string containing the command in question
     * @return - The normalised command name, or "Unknown" if the alias is unknown
     */
     public static String get_normalised_name(String command) {
        // lookup in map
        int id = aliases.get(command);

        for (CommandType ct : values())
            if (id == ct.id)
                return ct.name;

        return "Unknown";
    }

    /**
     * Return the description of the specified command, or unknown
     * if the supplied string is not a known alias
     * @param command - The string potentially containing a known command
     * @return The description of the command, or "Unknown" if the supplied
     *          string is not an alias
     */
    public static String get_description(String command) {
        // lookup in map
        int id = aliases.get(command);

        for (CommandType ct : values())
            if (id == ct.id)
                return ct.description;

        return "Unknown";
    }

    /**
     * Performs the same task as get_description(), but uses an
     * already normalised name to perform a lookup
     * @param normalised - The normalised command string to fetch the description of
     * @return - The description of the command, or "Unknown" if it doesn't exist
     * (which can not happen)
     */
    public static String get_description_from_normalised(String normalised) {
         for (CommandType ct : values())
             if (Objects.equals(ct.name, normalised))
                 return ct.description;
         return "Unknown";
    }

    /**
     * Return an ArrayList of strings containing all the commands that the aliases
     * map was initialised with. The normalised name of each command is returned.
     * @return - A list of command names in their normalised form
     */
    public static ArrayList<String> get_all_commands() {
         ArrayList<String> commands = new ArrayList<>(values().length);

         for (CommandType ct : values())
             commands.add(ct.name);

         return commands;
    }

    /**
     * Return the type of the command, if the supplied string is a command
     * @param command - A string potentially representing a command
     * @return A CommandType object describing what type of command was contained
     * in the string
     */
    static CommandType get_command_type(String command) {
        String normalised = command.toLowerCase();

        if (aliases.containsKey(normalised))
            return CommandType.values()[aliases.get(normalised)];

        return UNKNOWN;
    }

    /**
     * Return all aliases for a given target string
     * @param target A string potentially containing a command
     * @return An ArrayList containing all aliases of the command specified by
     * the provided string. Or, null if the string is an unknown command
     */
    public static ArrayList<String> get_aliases(String target) {
        CommandType cmd = get_command_type(target);

        if (cmd == UNKNOWN)
            return null;

        // Get all the aliases for specified command
        ArrayList<String> relevant_aliases = new ArrayList<>(10);

        for (String alias : aliases.keySet())
            if (aliases.get(alias) == cmd.id)
                relevant_aliases.add(alias);

        return relevant_aliases;
    }
}
