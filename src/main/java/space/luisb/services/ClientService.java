package space.luisb.services;

import com.google.gson.Gson;
import space.luisb.Config;
import space.luisb.messages.ChatMessage;
import space.luisb.messages.HelloMessage;
import space.luisb.messages.Message;

import java.io.*;
import java.net.Socket;

public class ClientService {
    private Socket socket;
    private InputStream in;
    private PrintStream out;

    public void start() {
        try {
            System.out.println("Connecting to server...");
            socket = new Socket(Config.getHost(), Config.getPort());
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
            System.out.println("Connected!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.socket.close();
        } catch (IOException e) {}
    }
}
