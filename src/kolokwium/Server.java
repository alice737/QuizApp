package kolokwium;


import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    public static void main(String[] args) {
        int port = 9090;

        try (ServerSocket serverSocket = new ServerSocket(port)){

            while(true) {
                // czeka na klienta, akceptuje, tworzymy wątek dla danego połączenia i uruchamiamy go
                new ServerThread(serverSocket.accept()).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
