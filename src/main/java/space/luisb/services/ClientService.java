package space.luisb.services;

import com.google.gson.Gson;
import space.luisb.Config;
import space.luisb.MessageType;
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
            socket = new Socket(Config.getHost(), Config.getPort());
            in = socket.getInputStream();
            out = new PrintStream(socket.getOutputStream());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            out.print(new HelloMessage(Config.getUsername()));
            out.flush();

            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
                String response = buffer.toString("UTF-8");

                try {
                    Message message = new Gson().fromJson(response, Message.class);
                    if(message.getMessageType().equals(MessageType.HELLO)) {
                        HelloMessage helloMessage = new Gson().fromJson(response, HelloMessage.class);
                        System.out.println(helloMessage.getUsername() + " join to the chat");
                    }
                    if(message.getMessageType().equals(MessageType.MESSAGE)) {
                        ChatMessage chatMessage = new Gson().fromJson(response, ChatMessage.class);
                        System.out.println("(" + chatMessage.getTTL() + " ttl) " + chatMessage.getUsername() + ": " + chatMessage.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.print(new ChatMessage(Config.getUsername(), "Hi!", 4));
                buffer.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
