package Chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.List;
import java.util.Set;

public class CustomServerSocket extends ServerSocketChannel {
    private final ServerSocketChannel delegate; // Real instance
    private List<SocketChannel> clientChannels;
    
    public CustomServerSocket(SelectorProvider provider, List<SocketChannel> clientChannels) throws IOException {
        super(provider);
        this.delegate = ServerSocketChannel.open(); // Create a real instance
        this.clientChannels = clientChannels;
    }
    
    @Override
    public ServerSocketChannel bind(SocketAddress local, int backlog) throws IOException {
        return delegate.bind(local, backlog); // Delegate the call
    }
    
    @Override
    public <T> ServerSocketChannel setOption(SocketOption<T> name, T value) throws IOException {
        return delegate.setOption(name, value); // Delegate the call
    }
    
    @Override
    public <T> T getOption(SocketOption<T> name) throws IOException {
        return delegate.getOption(name); // Delegate the call
    }
    
    @Override
    public Set<SocketOption<?>> supportedOptions() {
        return delegate.supportedOptions(); // Delegate the call
    }
    
    @Override
    public ServerSocket socket() {
        return delegate.socket(); // Delegate the call
    }
    
    @Override
    public SocketChannel accept() throws IOException {
        return delegate.accept(); // Delegate the call
    }
    
    @Override
    public SocketAddress getLocalAddress() throws IOException {
        return delegate.getLocalAddress(); // Delegate the call
    }
    
    @Override
    protected void implCloseSelectableChannel() throws IOException {
        delegate.close(); // Close the delegate
    }
    
    @Override
    protected void implConfigureBlocking(boolean block) throws IOException {
        delegate.configureBlocking(block); // Delegate the call
    }
    
    public List<SocketChannel> getClientChannels() {
        return clientChannels;
    }
}
