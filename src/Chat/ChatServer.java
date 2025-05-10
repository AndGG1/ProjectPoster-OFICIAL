package Chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class ChatServer {
    
    public void start() {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            
            serverChannel.socket().bind(new InetSocketAddress(5000));
            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Server is listening on port: " + serverChannel.socket().getLocalPort());
            
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            
            while (true) {
                selector.select(); // Wait for events to happen.
                Iterator<SelectionKey> keyIterator = selector.keys().iterator();
                
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();
                    
                    if (key.isAcceptable()) {
                        //Accept new client connection.
                        SocketChannel clientChannel = serverChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.printf("Client %s connected%n", clientChannel.socket().getRemoteSocketAddress());
                    } else if (key.isReadable()) {
                        //Read data from a client
                        SocketChannel clientChannel = (SocketChannel) key.channel();
                        byteBuffer.clear();
                        
                        int bytesRead = clientChannel.read(byteBuffer);
                        //Client disconnected.
                        if (bytesRead == -1) {
                            System.out.printf("Client %s disconnected%n", clientChannel.socket().getRemoteSocketAddress());
                            clientChannel.close();
                            key.cancel();
                        } else if (bytesRead > 0) {
                            byteBuffer.flip();
                            byte[] data = new byte[byteBuffer.remaining()];
                            byteBuffer.get(data);
                            String message = new String(data, StandardCharsets.UTF_8);
                            System.out.printf("Received message: %s%n", message);
                            
                            //Broadcast the message to all clients, including the sender.
                            broadcastMessage(selector, message);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void broadcastMessage(Selector selector, String message) {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        for (SelectionKey key : selector.keys()) {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            
            try {
                buffer.rewind();
                clientChannel.write(buffer);
            } catch (IOException e) {
                System.out.printf("Failed to send message to client %s%n", clientChannel.socket().getRemoteSocketAddress());
                try {
                    clientChannel.close();
                } catch (IOException ignored) {
                    //do nothing.
                }
                key.cancel();
            }
        }
    }
}
