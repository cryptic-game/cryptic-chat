package net.cryptic_game.microservice.chat;

import net.cryptic_game.microservice.MicroService;
import net.cryptic_game.microservice.chat.channel.ChannelHandler;
import org.apache.log4j.BasicConfigurator;

public class App extends MicroService {

    private final ChannelHandler channelHandler;

    public App() {
        super("chat");

        this.channelHandler = new ChannelHandler();
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        new App();
    }

    public ChannelHandler getChannelHandler() {
        return this.channelHandler;
    }
}
