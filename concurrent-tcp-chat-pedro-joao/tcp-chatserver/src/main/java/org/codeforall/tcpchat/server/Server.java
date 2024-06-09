package org.codeforall.tcpchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;


/**
 * accepts client connections
 * reads from client and sends to other clients
 */

public class Server {

    public static void main(String[] args) {
        Server server = new Server();

    }

    private ServerSocket serverSocket;
    private ClientHandler clientHandler;
    private LinkedList<ClientHandler> list;
    private Set<PrintWriter> clients = new HashSet<>();
    private static final int PORT = 8080;

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connection on port: " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        list = new LinkedList<>();
        //username = new LinkedList<>();
        init();

    }

    public void init() {
        System.out.println("Server started. Waiting for connections...");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); //
                System.out.println("Client connected: ");

                clientHandler = new ClientHandler(clientSocket, list);
                list.add(clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public  void sendMessageToAll (String message) {

        for (ClientHandler c : list) {
            if(!c.equals(this)) {
                c.sendMessage(message);
            }
        }
    }

}



