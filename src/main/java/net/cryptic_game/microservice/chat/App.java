package net.cryptic_game.microservice.chat;

import org.apache.log4j.BasicConfigurator;

import net.cryptic_game.microservice.MicroService;

public class App extends MicroService {

	public App() {
		super("chat");
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();

		new App();
	}

}
