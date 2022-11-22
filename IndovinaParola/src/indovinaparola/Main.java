package indovinaparola;

import GestioneParola.Parola;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    //variabili per il gioco
    static public Parola parola;
    
    
    //variabili per la connessione
    static List<ClientHandler> clients;
    ServerSocket serverSocket;
    static int numOfUsers = 0;
    Socket socket;

    public Main() {
        parola= new Parola();
        clients = new ArrayList<>();
        
        try {
            serverSocket = new ServerSocket(Constants.PORT);
        } catch (IOException ex) {
            log("Server : " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Main indovinaparola = new Main();
        indovinaparola.watiConnection();
    }

    private void watiConnection() {
        log("Server Running...");

        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                log("waitConnection : " + ex.getMessage());
            }

            log("Client accepted : " + socket.getInetAddress());
            numOfUsers++;

            ClientHandler handler = new ClientHandler(socket, "user" + numOfUsers);

            Thread thread = new Thread(handler);
            addClient(handler);
            thread.start();
        }
    }

    public static List<ClientHandler> getClients() {
        return clients;
    }

    private void addClient(ClientHandler client) {
        clients.add(client);
    }

    private void log(String message) {
        System.out.println(message);
    }

}
