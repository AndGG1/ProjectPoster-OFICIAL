package Chat;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    public Client(JTextField field) {
        try (Socket socket = new Socket("127.0.0.1", 5000)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.socket = socket;
            
            // Thread for reading messages from the server
            Thread readThread = new Thread(() -> {
                try {
                    String responseString;
                    while ((responseString = input.readLine()) != null) {
                        field.setText(field.getText() + "\n" + responseString);
                    }
                } catch (IOException e) {
                    System.out.println("Client Error: " + e.getMessage());
                }
            });
            readThread.start();
            
        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        } finally {
            System.out.println("Client Disconnected!");
        }
    }
    
    public void sendMessageToServer(JTextField scanner, Socket socket) throws IOException {
        // Main thread for sending messages to the server
        String requestString;
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        while (true) {
            System.out.println("Enter string to be sent to server: ");
            requestString = scanner.getText();
            output.println(requestString);
            if (requestString.equalsIgnoreCase("EXIT")) {
                break;
            }
        }
    }
    
    public Socket getSocket() {
        return socket;
    }
}

