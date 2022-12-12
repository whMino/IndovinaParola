package indovinaparola;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {

    final Socket socket;
    final Scanner scan;
    private Main main;
    public int ntentaivi = 0;
    String name;
    boolean isLosggedIn;
    boolean isEnd = false;

    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Main main, Socket socket, String name) {
        this.main = main;
        this.socket = socket;
        scan = new Scanner(System.in);
        this.name = name;
        isLosggedIn = true;

        try {
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            log("ClientHander : " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        String received;
        write(output, "Your name : " + name);
        write(output, "Word Length : " + Main.parola.getParola().length());

        while (!isEnd) {
            received = read();
            if (received.equalsIgnoreCase(Constants.LOGOUT)) {
                this.isLosggedIn = false;
                closeSocket();
                closeStreams();
                break;
            }

            forwardToClient(received);
        }
        //closeStreams();
        while(true) {
            forwardToClient("");
        }
    }

    private void forwardToClient(String received) {
        if (!isEnd) {
            String message = received;
            if (message != "") {
                ntentaivi++;
            }

            String risultato = Main.controllo.cambia(message);
            if (risultato.equals(message)) {
                write(this.output, "Hai Vinto");
                setclassifica();
                for (ClientHandler c : Main.getClients()) {
                    if (c != this) {
                        write(c.output, "Il giocatore: " + name + " ha vinto (" + Main.parola.getParola() + ")");
                        //c.closeSocket();
                        //c.closeStreams();
                        c.setEnd();
                    }
                }
                log(name + " : " + message);
                log(name + " : HA VINTO indovinando la parola " + Main.parola.getParola());
                main.read();
                this.closeSocket();
                this.closeStreams();
            } else if (risultato.equals("Jolly")) {
                write(this.output, "Hai perso, la parola era: " + Main.parola.getParola());
                closeSocket();
                closeStreams();

                log(name + " : " + message);
                log(name + " : " + "ha utilizzato il jolly");

            } else {
                write(this.output, "gioco: " + risultato);
                if (message != "") {
                    log(name + " : " + message);
                }
            }
        }else {
            write(this.output,main.getClassifica());
        }

    }

    public void setclassifica() {
        main.setClassifica(name, ntentaivi);
    }

    public void makeclassifica() {
        main.makeClassifica();
    }

    private String read() {
        String line = "";
        try {
            line = input.readUTF();
        } catch (IOException ex) {
            //log("read : " + ex.getMessage());
        }
        return line;
    }

    private void write(DataOutputStream output, String message) {
        try {
            output.writeUTF(message);
        } catch (IOException ex) {
            //log("write : " + ex.getMessage());
        }
    }

    private void closeStreams() {
        try {
            this.input.close();
            this.output.close();
        } catch (IOException ex) {
            //log("closeStreams : " + ex.getMessage());
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException ex) {
            //log("closeSocket : " + ex.getMessage());
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }

    public void setEnd() {
        isEnd = true;
    }
}
