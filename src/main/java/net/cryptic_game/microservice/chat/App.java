package net.cryptic_game.microservice.chat;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.chat.channel.ChannelHandler;
import org.apache.log4j.BasicConfigurator;

public class App extends MicroService {

    private static ChannelHandler channelHandler;

    private App() {
        super("chat");
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();

        channelHandler = new ChannelHandler();
        channelHandler.addChanel("global");

        new App();
    }

    public static ChannelHandler getChannelHandler() {
        return channelHandler;
    }
}
