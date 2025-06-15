package space.luisb.services;

import com.google.gson.Gson;
import space.luisb.messages.ChatMessage;
import space.luisb.messages.Message;

import java.util.*;

public class ChatService {
    private final ArrayList<Message> messages = new ArrayList<>();
    private final Set<String> uuidFilter = new HashSet<>();
    private static ChatService instance;

    public static ChatService getInstance() {
        if (instance == null) {
            instance = new ChatService();
        }
        return instance;
    }

    public static void addMessage(Message message) {
        // Prevent messages with same uuid
        if(message instanceof ChatMessage) {
            if(getInstance().uuidFilter.contains(((ChatMessage) message).getUUID()))
                return;
            getInstance().uuidFilter.add(((ChatMessage) message).getUUID());
            ServerService.getInstance().broadcastMessage((ChatMessage) message);
            ClientService.broadcastMessage((ChatMessage) message);
        }

        getInstance().messages.add(message);
        System.out.println(message.format());
    }

    public ArrayList<Message> getChatMessages() {
        return messages;
    }
}
