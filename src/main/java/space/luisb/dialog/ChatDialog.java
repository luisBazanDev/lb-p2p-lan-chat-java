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
                System.exit(0);
                break;
            }
            if(processCommand(input)) continue;
            ChatMessage chatMessage = new ChatMessage(Config.getUsername(), input);
            ChatService.addMessage(chatMessage);
        }
    }

    private static boolean processCommand(String command) {
        if(command.startsWith("/connect") && command.split(" ").length == 2) {
            String hostname = command.split(" ")[1];
            new ClientService(hostname, Config.getPort()).start();
            return true;
        }

        return false;
    }
}
