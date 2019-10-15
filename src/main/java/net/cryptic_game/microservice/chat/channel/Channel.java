package net.cryptic_game.microservice.chat.channel;

import net.cryptic_game.microservice.wrapper.User;

import java.util.ArrayList;
import java.util.UUID;

public class Channel {

    private final UUID uuid;
    private String name;
    private final ArrayList<User> user;

    public Channel(final String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.user = new ArrayList<>();
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<User> getUser() {
        return this.user;
    }

    public void addUser(final User user) {
        this.user.add(user);
    }

    public void removeUser(final User user) {
        this.user.remove(user);
    }
}
