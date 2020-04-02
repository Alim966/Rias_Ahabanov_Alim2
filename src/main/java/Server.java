import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final int PORT = 4488;

    public Server(int portnum) {
        try {
            server = new ServerSocket(portnum);
        }
        catch (Exception err) {
            System.out.println(err);
        }
    }

    public void serve() {
        try {
            while (true) {
                Socket client = server.accept();
                BufferedReader r = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter w = new PrintWriter(client.getOutputStream(), true);
                w.println("");
                String line;
                do {
                    line = r.readLine();
                    if ( line != null )
                        w.println(line);
                }
                while (!line.trim().equals("quit"));
                client.close();
            }
        }
        catch (Exception err) {
            System.err.println(err);
        }
    }

    public static void main(String[] args) {
        Server s = new Server(PORT);
        s.serve();
    }

    private ServerSocket server;
}