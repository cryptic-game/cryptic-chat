package net.cryptic_game.microservice.chat.channel;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.chat.App;
import net.cryptic_game.microservice.wrapper.User;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static net.cryptic_game.microservice.utils.JSONBuilder.anJSON;
import static net.cryptic_game.microservice.utils.JSONBuilder.simple;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MicroService.class, App.class})
public class ChannelHandlerTests {

    @Mock
    MicroService microService;

    @Mock
    ChannelHandler channelHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStatic(MicroService.class);
        mockStatic(App.class);
        when(MicroService.getInstance()).thenReturn(microService);
        when(App.getChannelHandler()).thenReturn(channelHandler);
    }

    @Test
    public void testChannelHandlerAddChannel() {
        final ChannelHandler channelHandler = new ChannelHandler();

        channelHandler.addChanel("test");

        assertEquals("test", channelHandler.getChannels().get(0).getName());
    }

    @Test
    public void testChannelHandlerRemoveChannel() {
        final ChannelHandler channelHandler = new ChannelHandler();

        channelHandler.addChanel("test");

        final Channel testChannel = channelHandler.getChannels().get(0);
        if (!channelHandler.getChannels().get(0).getName().equals("test")) {
            fail();
        }

        channelHandler.removeChannel(testChannel.getUuid(), null);

        assertNull(channelHandler.getChannelByUUID(testChannel.getUuid()));
    }

    @Test
    public void testChannelHandlerGetChannelByUuid() {
        final ChannelHandler channelHandler = new ChannelHandler();

        channelHandler.addChanel("test");
        final Channel testChannel = channelHandler.getChannels().get(0);

        assertNotNull(channelHandler.getChannelByUUID(testChannel.getUuid()));
    }

    @Test
    public void testNotifyUserWithOutJson() {
        doNothing().when(microService).sendToUser(any(UUID.class), any(JSONObject.class));

        final User target = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final User user = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final Channel channel = new Channel("");

        final ChannelHandler channelHandler = new ChannelHandler();
        channelHandler.notifyUser(user, ChatAction.CHANNEL_DELETE, channel, target.getUUID().toString());

        final JSONObject jsonObject = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", anJSON()
                        .add("action", ChatAction.CHANNEL_DELETE.getValue())
                        .add("channel", channel.getUuid().toString())
                        .add("user", target.getUUID().toString())
                        .build()
                ).build();

        verify(microService, times(1)).sendToUser(user.getUUID(), jsonObject);
    }

    @Test
    public void testNotifyUser() {
        doNothing().when(microService).sendToUser(any(UUID.class), any(JSONObject.class));

        final User target = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final User user = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final Channel channel = new Channel("");

        final ChannelHandler channelHandler = new ChannelHandler();
        channelHandler.notifyUser(user, ChatAction.CHANNEL_DELETE, channel, target.getUUID().toString(), simple("abc", "cba"));

        final JSONObject jsonObject = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", anJSON()
                        .add("action", ChatAction.CHANNEL_DELETE.getValue())
                        .add("channel", channel.getUuid().toString())
                        .add("user", target.getUUID().toString())
                        .add("content", simple("abc", "cba"))
                        .build()
                ).build();

        verify(microService, times(1)).sendToUser(user.getUUID(), jsonObject);
    }

    @Test
    public void testNotifyAllChannelUsersWithOutJson() {
        doNothing().when(microService).sendToUser(any(UUID.class), any(JSONObject.class));

        final User target = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "", "", new Date(), new Date()));

        final Channel channel = new Channel("");
        users.forEach(channel::addUser);

        final ChannelHandler channelHandler = new ChannelHandler();
        channelHandler.notifyAllChannelUsers(ChatAction.CHANNEL_DELETE, channel, target.getUUID().toString());

        final JSONObject jsonObject = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", anJSON()
                        .add("action", ChatAction.CHANNEL_DELETE.getValue())
                        .add("channel", channel.getUuid().toString())
                        .add("user", target.getUUID().toString())
                        .build()
                ).build();

        users.forEach(user -> verify(microService, times(1)).sendToUser(user.getUUID(), jsonObject));
    }

    @Test
    public void testNotifyAllChannelUsers() {
        doNothing().when(microService).sendToUser(any(UUID.class), any(JSONObject.class));

        final User target = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "", "", new Date(), new Date()));

        final Channel channel = new Channel("");
        users.forEach(channel::addUser);

        final ChannelHandler channelHandler = new ChannelHandler();
        channelHandler.notifyAllChannelUsers(ChatAction.CHANNEL_DELETE, channel, target.getUUID().toString(), simple("abc", "cba"));

        final JSONObject jsonObject = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", anJSON()
                        .add("action", ChatAction.CHANNEL_DELETE.getValue())
                        .add("channel", channel.getUuid().toString())
                        .add("user", target.getUUID().toString())
                        .add("content", simple("abc", "cba"))
                        .build()
                ).build();

        users.forEach(user -> verify(microService, times(1)).sendToUser(user.getUUID(), jsonObject));
    }

    @Test
    public void testNotifyUsersWithOutJson() {
        doNothing().when(microService).sendToUser(any(UUID.class), any(JSONObject.class));

        final User target = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "", "", new Date(), new Date()));

        final Channel channel = new Channel("");

        final ChannelHandler channelHandler = new ChannelHandler();
        channelHandler.notifyUsers(users, ChatAction.CHANNEL_DELETE, channel, target.getUUID().toString());

        final JSONObject jsonObject = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", anJSON()
                        .add("action", ChatAction.CHANNEL_DELETE.getValue())
                        .add("channel", channel.getUuid().toString())
                        .add("user", target.getUUID().toString())
                        .build()
                ).build();

        users.forEach(user -> verify(microService, times(1)).sendToUser(user.getUUID(), jsonObject));
    }

    @Test
    public void testNotifyUsers() {
        doNothing().when(microService).sendToUser(any(UUID.class), any(JSONObject.class));

        final User target = new User(UUID.randomUUID(), "", "", new Date(), new Date());

        final List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "", "", new Date(), new Date()));

        final Channel channel = new Channel("");

        final ChannelHandler channelHandler = new ChannelHandler();
        channelHandler.notifyUsers(users, ChatAction.CHANNEL_DELETE, channel, target.getUUID().toString(), simple("abc", "cba"));

        final JSONObject jsonObject = anJSON()
                .add("notify-id", "chat-update")
                .add("origin", "chat")
                .add("data", anJSON()
                        .add("action", ChatAction.CHANNEL_DELETE.getValue())
                        .add("channel", channel.getUuid().toString())
                        .add("user", target.getUUID().toString())
                        .add("content", simple("abc", "cba"))
                        .build()
                ).build();

        users.forEach(user -> verify(microService, times(1)).sendToUser(user.getUUID(), jsonObject));
    }
}

