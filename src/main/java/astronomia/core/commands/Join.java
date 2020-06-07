package astronomia.core.commands;

import astronomia.modules.Accessibility;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Join extends Command {

    public Join() {
        super.name = "join";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        Accessibility.join(commandEvent.getGuild(), commandEvent.getTextChannel(), commandEvent.getMember());
    }
}
