import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 10229);
        final SocketChannel channel = SocketChannel.open();
        channel.connect(address);

        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 8);
            String message;
            while (true) {
                System.out.println("Введите сообщение или 'end' для прекращения работы приложения");
                message = scanner.nextLine();
                if (message.equals("end")) {
                    break;
                }
                channel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(1500);
                int bytesCount = channel.read(inputBuffer);
                System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8));
                inputBuffer.clear();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            channel.close();
        }
    }
}
