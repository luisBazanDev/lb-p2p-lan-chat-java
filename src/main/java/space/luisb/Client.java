package space.luisb;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private InputStream in;
    private PrintStream out;

    public void start() {
        try {
            socket = new Socket(Config.getHost(), Config.getPort());
            in = socket.getInputStream();
            out = new PrintStream(socket.getOutputStream());
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            out.print("{\"type\":\"HELLO\",\"payload\":{\"username\":\"" + Config.getUsername() + "\"}}");
            out.flush();

            byte[] data = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(data)) != -1) {
                buffer.write(data, 0, bytesRead);
                String response = buffer.toString("UTF-8");
                System.out.println("Server replied: " + response);
                buffer.reset();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
