package indovinaparola;

import GestioneParola.Controllo;
import GestioneParola.Parola;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    //variabili per il gioco
    static public Parola parola;
    static public Controllo controllo;
    static List<String> nomi;
    static List<Integer> tentativi;
    static String[] classifica;
    String visualizzaClassifica = "Classifica per tentativi:\n";
    //variabili per la connessione
    static List<ClientHandler> clients;
    ServerSocket serverSocket;
    static int numOfUsers = 0;
    Socket socket;

    public Main() {
        parola = new Parola();
        controllo = new Controllo(parola);
        clients = new ArrayList<>();
        nomi = new ArrayList<>();
        tentativi = new ArrayList<>();
        classifica = new String[10];

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
        log("Parola segreta: " + parola.getParola());
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

    public void setClassifica(String name, int ntentativi) {
        nomi.add(name);
        tentativi.add(ntentativi);
    }

    public String makeClassifica() {
        for (int j = 0; j < nomi.size() - 1; j++) {
            for (int i = 0; i < nomi.size() - 1; i++) {
                if (tentativi.get(i) > tentativi.get(i + 1)) {
                    Integer temp = tentativi.get(i);
                    tentativi.set(i, tentativi.get(i + 1));
                    tentativi.set(i + 1, temp);
                    String temp2 = nomi.get(i);
                    nomi.set(i, nomi.get(i + 1));
                    nomi.set(i + 1, temp2);
                }
            }
        }
        int k = 10;
        if (nomi.size() < 10) {
            k = nomi.size();
        }
        for (int i = 0; i < k; i++) {
            visualizzaClassifica += i + 1 + "." + nomi.get(i) + "/ tentativi: " + tentativi.get(i) + "\n";
            classifica[i] = nomi.get(i) + "-" + tentativi.get(i);
        }

        log(visualizzaClassifica);
        return visualizzaClassifica;
    }
    
    public String getClassifica() {
        return visualizzaClassifica;
    }

    public void read() {
        File file = new File("src/" + "classifica" + ".txt");

        try {
            Scanner s = new Scanner(file);
            while (s.hasNextLine()) {
                String line = s.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '-') {
                        nomi.add(line.substring(2, i));
                        tentativi.add(Integer.parseInt(line.substring(i + 1, line.length())));
                    }
                }
            }
            s.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parola.class.getName()).log(Level.SEVERE, null, ex);
        }
        makeClassifica();
        save(file);
    }

    private void save(File f) {
        ArrayList<String> list = new ArrayList<>();
        int contatore = 1;
        try {
            PrintWriter printwriter = new PrintWriter(f);
            for (String i : classifica) {
                if (i != null) {
                    i = contatore + "." + i;
                    contatore++;
                    printwriter.println(i);
                }
            }
            printwriter.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
