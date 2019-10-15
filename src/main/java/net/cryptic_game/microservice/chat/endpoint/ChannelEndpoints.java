package net.cryptic_game.microservice.chat.endpoint;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.chat.channel.Channel;
import net.cryptic_game.microservice.chat.channel.ChannelHandler;
import net.cryptic_game.microservice.endpoint.UserEndpoint;
import net.cryptic_game.microservice.utils.JSON;
import net.cryptic_game.microservice.utils.JSONBuilder;
import net.cryptic_game.microservice.wrapper.User;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;
import static net.cryptic_game.microservice.utils.JSONBuilder.simple;

public class ChannelEndpoints {

    @UserEndpoint(path = {"channel", "list"}, keys = {}, types = {})
    public static JSONObject getChannel(final JSON data, final UUID uuid) {
        final ChannelHandler channelHandler = ((App) App.getInstance()).getChannelHandler();

        final ArrayList<JSONBuilder> channelsJson = new ArrayList<>();

        for (final Channel channel : channelHandler.getChannels()) {
            final JSONBuilder jsonBuilder = anJSON();
            jsonBuilder.add("uuid", channel.getUuid());
            jsonBuilder.add("name", channel.getName());
            channelsJson.add(jsonBuilder);
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

        final ArrayList<JSONObject> userJson = new ArrayList<>();

        for (final User user : channel.getUser()) {
            final JSONBuilder jsonBuilder = anJSON();
            jsonBuilder.add("uuid", user.getUUID());
            jsonBuilder.add("name", user.getName());
            jsonBuilder.add("mail", user.getMail());
            jsonBuilder.add("created", user.getCreated());
            jsonBuilder.add("last", user.getLast());
            userJson.add(jsonBuilder.build());
        }

        return simple("user", userJson);
    }

    @UserEndpoint(path = {"channel", "join"}, keys = {"channel"}, types = {UUID.class})
    public static JSONObject joinChannel(final JSON data, final UUID uuid) {
        final ChannelHandler channelHandler = ((App) App.getInstance()).getChannelHandler();

        final User user = MicroService.getInstance().getUser(uuid);
        final Channel channel = channelHandler.getChannelByUUID(data.getUUID("channel"));

        if (user == null) {
            return simple("error", "User with the UUID \"" + uuid.toString() + "\" can't be found.");
        }
        if (channel == null) {
            return simple("error", "Channel with the UUID \"" + data.getUUID("channel").toString() + "\" can't be found.");
        }

        channelHandler.getChannelByUUID(data.getUUID("channel")).addUser(user);

        return simple("info", "Added User \"" + user.getUUID().toString() + "\" to channel \"" + channel.getUuid().toString() + "\".");
    }

    @UserEndpoint(path = {"channel", "leave"}, keys = {"channel"}, types = {UUID.class})
    public static JSONObject leaveChannel(final JSON data, final UUID uuid) {
        final ChannelHandler channelHandler = ((App) App.getInstance()).getChannelHandler();

        final User user = MicroService.getInstance().getUser(uuid);
        final Channel channel = channelHandler.getChannelByUUID(data.getUUID("channel"));

        if (user == null) {
            return simple("error", "User with the UUID \"" + uuid.toString() + "\" can't be found.");
        }
        if (channel == null) {
            return simple("error", "Channel with the UUID \"" + data.getUUID("channel").toString() + "\" can't be found.");
        }

        channelHandler.getChannelByUUID(data.getUUID("channel")).removeUser(user);

        return simple("info", "Removed User \"" + user.getUUID().toString() + "\" from channel \"" + channel.getUuid().toString() + "\".");
    }
}
