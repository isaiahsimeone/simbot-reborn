package sim.bot.parse;

public enum CommandType {
    // Ordering important
    PLAY(0),
    STOP(1),
    PAUSE(2),
    RESUME(3),
    QUEUE(4),
    SKIP(5),
    QUEUELIST(6),
    FASTFORWARD(7),
    REWIND(8),
    NOWPLAYING(9),
    MASTER(10),
    VERBOSE(11),
    UNKNOWN(-1);

    private int id;

    CommandType(int id) {
        this.id = id;
    }

    private static final String[][] ALIASES = {
            {"play", "pla", "pl", "p"}, // Play
            {"stop", "s", "leave", "exit", "dc", "x", "shutup", "fuckoff"}, // Stop
            {"pause", "hold", "wait"}, // Pause
            {"resume", "res"}, // Resume
            {"q", "queue", "enqueue"}, // Queue
            {"skip", "next", "n", "thissongshit", "nextsong"}, // Skip
            {"queuelist", "ql", "qlist", "list"}, // List queue
            {"fastforward", "ff"}, // Fast forward
            {"rewind", "rw"}, // Rewind
            {"nowplaying", "whatsong", "currentsong", "whatthis"}, // Get playing
            {"iamyourmaster", "iaym"},
            {"debug", "sdb", "deb"}
    };


    static CommandType get_command_type(String command) {
        String normalised = command.toLowerCase();

        for (int i = 0; i < ALIASES.length; i++) {
            for (int j = 0; j < ALIASES[i].length; j++) {
                if (normalised.equals(ALIASES[i][j]))
                    return CommandType.values()[i];
            }
        }
        return UNKNOWN;
    }

}
