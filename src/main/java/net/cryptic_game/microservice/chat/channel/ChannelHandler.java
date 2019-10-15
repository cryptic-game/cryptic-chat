package net.cryptic_game.microservice.chat.channel;

import net.cryptic_game.microservice.MicroService;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;

public class ChannelHandler {

    private final ArrayList<Channel> channels;

    public ChannelHandler() {
        this.channels = new ArrayList<>();
    }

    public Channel addChanel(final String name) {
        final Channel channel = new Channel(name);
        this.channels.add(channel);
        return channel;
    }

    public Channel getChannelByUUID(final UUID uuid) {
        for (final Channel channel : this.channels) if (channel.getUuid().equals(uuid)) return channel;
        return null;
    }

    public ArrayList<Channel> getChannels() {
        return this.channels;
    }

    public void notifyUsers(final ChatAction action, final Channel channel, final UUID target) {
        final JSONObject json = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", anJSON()
                        .add("action", action.getValue())
                        .add("channel", channel)
                        .add("user", channel).build()
                ).build();

        channel.getUser().forEach(user -> MicroService.getInstance().sendToUser(target, json));
    }
}
