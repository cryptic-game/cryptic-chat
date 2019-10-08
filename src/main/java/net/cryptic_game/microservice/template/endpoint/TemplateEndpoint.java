package net.cryptic_game.microservice.template.endpoint;

import java.util.UUID;

import org.json.simple.JSONObject;

import net.cryptic_game.microservice.endpoint.UserEndpoint;

public class TemplateEndpoint {

	@UserEndpoint(path = { "echo" },keys = { "message" }, types = { String.class })
	public static JSONObject echo(JSONObject data, UUID user) {
		return data;
	}

}
