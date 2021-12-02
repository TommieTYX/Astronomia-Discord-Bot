package com.astronomia.utils;

import com.astronomia.models.UserCommand;
import net.dv8tion.jda.api.entities.User;

import java.util.Optional;

public class MessageHelper {

    public static UserCommand extractUserCommand(String msg) {
        String[] msgArr = msg.trim().split(" ", 2);

        return UserCommand.builder()
                .command(msgArr[0])
                .message(msgArr.length > 1 ? Optional.ofNullable(msgArr[1]).orElse(null) : null).build();
    }

    public static String mention(User user) {
        return String.format("<@%s>", user.getId());
    }

    public static String convertTextToURL(String currentText, String currentUrl){
        return String.format("[%s](%s)", currentText, currentUrl);
    }
}
