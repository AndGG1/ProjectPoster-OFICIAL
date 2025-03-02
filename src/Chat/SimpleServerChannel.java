package Chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SimpleServerChannel {
    public static void main(String[] args) {
        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.socket().bind(new InetSocketAddress(5000));
            serverChannel.configureBlocking(false);
            System.out.println("Server is listening on port " + serverChannel.socket().getLocalPort());
            List<SocketChannel> clientChannels = new ArrayList<>();
            ByteBuffer buffer = ByteBuffer.allocate(256);
            
            while (true) {
                SocketChannel clientChannel = serverChannel.accept();
                if (clientChannel != null) {
                    clientChannel.configureBlocking(false);
                    clientChannels.add(clientChannel);
                    System.out.printf("Client %s connected%n", clientChannel.socket().getRemoteSocketAddress());
                }
                
                for (SocketChannel client : clientChannels) {
                    try {
                        buffer.clear();
                        int bytesRead = client.read(buffer);
                        if (bytesRead == -1) {
                            System.out.printf("Client %s disconnected%n", client.socket().getRemoteSocketAddress());
                            clientChannels.remove(client);
                            client.close();
                        } else if (bytesRead > 0) {
                            buffer.flip();
                            byte[] data = new byte[buffer.remaining()];
                            buffer.get(data);
                            String message = "\"%s\" ".formatted(new String(data, StandardCharsets.UTF_8));
                            
                            for (SocketChannel otherClient : clientChannels) {
                                    buffer.clear();
                                    buffer.put(("Message from " + client.socket().getRemoteSocketAddress() + ": " + message).getBytes());
                                    buffer.flip();
                                    while (buffer.hasRemaining()) {
                                        otherClient.write(buffer);
                                    }
                            }
                        }
                    } catch (IOException e) {
                        System.out.printf("Client %s disconnected due to error%n", client.socket().getRemoteSocketAddress());
                        clientChannels.remove(client);
                        client.close();
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
