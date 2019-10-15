package net.cryptic_game.microservice.chat.channel;

import java.util.ArrayList;
import java.util.UUID;

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
}
