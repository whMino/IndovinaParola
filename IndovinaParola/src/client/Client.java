package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    Scanner scan;
    Socket socket = null;
    DataInputStream input = null;
    DataOutputStream output = null;
    InetAddress ip;
    Interfaccia interfaccia;

    public Client() {
        interfaccia = new Interfaccia(this);
        interfaccia.setVisible(true);

        try {
            ip = InetAddress.getByName("localhost");
            socket = new Socket(ip, Constants.PORT);

            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());

            scan = new Scanner(System.in);
        } catch (UnknownHostException ex) {
            log("Client : " + ex.getMessage());
        } catch (IOException ex) {
            log("Client : " + ex.getMessage());
        }

    }

    public static void main(String[] args) {
        Client client = new Client();
        client.readMessageThread();
        client.writeMessageThread();
    }

    private void readMessageThread() {
        Thread readMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        String msg = input.readUTF();
                        log(msg);
                        if(msg.substring(0,10).equals("Classifica"))
                            interfaccia.writeClassifica(msg);
                        else
                            interfaccia.writeError(msg);
                    } catch (IOException ex) {
                        return;
                    }

                }
            }
        });
        readMessage.start();
    }

    public void prova(String x) {
        String msg = x;

        try {
            output.writeUTF(msg);
        } catch (IOException ex) {
            log("writeMessageThread : " + ex.getMessage());
        }
    }

    private void writeMessageThread() {
        Thread sendMessage = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    String msg = scan.nextLine();

                    try {
                        output.writeUTF(msg);
                    } catch (IOException ex) {
                        log("writeMessageThread : " + ex.getMessage());
                    }
                }
            }
        });
        sendMessage.start();
    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
