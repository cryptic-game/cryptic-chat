package net.cryptic_game.microservice.chat.endpoint;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.chat.channel.Channel;
import net.cryptic_game.microservice.chat.channel.ChannelHandler;
import net.cryptic_game.microservice.chat.channel.ChatAction;
import net.cryptic_game.microservice.endpoint.UserEndpoint;
import net.cryptic_game.microservice.utils.JSON;
import net.cryptic_game.microservice.wrapper.User;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;
import static net.cryptic_game.microservice.utils.JSONBuilder.simple;

public class ChannelEndpoints {

    @UserEndpoint(path = {"channel", "list"}, keys = {}, types = {})
    public static JSONObject getChannel(final JSON data, final UUID uuid) {
        final ChannelHandler channelHandler = ((App) App.getInstance()).getChannelHandler();

        final List<JSONObject> channelsJson = new ArrayList<>();

        for (final Channel channel : channelHandler.getChannels()) {
            channelsJson.add(anJSON()
                    .add("uuid", channel.getUuid())
                    .add("name", channel.getName())
                    .build());
        }

        return simple("channel", channelsJson);
    }

    @UserEndpoint(path = {"channel", "members"}, keys = {"channel"}, types = {UUID.class})
    public static JSONObject getChannelMembers(final JSON data, final UUID uuid) {
        final ChannelHandler channelHandler = ((App) App.getInstance()).getChannelHandler();

        final Channel channel = channelHandler.getChannelByUUID(data.getUUID("channel"));

        if (channel == null) {
            return simple("error", "Channel with the UUID \"" + data.getUUID("channel").toString() + "\" can't be found.");
        }

        final List<JSONObject> userJson = new ArrayList<>();

        for (final User user : channel.getUser()) {
            userJson.add(simple("name", user.getName()));
        }

        return simple("user", userJson);
    }

    @UserEndpoint(path = {"channel", "join"}, keys = {"channel"}, types = {UUID.class})
    public static JSONObject joinChannel(final JSON data, final UUID uuid) {
        final ChannelHandler channelHandler = ((App) App.getInstance()).getChannelHandler();

        final User user = MicroService.getInstance().getUser(uuid);
        final Channel channel = channelHandler.getChannelByUUID(data.getUUID("channel"));

        if (user == null) {
            return simple("error", "user_not_found");
        }
        if (channel == null) {
            return simple("error", "channel_not_found");
        }

        channelHandler.getChannelByUUID(data.getUUID("channel")).addUser(user);
        channelHandler.notifyUsers(ChatAction.MEMBER_JOIN, channel, user.getUUID());

        return simple("success", true);
    }

    @UserEndpoint(path = {"channel", "leave"}, keys = {"channel"}, types = {UUID.class})
    public static JSONObject leaveChannel(final JSON data, final UUID uuid) {
        final ChannelHandler channelHandler = ((App) App.getInstance()).getChannelHandler();

        final User user = MicroService.getInstance().getUser(uuid);
        final Channel channel = channelHandler.getChannelByUUID(data.getUUID("channel"));

        if (user == null) {
            return simple("error", "user_not_found");
        }
        if (channel == null) {
            return simple("error", "channel_not_found");
        }

        channelHandler.getChannelByUUID(data.getUUID("channel")).removeUser(user);
        channelHandler.notifyUsers(ChatAction.MEMBER_LEAVE, channel, user.getUUID());

        return simple("success", true);
    }
}
