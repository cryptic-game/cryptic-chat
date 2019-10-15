package net.cryptic_game.microservice.chat.channel;

public enum ChatAction {
    MEMBER_JOIN("member-join"),
    MEMBER_LEAVE("member-leave");

    final String value;

    ChatAction(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}