package net.cryptic_game.microservice.chat.endpoint;

import org.json.simple.JSONObject;

import static net.cryptic_game.microservice.utils.JSONBuilder.simple;

public enum EndpointResponse {

    USER_NOT_FOUND(simple("error", "user_not_found")),
    CHANNEL_NOT_FOUND(simple("error", "channel_not_found")),
    FAIL(simple("success", false)),
    SUCCESS(simple("success", true));

    final JSONObject json;

    EndpointResponse(final JSONObject json) {
        this.json = json;
    }

    public JSONObject getJson() {
        return this.json;
    }
}
