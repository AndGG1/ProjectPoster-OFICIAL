package Chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleServerChannel {
    private List<SocketChannel> clientChannels = new CopyOnWriteArrayList<>();
    private List<Chat_Interface> clientInterfaces = new ArrayList<>();
    
    public void start() {
            try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
                serverChannel.socket().bind(new InetSocketAddress(5000));
                serverChannel.configureBlocking(false);
                System.out.println("Server is listening on port " + serverChannel.socket().getLocalPort());
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                
                while (true) {
                    SocketChannel clientChannel = serverChannel.accept();
                    Iterator<SocketChannel> iterator = clientChannels.iterator();
                    if (clientChannel != null) {
                        clientChannel.configureBlocking(false);
                            clientChannels.add(clientChannel);
                        System.out.printf("Client %s connected%n", clientChannel.socket().getRemoteSocketAddress());
                    }
                    
                        while (iterator.hasNext()) {
                            SocketChannel client = iterator.next();
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
                                    String message = String.format("%s", new String((data), StandardCharsets.UTF_8));
                                    
                                    for (SocketChannel otherClient : clientChannels) {
                                        buffer.clear();
                                        buffer.put((message).getBytes());
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
    
    public List<SocketChannel> getClientChannels() {
        return clientChannels;
    }
    
    public List<Chat_Interface> getClientInterfaces() {
        return clientInterfaces;
    }
}
