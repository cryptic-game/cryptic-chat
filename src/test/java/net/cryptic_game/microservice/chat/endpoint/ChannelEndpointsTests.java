package net.cryptic_game.microservice.chat.endpoint;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.chat.channel.Channel;
import net.cryptic_game.microservice.chat.channel.ChannelHandler;
import net.cryptic_game.microservice.chat.channel.ChatAction;
import net.cryptic_game.microservice.utils.JSON;
import net.cryptic_game.microservice.wrapper.User;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;
import static net.cryptic_game.microservice.utils.JSONBuilder.simple;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({App.class, MicroService.class})
public class ChannelEndpointsTests {

    private ChannelHandler channelHandler;
    private MicroService microService;

    @Before
    public void setUp() {
        mockStatic(App.class);
        mockStatic(MicroService.class);
        this.channelHandler = new ChannelHandler();
        this.microService = mock(MicroService.class);
        when(App.getChannelHandler()).thenReturn(this.channelHandler);
        when(MicroService.getInstance()).thenReturn(this.microService);
    }

    @Test
    public void getChannel() {
        final Channel[] channels = new Channel[2];
        channels[0] = this.channelHandler.addChanel("test");
        channels[1] = this.channelHandler.addChanel("test123");

        final List<JSONObject> resultChannels = (List<JSONObject>) ChannelEndpoints.getChannel(new JSON(new JSONObject()), UUID.randomUUID()).get("channels");

        assertEquals(channels.length, resultChannels.size());

        for (int i = 0; i < resultChannels.size(); i++) {
            final Channel channel = channels[i];
            assertEquals(anJSON()
                    .add("uuid", channel.getUuid().toString())
                    .add("name", channel.getName())
                    .build(), resultChannels.get(i));
        }
    }

    @Test
    public void getChannelEmpty() {
        final List<JSONObject> resultChannels = (List<JSONObject>) ChannelEndpoints.getChannel(new JSON(new JSONObject()), UUID.randomUUID()).get("channels");
        assertEquals(resultChannels.size(), 0);
    }

    @Test
    public void getChannelMembersWithoutChannel() {
        assertEquals(simple("error", "channel_not_found").toString(), ChannelEndpoints.getChannelMembers(new JSON(simple("channel", UUID.randomUUID().toString())), UUID.randomUUID()).toString());
    }

    @Test
    public void getChannelMembers() {
        this.channelHandler = mock(ChannelHandler.class);
        when(App.getChannelHandler()).thenReturn(this.channelHandler);
        doNothing().when(this.channelHandler).notifyAllChannelUsers(isA(ChatAction.class), isA(Channel.class), isA(String.class));

        final Channel channel = new Channel("test123");
        final User[] users = new User[2];
        users[0] = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        users[1] = new User(UUID.randomUUID(), "test1", "tes1t@test1.test1", new Date(), new Date());
        for (final User user : users) {
            channel.addUser(user);
        }

        when(this.channelHandler.getChannelByUUID(channel.getUuid())).thenReturn(channel);

        final List<JSONObject> resultUsers = (List<JSONObject>) ChannelEndpoints.getChannelMembers(
                new JSON(simple("channel", channel.getUuid().toString())),
                UUID.randomUUID())
                .get("users");

        assertEquals(users.length, resultUsers.size());

        for (int i = 0; i < resultUsers.size(); i++) {
            assertEquals(users[i].getName(), resultUsers.get(i).get("name"));
        }
    }

    @Test
    public void joinChannelEmptyUser() {
        assertEquals(simple("error", "user_not_found").toString(), ChannelEndpoints.leaveChannel(new JSON(simple("channel", UUID.randomUUID())), UUID.randomUUID()).toString());
    }

    @Test
    public void joinChannelEmptyChannel() {
        final User user = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        when(this.microService.getUser(isA(UUID.class))).thenReturn(user);
        assertEquals(simple("error", "channel_not_found").toString(), ChannelEndpoints.leaveChannel(new JSON(simple("channel", UUID.randomUUID())), user.getUUID()).toString());
    }

    @Test
    public void joinChannelAlreadyJoined() {
        final Channel channel = this.channelHandler.addChanel("test123");
        final User user = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());

        this.channelHandler = mock(ChannelHandler.class);
        when(App.getChannelHandler()).thenReturn(this.channelHandler);
        doNothing().when(this.channelHandler).notifyAllChannelUsers(isA(ChatAction.class), isA(Channel.class), isA(String.class));
        when(this.channelHandler.getChannelByUUID(channel.getUuid())).thenReturn(channel);

        channel.addUser(user);
        when(this.microService.getUser(isA(UUID.class))).thenReturn(user);
        assertEquals(simple("success", false).toString(), ChannelEndpoints.joinChannel(new JSON(simple("channel", channel.getUuid().toString())), user.getUUID()).toString());
    }

    @Test
    public void joinChannel() {
        final Channel channel = this.channelHandler.addChanel("test123");
        final User user = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        when(this.microService.getUser(isA(UUID.class))).thenReturn(user);
        assertEquals(simple("success", true).toString(), ChannelEndpoints.joinChannel(new JSON(simple("channel", channel.getUuid().toString())), user.getUUID()).toString());
    }

    @Test
    public void leaveChannelEmptyUser() {
        assertEquals(simple("error", "user_not_found").toString(), ChannelEndpoints.joinChannel(new JSON(simple("channel", UUID.randomUUID())), UUID.randomUUID()).toString());
    }

    @Test
    public void leaveChannelEmptyChannel() {
        final User user = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());
        when(this.microService.getUser(isA(UUID.class))).thenReturn(user);
        assertEquals(simple("error", "channel_not_found").toString(), ChannelEndpoints.joinChannel(new JSON(simple("channel", UUID.randomUUID())), user.getUUID()).toString());
    }

    @Test
    public void leaveChannelWithoutUser() {
        final Channel channel = this.channelHandler.addChanel("test123");
        final User user = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());

        this.channelHandler = mock(ChannelHandler.class);
        when(App.getChannelHandler()).thenReturn(this.channelHandler);
        when(this.channelHandler.getChannelByUUID(channel.getUuid())).thenReturn(channel);

        when(this.microService.getUser(isA(UUID.class))).thenReturn(user);
        assertEquals(simple("success", false).toString(), ChannelEndpoints.leaveChannel(new JSON(simple("channel", channel.getUuid().toString())), user.getUUID()).toString());
    }

    @Test
    public void leaveChannel() {
        final Channel channel = this.channelHandler.addChanel("test123");
        final User user = new User(UUID.randomUUID(), "test", "test@test.test", new Date(), new Date());

        this.channelHandler = mock(ChannelHandler.class);
        when(App.getChannelHandler()).thenReturn(this.channelHandler);
        doNothing().when(this.channelHandler).notifyAllChannelUsers(isA(ChatAction.class), isA(Channel.class), isA(String.class));
        when(this.channelHandler.getChannelByUUID(channel.getUuid())).thenReturn(channel);

        channel.addUser(user);
        when(this.microService.getUser(isA(UUID.class))).thenReturn(user);
        assertEquals(simple("success", true).toString(), ChannelEndpoints.leaveChannel(new JSON(simple("channel", channel.getUuid().toString())), user.getUUID()).toString());
    }
}
