package net.cryptic_game.microservice.chat.channel;

import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.wrapper.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Channel {

    private final UUID uuid;
    private final List<User> users;
    private String name;

    public Channel(final String name) {
        this.uuid = UUID.randomUUID();
        this.name = name;
        this.users = new ArrayList<>();
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

    public List<User> getUsers() {
        return this.users;
    }

    public boolean addUser(final User user) {
        for (final User u : this.users) {
            if (u.getUUID().equals(user.getUUID())) {
                return false;
            }
        }
        App.getChannelHandler().notifyAllChannelUsers(ChatAction.MEMBER_JOIN, this, user.getName());
        this.users.add(user);
        return true;
    }

    public boolean removeUser(final User user) {
        for (final User u : this.users) {
            if (u.getUUID().equals(user.getUUID())) {
                this.users.remove(user);
                App.getChannelHandler().notifyAllChannelUsers(ChatAction.MEMBER_LEAVE, this, user.getName());
                return true;
            }
        }
        return false;
    }

    public User getUserByName(final String name) {
        for (final User user : this.users) {
            if (user.getName().equals(name)) return user;
        }
        return null;
    }
}
