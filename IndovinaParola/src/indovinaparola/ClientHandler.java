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
    String name;
    boolean isLosggedIn;

    private DataInputStream input;
    private DataOutputStream output;

    public ClientHandler(Socket socket, String name) {
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

        while (true) {
            received = read();
            if (received.equalsIgnoreCase(Constants.LOGOUT)) {
                this.isLosggedIn = false;
                closeSocket();
                closeStreams();
                break;
            }

            forwardToClient(received);
        }
        closeStreams();
    }

    private void forwardToClient(String received) {
        // username # message
        //StringTokenizer tokenizer = new StringTokenizer(received, "#");
        //String recipient = tokenizer.nextToken().trim();
        //String message = tokenizer.nextToken().trim();
        String message = received;
        //for (ClientHandler c : Main.getClients()) {
        //if (c.isLosggedIn && c.name.equals(recipient)) {
        //message = "(" + Main.parola.getParola() + ")" + message;
        //for (ClientHandler c : Main.getClients()) {
        String risultato = Main.controllo.cambia(message);
        if (risultato.equals(message)) {
            write(this.output, "Hai Vinto");
            for (ClientHandler c : Main.getClients()) {
                if (c != this) {
                    write(c.output, "Il giocatore: " + name + " ha vinto (" + Main.parola.getParola() + ")");
                    c.closeSocket();
                    c.closeStreams();
                }
            }
            log(name + " : ha indovinato la parola " + Main.parola.getParola());
            this.closeSocket();
            this.closeStreams();
        } else {
            write(this.output, name + " : " + risultato);
        }

        //}
        if (message != "") {
            log(name + " : " + message);
        } else {

        }
        // break;
        //}
        //}

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
}
