package astronomia.utils;

import astronomia.models.Command;
import net.dv8tion.jda.api.entities.User;

import java.util.Optional;

public class MessageHelper {

    public static Command extractCommand(String msg) {
        String[] msgArr = msg.trim().split(" ", 2);

        return Command.builder()
                .command(msgArr[0])
                .message(msgArr.length > 1 ? Optional.ofNullable(msgArr[1]).orElse(null) : null).build();
    }

    public static String mention(User user) {
        return String.format("<@%s>", user.getId());
    }
}
