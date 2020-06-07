package astronomia.core.commands.fun;

import astronomia.utils.MessageHelper;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Ping extends Command {

    public Ping() {
        super.name = "ping";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply("PONG your head ah! " + MessageHelper.mention(commandEvent.getAuthor()));
    }
}
