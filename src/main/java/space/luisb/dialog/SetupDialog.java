package space.luisb.dialog;

import space.luisb.Config;

import java.util.Scanner;

public class SetupDialog {
    public static void setup() {
        Scanner scanner = new Scanner(System.in);

        // Username
        System.out.println("Please enter your name: ");
        String name = scanner.nextLine();
        Config.setUsername(name);

        // Host
        String host = "";
        while (!validHost(host)) {
            System.out.println("Please enter the host ip");
            host = scanner.nextLine();
        }
        Config.setHost(host);

        // Port
        System.out.println("Please enter your port: ");
        int port = scanner.nextInt();
        while(port < 0 || port > 65535) {
            System.out.println("Please enter a valid port");
            port = scanner.nextInt();
        }
        Config.setPort(port);

        System.out.println("Setup complete " + name);
    }

    private static boolean validHost(String host) {
        String patternIPv4 = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$";

        if (!host.matches(patternIPv4))
            return false;

        String[] parts = host.split("\\.");

        for (String part : parts) {
            int num = Integer.parseInt(part);
            if (num < 0 || num > 255)
                return false;
        }

        return true;
    }
}
