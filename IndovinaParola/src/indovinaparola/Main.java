package indovinaparola;

import GestioneParola.Controllo;
import GestioneParola.Parola;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Main {
    //variabili per il gioco
    static public Parola parola;
    static public Controllo controllo;
    static List<String> classifica;
    static List<Integer> tentativi;
    //variabili per la connessione
    static List<ClientHandler> clients;
    ServerSocket serverSocket;
    static int numOfUsers = 0;
    Socket socket;

    public Main() {
        parola= new Parola();
        controllo=new Controllo(parola);
        clients = new ArrayList<>();
        classifica = new ArrayList<>();
        tentativi = new ArrayList<>();
        
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

            ClientHandler handler = new ClientHandler(this, socket, "user" + numOfUsers);

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
    public void setClassifica(String name, int ntentativi){
        classifica.add(name);
        tentativi.add(ntentativi); 
    }
    public void makeClassifica(){
        String tot="Claffica per tentativi:\n";
        
        for (int i = 0; i < classifica.size()-1 ; i++) {
            if(tentativi.get(i)>tentativi.get(i+1)){
                Integer temp = tentativi.get(i);
                tentativi.set(i, tentativi.get(i+1));
                tentativi.set(i+1, temp);
                String temp2 = classifica.get(i);
                classifica.set(i, classifica.get(i+1));
                classifica.set(i+1, temp2);
            }
        }
        for (int i = 0; i < classifica.size() ; i++) {
            tot+= classifica.get(i) + "/ tentativi: " + tentativi.get(i) + "\n";
        }
        
        log(tot);
    }
}
