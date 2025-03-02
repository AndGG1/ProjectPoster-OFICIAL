package Chat;

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
    
    public Client(JTextArea field) {
        try {
            socket = new Socket("127.0.0.1", 5000);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
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
        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
    
    public void sendMessageToServer(String s) throws IOException {
        // Main thread for sending messages to the server
        output.println(s);
    }
    
    public Socket getSocket() {
        return socket;
    }
    
    public void close() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            System.out.println("Client Error while closing: " + e.getMessage());
        }
    }
}
