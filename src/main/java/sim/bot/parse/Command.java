package sim.bot.parse;

import java.util.ArrayList;
import java.util.Arrays;

import static sim.bot.parse.CommandType.*;

public class Command {
    /**
     * The type of the command, e.g. PLAY, STOP, etc
     */
    private final CommandType type;
    /**
     * The arguments associated with the issuance of the command
     */
    private final ArrayList<String> args;

    /**
     * Constructs a command Object with the specified type and arguments
     * @param type
     * @param args
     */
    public Command(CommandType type, ArrayList<String> args) {
        this.type = type;
        this.args = args;
    }

    /**
     * Get the type of the command issued (e.g. PLAY, STOP, etc)
     * @return - The type of command issued
     */
    public CommandType get_type() {
        return type;
    }

    /**
     * The arguments that were passed upon invocation of the command
     * @return - The arguments supplied to the command as an ArrayList of strings
     */
    public ArrayList<String> get_args() {
        return args;
    }

    /**
     * Parse a string as a bot command. A new Command object will be created
     * containing the type of command and its arguments
     * @param cmd - The string containing a bot command
     * @return - A new Command object describing the command issued
     */
    public static Command parse_command(String cmd) {
        ArrayList<String> args = new ArrayList<>();

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
