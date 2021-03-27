import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Server {

    public static void main(String[] args) throws IOException {

        Deleter deleter = new Deleter();
        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress("127.0.0.1", 10229));

        try (SocketChannel socketChannel = channel.accept()){
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
            while (socketChannel.isConnected()) {
                int bytesCount = socketChannel.read(inputBuffer);
                if (bytesCount == -1) {
                    break;
                }
                final String message = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                String finalMessage = deleter.replaceSpaces(message);
                inputBuffer.clear();
                socketChannel.write(ByteBuffer.wrap((finalMessage).getBytes(StandardCharsets.UTF_8)));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
