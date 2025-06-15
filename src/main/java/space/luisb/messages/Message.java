package space.luisb.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import space.luisb.MessageType;

public class Message {
    @SerializedName("type")
    private MessageType messageType;

    public Message(MessageType messageType) {
        this.messageType = messageType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public JsonObject toJson() {
        return new Gson().toJsonTree(this).getAsJsonObject();
    }

    public static Message fromJson(JsonObject json) {
        return new Gson().fromJson(json, Message.class);
    }

    @Override
    public String toString() {
        return toJson().toString();
    }

    public String format() {
        return toString();
    }
}
