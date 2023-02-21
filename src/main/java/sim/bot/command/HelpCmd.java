package sim.bot.command;

import org.javacord.api.event.message.MessageCreateEvent;
import sim.bot.DiscordServerManager;
import sim.bot.parse.CommandType;

import java.util.ArrayList;

public class HelpCmd implements Executable {
    @Override
    public void execute(DiscordServerManager manager, MessageCreateEvent mce, ArrayList<String> args) {
        StringBuilder printout = new StringBuilder();

        if (args.size() == 0) {
            printout.append("For more granular help information, use -help [command]\n\n");
            ArrayList<String> commands = CommandType.get_all_commands();

            for (String command : commands) {
                printout.append("__**").append(command).append("**__\n");
                printout.append("\t\t\t").append(CommandType.get_description_from_normalised(command)).append("\n");
            }

            mce.getMessage().reply(printout.toString());
            return ;
        }
        String target = args.get(0);
        ArrayList<String> aliases = CommandType.get_aliases(target);

        if (aliases == null) {
            mce.getMessage().reply("I don't know this command");
            return ;
        }

        printout = new StringBuilder("**Command:** " + CommandType.get_normalised_name(target) + "\n" +
                "**Description:** " + CommandType.get_description(target) + "\n" +
                "**Aliases:** " + CommandType.get_aliases(target));

        mce.getMessage().reply(printout.toString());
    }
}
