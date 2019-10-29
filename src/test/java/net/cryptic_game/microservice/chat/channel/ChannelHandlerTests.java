package net.cryptic_game.microservice.chat.channel;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChannelHandlerTests {

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

        channelHandler.removeChannel(testChannel.getUuid(), null, false);

        assertNull(channelHandler.getChannelByUUID(testChannel.getUuid()));
    }

    @Test
    public void testChannelHandlerGetChannelByUuid() {
        final ChannelHandler channelHandler = new ChannelHandler();

        channelHandler.addChanel("test");
        final Channel testChannel = channelHandler.getChannels().get(0);

        assertNotNull(channelHandler.getChannelByUUID(testChannel.getUuid()));
    }
}
