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

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;
import static net.cryptic_game.microservice.utils.JSONBuilder.simple;

public class MessageEndpoints {

    @UserEndpoint(path = {"message", "send"}, keys = {"channel", "message"}, types = {UUID.class, String.class})
    public static JSONObject sendMessage(final JSON data, final UUID userUuid) {
        final UUID channelUuid = data.getUUID("channel");
        final String messageContent = data.get("content");

        final Channel channel = App.getChannelHandler().getChannelByUUID(channelUuid);
        if (channel == null) {
            return simple("error", "channel_not_found");
        }

        final JSONObject content = anJSON()
                .add("message", messageContent)
                .add("send-date", new Date())
                .add("whisper", false)
                .build();

        App.getChannelHandler().notifyAllChannelUsers(ChatAction.SEND_MESSAGE, channel, userUuid, content);

        return simple("success", true);
    }

    @UserEndpoint(path = {"message", "whisper"}, keys = {"channel", "message", "target"}, types = {UUID.class, String.class, UUID.class})
    public static JSONObject whisperMessage(final JSON data, final UUID userUuid) {
        final UUID channelUuid = data.getUUID("channel");
        final String messageContent = data.get("content");
        final UUID targetUuid = data.getUUID("target");

        final Channel channel = App.getChannelHandler().getChannelByUUID(channelUuid);
        if (channel == null) {
            return simple("error", "channel_not_found");
        }

        final User target = MicroService.getInstance().getUser(targetUuid);
        if (target == null) {
            return simple("error", "user_not_found");
        }

        final JSONObject content = anJSON()
                .add("message", messageContent)
                .add("send-date", new Date())
                .add("whisper", true)
                .build();

        App.getChannelHandler().notifyUser(target, ChatAction.SEND_MESSAGE, channel, userUuid, content);

        return simple("success", true);
    }
}