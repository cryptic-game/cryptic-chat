package net.cryptic_game.microservice.chat.channel;

import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.wrapper.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class ChannelTests {

    @Mock
    ChannelHandler channelHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStatic(App.class);
        when(App.getChannelHandler()).thenReturn(channelHandler);
    }

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
        channel.addUser(new User(UUID.randomUUID(), "test123", "test@test.test", new Date(), new Date()));

        assertTrue(channel.getUsers().contains(testUser));
    }

    @Test
    public void testAddUserReturn() {
        final Channel channel = new Channel("test");
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());

        assertTrue(channel.addUser(testUser));
    }

    @Test
    public void testDupedAddUser() {
        final Channel channel = new Channel("test");
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        channel.addUser(testUser);
        assertFalse(channel.addUser(testUser));
    }

    @Test
    public void testRemoveUser() {
        final Channel channel = new Channel("test");
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        channel.addUser(testUser);
        channel.addUser(new User(UUID.randomUUID(), "test123", "test@test.test", new Date(), new Date()));

        if (!channel.getUsers().contains(testUser)) {
            fail();
        }

        channel.removeUser(testUser);

        assertFalse(channel.getUsers().contains(testUser));
    }

    @Test
    public void testRemoveUserReturn() {
        final Channel channel = new Channel("test");
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        channel.addUser(testUser);
        channel.addUser(new User(UUID.randomUUID(), "test123", "test@test.test", new Date(), new Date()));

        if (!channel.getUsers().contains(testUser)) {
            fail();
        }

        assertTrue(channel.removeUser(testUser));
    }

    @Test
    public void testRemoveUserWithoutExist() {
        final Channel channel = new Channel("test");
        channel.addUser(new User(UUID.randomUUID(), "test123", "test@test.test", new Date(), new Date()));
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());

        assertFalse(channel.removeUser(testUser));
    }

    @Test
    public void testGetUserByName() {
        final Channel channel = new Channel("test");
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        channel.addUser(testUser);

        assertEquals(testUser, channel.getUserByName(testUser.getName()));
    }

    @Test
    public void testGetUserByNameWithoutExist() {
        final Channel channel = new Channel("test");
        channel.addUser(new User(UUID.randomUUID(), "test123", "test@test.test", new Date(), new Date()));
        final User testUser = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());

        assertNull(channel.getUserByName(testUser.getName()));
    }
}
