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
                selector.select(5000); // 5-second timeout to prevent indefinite blocking
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator(); // Fix: Use selectedKeys()
                
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove(); // Remove processed key
                    
                    if (key.isAcceptable()) {
                        // Accept new client connection
                        SelectorTasks.acceptUser(selector, serverChannel);
                    } else if (key.isReadable()) {
                        // Read data from a client
                        SelectorTasks.readMessage(selector, byteBuffer, key);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
        }
    }
    
    private static void broadcastMessage(Selector selector, String message) {
        ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        for (SelectionKey key : selector.keys()) {
            if (key.isValid() && key.channel() instanceof SocketChannel) { // Fix: Check if key is valid
                SocketChannel clientChannel = (SocketChannel) key.channel();
                try {
                    buffer.rewind();
                    clientChannel.write(buffer);
                } catch (IOException e) {
                    System.out.printf("Failed to send message to client %s%n", clientChannel.socket().getRemoteSocketAddress());
                    try {
                        clientChannel.close();
                    } catch (IOException ignored) {
                        // Do nothing
                    }
                    key.cancel();
                }
            }
        }
    }

    protected static class SelectorTasks {
        public static void acceptUser(Selector selector, ServerSocketChannel serverChannel) throws IOException {
            SocketChannel clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            System.out.printf("Client %s connected%n", clientChannel.socket().getRemoteSocketAddress());
        }

        public static void readMessage(Selector selector, ByteBuffer byteBuffer, SelectionKey key) throws IOException {
            SocketChannel clientChannel = (SocketChannel) key.channel();
            byteBuffer.clear();

            int bytesRead = clientChannel.read(byteBuffer);
            if (bytesRead == -1) { // Client disconnected
                System.out.printf("Client %s disconnected%n", clientChannel.socket().getRemoteSocketAddress());
                clientChannel.close();
                key.cancel();
            } else if (bytesRead > 0) {
                byteBuffer.flip();
                byte[] data = new byte[byteBuffer.remaining()];
                byteBuffer.get(data);
                String message = new String(data, StandardCharsets.UTF_8);
                System.out.printf("Received message: %s%n", message);

                // Broadcast the message to all clients
                broadcastMessage(selector, message);
            }
        }
    }
}