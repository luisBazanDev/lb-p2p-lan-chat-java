package space.luisb.services;

import com.google.gson.Gson;
import space.luisb.Config;
import space.luisb.messages.ChatMessage;
import space.luisb.messages.HelloMessage;
import space.luisb.messages.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerService {
    private ServerSocket serverSocket;
    private final List<Socket> clients = new ArrayList<>();

    private static ServerService instance;

    public static ServerService getInstance() {
        if(instance == null) {
            instance = new ServerService();
        }
        return instance;
    }

    public void start() {
        try {
            System.out.println("Starting server...");
            this.serverSocket = new ServerSocket(Config.getPort());
            new Thread(() -> {
                while(this.serverSocket.isBound()) {
                    try {
                        Socket socket = this.serverSocket.accept();
                        clients.add(socket);
                        // Hello socket!
                        new PrintStream(socket.getOutputStream()).println(new HelloMessage(Config.getUsername()));

                        new Thread(() -> {
                            byte[] data = new byte[1024];
                            int bytesRead;
                            try {
                                InputStream in = socket.getInputStream();
                                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Server stopped.");
            }).start();
            System.out.println("Server started on port " + this.serverSocket.getLocalPort());
        } catch (IOException e) {
            System.out.println("Server failed to start on port " + Config.getPort());
        }
    }

    /**
     * This method restart TTL on 1
     * @param chatMessage a ChatMessage
     */
    public void broadcastMessage(ChatMessage chatMessage) {
        Message finalMessage =  new ChatMessage(chatMessage.getUsername(), chatMessage.getMessage(), chatMessage.getTTL() - 1, chatMessage.getUUID());

        this.clients.forEach(socket -> {
            try {
                new PrintStream(socket.getOutputStream()).println(finalMessage);
            } catch (IOException e) {}
        });
    }
}
