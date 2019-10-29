package net.cryptic_game.microservice.chat.channel;

import net.cryptic_game.microservice.wrapper.User;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

public class ChannelTests {

    @Test
    public void testGetUuid() {
        final Channel channel = new Channel("test");
        assertNotNull(channel.getUuid());
    }

    @Test
    public void testGetName() {
        final Channel channel = new Channel("test");
        assertEquals("test", channel.getName());
    }

    @Test
    public void testSetName() {
        final Channel channel = new Channel("test");
        channel.setName("test123");

        assertEquals("test123", channel.getName());
    }

    @Test
    public void testAddUser() {
        final Channel channel = new Channel("test");
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        channel.addUser(testUser);

        assertTrue(channel.getUsers().contains(testUser));
    }

    @Test
    public void testRemoveUser() {
        final Channel channel = new Channel("test");
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        channel.addUser(testUser);

        if (!channel.getUsers().contains(testUser)) {
            fail();
        }

        channel.removeUser(testUser);

        assertFalse(channel.getUsers().contains(testUser));
    }
}
