package net.cryptic_game.microservice.chat.endpoint;

import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.chat.channel.Channel;
import net.cryptic_game.microservice.chat.channel.ChannelHandler;
import net.cryptic_game.microservice.utils.JSON;
import net.cryptic_game.microservice.wrapper.User;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;
import static net.cryptic_game.microservice.utils.JSONBuilder.simple;
import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(App.class)
public class ChannelEndpointsTests {

    @Mock
    ChannelHandler channelHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStatic(App.class);
        when(App.getChannelHandler()).thenReturn(channelHandler);
    }

    @Test
    public void getChannel() {
        final Channel[] channels = new Channel[2];
        channels[0] = this.channelHandler.addChanel("test");
        channels[1] = this.channelHandler.addChanel("test123");

        final List<JSONObject> resultChannels = (List<JSONObject>) ChannelEndpoints.getChannel(new JSON(new JSONObject()), UUID.randomUUID()).get("channels");

        for (int i = 0; i < resultChannels.size(); i++) {
            final Channel channel = channels[i];
            if (anJSON()
                    .add("uuid", channel.getUuid().toString())
                    .add("name", channel.getName())
                    .build() != resultChannels.get(i)) {
                fail();
            }
        }
        assertTrue(true);
    }

    @Test
    public void getChannelEmpty() {
        final List<JSONObject> resultChannels = (List<JSONObject>) ChannelEndpoints.getChannel(new JSON(new JSONObject()), UUID.randomUUID()).get("channels");
        for (int i = 0; i < resultChannels.size(); i++) {
            fail();
        }
        assertTrue(true);
    }

    @Test
    public void getChannelMembersWithoutChannel() {
        assertEquals(simple("error", "channel_not_found"), ChannelEndpoints.getChannelMembers(
                new JSON(simple("channel", UUID.randomUUID())),
                UUID.randomUUID()));
    }

    @Test
    public void getChannelMembers() {
        this.channelHandler = new ChannelHandler();
        final Channel channel = this.channelHandler.addChanel("test");
        final User[] users = new User[2];
        users[0] = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        users[1] = new User(UUID.randomUUID(), "test1", "tes1t@test1.test1", new Date(), new Date());
        for (final User user : users) channel.addUser(user);

        final List<JSONObject> resultUsers = (List<JSONObject>) ChannelEndpoints.getChannelMembers(
                new JSON(simple("channel", channel.getUuid())),
                UUID.randomUUID())
                .get("users");
        for (int i = 0; i < resultUsers.size(); i++) {
            if (simple("name", users[i].getName()) != resultUsers.get(i)) {
                fail();
            }
        }
        assertTrue(true);
    }

    @Test
    public void joinChannel() {
    }

    @Test
    public void leaveChannel() {
    }
}
