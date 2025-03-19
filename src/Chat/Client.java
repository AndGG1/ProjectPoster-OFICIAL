package Chat;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

public class Client {
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private String username;
    private int onlineCount = 1;
    private Frame frame;
    
    public Client(JTextArea field, String username, Frame frame, String host) {
        try {
            socket = new Socket("127.0.0.1", 5000);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.frame = frame;
            
            // Thread for reading messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String responseString;
                    while ((responseString = input.readLine()) != null) {
                            field.append("\n" + responseString);
                    }
                } catch (IOException e) {
                    System.out.println("Client Error: " + e.getMessage());
                }
            });
            readThread.start();
            output.println(username + " joined the chat!");
        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
    
    public void sendMessageToServer(String s, boolean isOwner) throws IOException {
        // Main thread for sending messages to the server
        output.println("Message from " + username + (isOwner ? " (Admin)" : " (Visitor)") + ": " + s);
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public void close() {
        try {
            output.println(username + " disconnecting...");
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
            frame.dispose();
        } catch (IOException e) {
            System.out.println("Client Error while closing: " + e.getMessage());
        }
    }
    
    public int getOnlineCount() {
        return onlineCount;
    }
    
    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }
}
