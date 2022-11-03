package sim.bot.parse;

import java.util.HashMap;

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
    DEBUG(11),
    UNKNOWN(-1);

    private final int id;

    CommandType(int id) {
        this.id = id;
    }

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
            /* Debugger */
            put("debug", DEBUG.id);
            put("sdb", DEBUG.id);
    }};

    static CommandType get_command_type(String command) {
        String normalised = command.toLowerCase();

        if (aliases.containsKey(normalised))
            return CommandType.values()[aliases.get(command)];

        return UNKNOWN;
    }
}
