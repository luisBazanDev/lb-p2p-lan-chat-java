package space.luisb.messages;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import space.luisb.Config;
import space.luisb.MessageType;

import java.util.UUID;

public class ChatMessage extends Message {
    class Payload {
        @SerializedName("username")
        private String username;
        @SerializedName("message")
        private String message;
        @SerializedName("ttl")
        private int ttl;
        @SerializedName("uuid")
        private String uuid;

        public Payload(String username, String message, int ttl, String uuid) {
            this.username = username;
            this.message = message;
            this.ttl = ttl;
            this.uuid = uuid;
        }
    }
    private Payload payload;

    public ChatMessage(String username, String message, int ttl, String uuid) {
        super(MessageType.MESSAGE);
        this.payload = new Payload(username, message, ttl, uuid);
    }

    public ChatMessage(String username, String message, int ttl) {
        super(MessageType.MESSAGE);
        this.payload = new Payload(username, message, ttl, UUID.randomUUID().toString());
    }

    public ChatMessage(String username, String message) {
        super(MessageType.MESSAGE);
        this.payload = new Payload(username, message, Config.getMaxTTL(), UUID.randomUUID().toString());
    }

    public String getUsername() {
        return payload.username;
    }

    public String getMessage() {
        return payload.message;
    }

    public int getTTL() {
        return payload.ttl;
    }

    public String getUUID() {
        return payload.uuid;
    }

    @Override
    public String format() {
        return String.format("(%d ttl) %s: %s", payload.ttl, payload.username, payload.message);
    }
}
