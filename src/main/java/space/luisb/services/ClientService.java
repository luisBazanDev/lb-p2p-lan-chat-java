package space.luisb.services;

import com.google.gson.Gson;
import space.luisb.Config;
import space.luisb.messages.ChatMessage;
import space.luisb.messages.HelloMessage;
import space.luisb.messages.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private Socket socket;
    private InputStream in;
    private PrintStream out;
    private String host;
    private int port;
    private static List<ClientService> connections = new ArrayList<>();

    public ClientService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void broadcastMessage(ChatMessage chatMessage) {
        ChatMessage finalMessage =  new ChatMessage(chatMessage.getUsername(), chatMessage.getMessage(), chatMessage.getTTL() - 1, chatMessage.getUUID());

        connections.forEach(clientService -> {
            clientService.sendMessage(finalMessage);
        });
    }

    public void sendMessage(ChatMessage chatMessage) {
        out.print(chatMessage);
    }

    public void start() {
        try {
            System.out.println("Connecting to server...");
            socket = new Socket(host, port);
            in = socket.getInputStream();
            out = new PrintStream(socket.getOutputStream());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            out.print(new HelloMessage(Config.getUsername()));
            out.flush();

            new Thread(() -> {
                byte[] data = new byte[1024];
                int bytesRead;
                try {
                    while ((bytesRead = in.read(data)) != -1) {
                        buffer.write(data, 0, bytesRead);
                        String rawMessage = buffer.toString("UTF-8");

                        try {
                            // Try parse json
                            Message message = new Gson().fromJson(rawMessage, Message.class);
                            switch (message.getMessageType()) {
                                case HELLO:
                                    ChatService.addMessage(new Gson().fromJson(rawMessage, HelloMessage.class));
                                    break;
                                case MESSAGE:
                                    ChatService.addMessage(new Gson().fromJson(rawMessage, ChatMessage.class));
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        buffer.reset();
                    }
                } catch (IOException e) {

                }
            }).start();
            connections.add(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            connections.remove(this);
            this.socket.close();
        } catch (IOException e) {}
    }
}
