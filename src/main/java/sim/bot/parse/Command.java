package sim.bot.parse;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static sim.bot.parse.CommandType.*;

public class Command {
    private CommandType type;
    private ArrayList<String> args;

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

    public static Command parse_command(String command) {
        ArrayList<String> args = new ArrayList<>(8);

        if (command.charAt(0) != '-')
            return new Command(UNKNOWN, new ArrayList<>(0));

        args.addAll(Arrays.asList(command.split("\\s+"))); // Split at whitespace

        CommandType type = get_command_type(args.get(0).substring(1)); // Omit leading '-'

        // parse here
        return new Command(type, args);
    }
}
