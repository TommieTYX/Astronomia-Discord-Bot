package astronomia.core;

import astronomia.core.commands.Join;
import astronomia.core.commands.Leave;
import astronomia.core.commands.fun.Ping;
import astronomia.core.commands.fun.darylJoker;
import astronomia.core.commands.musicplayer.*;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;

import static astronomia.constant.ApplicationConstants.DEFAULT_COMMAND_PREFIX;

public class CommandListener {

    public CommandClient build() {
        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setPrefix(DEFAULT_COMMAND_PREFIX);
        commandClientBuilder.setHelpWord("help");
        commandClientBuilder.setOwnerId("223395701197897728");

        loadCommands(commandClientBuilder);

        return commandClientBuilder.build();
    }

    private void loadCommands(CommandClientBuilder commandClientBuilder) {
        commandClientBuilder.addCommands(
                new darylJoker(), new Ping(), new Join(), new Leave(),
                new Play(), new Pause(), new Queue(), new Resume(), new Push(), new Skip(), new Volume()
        );
    }
}
