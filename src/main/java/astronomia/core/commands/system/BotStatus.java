package astronomia.core.commands.system;

import astronomia.core.commands.AbstractCommand;
import astronomia.utils.CommonUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static astronomia.constant.ApplicationConstants.*;

@Component
public class BotStatus extends AbstractCommand {

    private static String COMMAND_KEYWORD = "botstatus";
    private static String COMMAND_DESCRIPTION = "Change bot status";

    @Autowired
    private JDA jda;

    public BotStatus() {
        init(COMMAND_KEYWORD, COMMAND_DESCRIPTION);
        addArgs(OptionType.STRING, "activity", "Type of activity to show in status. E.g Playing <msg>");
        addArgs(OptionType.STRING, "param1", "Parameter 1");
        addArgs(OptionType.STRING, "param2", "Parameter 2", false);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.getName().equals(COMMAND_KEYWORD)) return;
        if (CommonUtils.checkCurrentUserRoleExistByRoleName(event.getUser(), ROLES_BOT_DEV)) {
            System.out.println(!event.getOptions().isEmpty());
            if (!event.getOptions().isEmpty()) {
                switch(event.getOptions().get(0).getAsString().toLowerCase()) {
                    case "playing":
                        if (event.getOptions().size() >= 2)
                            jda.getPresence().setActivity(Activity.playing(event.getOptions().get(1).getAsString()));
                        event.reply(BOT_STATUS_CHANGE_SUCCESSFUL).setEphemeral(true).queue();
                        break;
                    case "streaming":
                        if (event.getOptions().size() >= 3) {
                            jda.getPresence().setActivity(
                                    Activity.streaming(
                                            event.getOptions().get(1).getAsString(),
                                            event.getOptions().get(2).getAsString()));
                        } else {
                            event.reply("Usage: /" + COMMAND_KEYWORD + " streaming <name> <url>").setEphemeral(true).queue();
                        }
                        event.reply(BOT_STATUS_CHANGE_SUCCESSFUL).setEphemeral(true).queue();
                        break;
                    case "listening":
                        if (event.getOptions().size() >= 2)
                            jda.getPresence().setActivity(Activity.listening(event.getOptions().get(1).getAsString()));
                        event.reply(BOT_STATUS_CHANGE_SUCCESSFUL).setEphemeral(true).queue();
                        break;
                    case "watching":
                        if (event.getOptions().size() >= 2)
                            jda.getPresence().setActivity(Activity.watching(event.getOptions().get(1).getAsString()));
                        event.reply(BOT_STATUS_CHANGE_SUCCESSFUL).setEphemeral(true).queue();
                        break;
                    case "competing":
                        if (event.getOptions().size() >= 2)
                            jda.getPresence().setActivity(Activity.competing(event.getOptions().get(1).getAsString()));
                        event.reply(BOT_STATUS_CHANGE_SUCCESSFUL).setEphemeral(true).queue();
                        break;
                    default:
                        event.reply("Invalid activity type. Only accept the follow activity.\n" +
                                "playing\n" +
                                "streaming\n" +
                                "listening\n" +
                                "watching\n" +
                                "competing\n").setEphemeral(true).queue();
                }
            }
        } else {
            event.reply(BOT_MESSAGE_NO_PERMISSION).setEphemeral(true).queue();
        }
    }
}
