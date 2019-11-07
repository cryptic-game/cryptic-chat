package net.cryptic_game.microservice.chat.endpoint;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.chat.channel.Channel;
import net.cryptic_game.microservice.chat.channel.ChatAction;
import net.cryptic_game.microservice.endpoint.UserEndpoint;
import net.cryptic_game.microservice.utils.JSON;
import net.cryptic_game.microservice.wrapper.User;
import org.json.simple.JSONObject;

import java.util.Date;
import java.util.UUID;

import static net.cryptic_game.microservice.chat.endpoint.EndpointResponse.*;
import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;
import static net.cryptic_game.microservice.utils.JSONBuilder.simple;

public class MessageEndpoints {

    private MessageEndpoints() {
    }

    @UserEndpoint(path = {"message", "send"}, keys = {"channel", "message"}, types = {String.class, String.class})
    public static JSONObject sendMessage(final JSON data, final UUID userUuid) {
        final UUID channelUuid = data.getUUID("channel");
        final String messageContent = data.get("message");
        final User user = MicroService.getInstance().getUser(userUuid);

        if (user == null) {
            return USER_NOT_FOUND.getJson();
        }

        final Channel channel = App.getChannelHandler().getChannelByUUID(channelUuid);
        if (channel == null) {
            return CHANNEL_NOT_FOUND.getJson();
        }

        final JSONObject content = anJSON()
                .add("message", messageContent)
                .add("send-date", new Date().getTime())
                .build();

        App.getChannelHandler().notifyAllChannelUsers(ChatAction.SEND_MESSAGE, channel, user.getName(), content);

        return simple("success", true);
    }

    @UserEndpoint(path = {"message", "whisper"}, keys = {"channel", "message", "target"}, types = {String.class, String.class, String.class})
    public static JSONObject whisperMessage(final JSON data, final UUID userUuid) {
        final UUID channelUuid = data.getUUID("channel");
        final String messageContent = data.get("message");
        final String targetName = data.get("target");
        final User user = MicroService.getInstance().getUser(userUuid);

        if (user == null) {
            return USER_NOT_FOUND.getJson();
        }

        final Channel channel = App.getChannelHandler().getChannelByUUID(channelUuid);
        if (channel == null) {
            return CHANNEL_NOT_FOUND.getJson();
        }

        final User target = channel.getUserByName(targetName);
        if (target == null) {
            return USER_NOT_IN_CHANNEL.getJson();
        }

        final JSONObject content = anJSON()
                .add("message", messageContent)
                .add("send-date", new Date().getTime())
                .build();

        App.getChannelHandler().notifyUser(target, ChatAction.WHISPER_MESSAGE, channel, user.getName(), content);

        return simple("success", true);
    }
}
