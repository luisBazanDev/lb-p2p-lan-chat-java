package space.luisb.messages;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import space.luisb.MessageType;

public class HelloMessage extends Message {
    private class Payload {
        @SerializedName("username")
        private final String username;
        public Payload(String username){
            this.username = username;
        }
    }

    @SerializedName("payload")
    private final Payload payload;

    public HelloMessage(String username) {
        super(MessageType.HELLO);
        this.payload = new Payload(username);
    }

    public String getUsername() {
        return payload.username;
    }

    public static HelloMessage fromJson(JsonObject jsonObject) {
        try {
            String username = jsonObject.get("payload.username").getAsString();
            return new HelloMessage(username);
        } catch (Exception e) {
            return null;
        }
    }
}
