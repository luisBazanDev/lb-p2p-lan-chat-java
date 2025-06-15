package space.luisb.dialog;

import space.luisb.Config;
import space.luisb.messages.ChatMessage;
import space.luisb.services.ChatService;
import space.luisb.services.ClientService;

import java.util.Scanner;

public class ChatDialog {
    public static void start() {
        Scanner scanner = new Scanner(System.in);
        String input = "";

        while (!input.equals("exit")) {
            System.out.println("Please enter your message:");
            input = scanner.nextLine();
            if (input.equals("exit")) {
                ClientService.getInstance().stop();
                break;
            }
            ChatMessage chatMessage = new ChatMessage(Config.getUsername(), input);
            ChatService.addMessage(chatMessage);
            ClientService.getInstance().sendMessage(chatMessage);
        }
    }
}
