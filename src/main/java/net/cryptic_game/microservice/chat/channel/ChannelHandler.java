package net.cryptic_game.microservice.chat.channel;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.utils.JSONBuilder;
import net.cryptic_game.microservice.wrapper.User;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;

public class ChannelHandler {

    private final List<Channel> channels;

    public ChannelHandler() {
        this.channels = new ArrayList<>();
    }

    public Channel addChanel(final String name) {
        final Channel channel = new Channel(name);
        this.channels.add(channel);
        return channel;
    }

    public void removeChannel(final UUID channelUuid, final UUID userUuid) {
        this.removeChannel(channelUuid, userUuid, true);
    }

    public void removeChannel(final UUID channelUuid, final UUID userUuid, final boolean notifyUsers) {
        final Channel channel = this.getChannelByUUID(channelUuid);
        if (notifyUsers) this.notifyAllChannelUsers(ChatAction.CHANNEL_DELETE, channel, userUuid);
        this.channels.remove(channel);
    }

    public Channel getChannelByUUID(final UUID uuid) {
        for (final Channel channel : this.channels) if (channel.getUuid().equals(uuid)) return channel;
        return null;
    }

    public List<Channel> getChannels() {
        return this.channels;
    }

    public void notifyUser(final User user, final ChatAction action, final Channel channel, final UUID target) {
        this.notifyUsers(new ArrayList<>(Collections.singletonList(user)), action, channel, target);
    }

    public void notifyUser(final User user, final ChatAction action, final Channel channel, final UUID target, final JSONObject content) {
        this.notifyUsers(new ArrayList<>(Collections.singletonList(user)), action, channel, target, content);
    }

    public void notifyUsers(final List<User> users, final ChatAction action, final Channel channel, final UUID target) {
        this.notifyUsers(users, action, channel, target, null);
    }

    public void notifyAllChannelUsers(final ChatAction action, final Channel channel, final UUID target) {
        this.notifyUsers(channel.getUsers(), action, channel, target, null);
    }

    public void notifyAllChannelUsers(final ChatAction action, final Channel channel, final UUID target, final JSONObject content) {
        this.notifyUsers(channel.getUsers(), action, channel, target, content);
    }

    public void notifyUsers(final List<User> users, ChatAction action, final Channel channel, final UUID target, final JSONObject content) {
        final JSONBuilder data = anJSON()
                .add("action", action.getValue())
                .add("channel", channel.getUuid().toString())
                .add("user", target.toString());

        if (content != null) {
            data.add("content", content);
        }

        final JSONObject json = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", data.build()
                ).build();

        users.forEach(user -> MicroService.getInstance().sendToUser(user.getUUID(), json));
    }
}
