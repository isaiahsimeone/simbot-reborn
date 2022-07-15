package sim.bot.parse;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static sim.bot.parse.CommandType.*;

public class Command {
    private final CommandType type;
    private final ArrayList<String> args;

    public Command(CommandType type, ArrayList<String> args) {
        this.type = type;
        this.args = args;
    }

    public CommandType get_type() {
        return type;
    }

    public ArrayList<String> get_args() {
        return args;
    }

    public static Command parse_command(String cmd) {
        ArrayList<String> args = new ArrayList<>(8);
        StringBuilder command_specifier = new StringBuilder();

        /* Replace multiple spaces with single space */
        String command = cmd.trim().replaceAll(" +", " ");

        /* Must start with '-' */
        if (!command.startsWith("-") && !command.startsWith(" -"))
            return new Command(UNKNOWN, args);

        /* Remove leading '-' and leading spaces */
        command = command.replaceFirst("[ ]?-[ ]?", "");

        CommandType type = get_command_type(command.split(" ")[0]);
        args.addAll(Arrays.asList(command.split(" ")));
        /* Remove command specifier from arg list */
        args.remove(0);

        System.out.println("type = " + type + ", args = " + args);

        return new Command(type, args);
    }
}
