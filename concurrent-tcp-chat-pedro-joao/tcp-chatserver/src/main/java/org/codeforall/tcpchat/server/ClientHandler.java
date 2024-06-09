package org.codeforall.tcpchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class ClientHandler implements  Runnable{


    private BufferedReader input;
    private PrintWriter output;
    private Socket clientSocket;
    private String username;
    private LinkedList<ClientHandler> list;

    public ClientHandler(Socket clientSocket, LinkedList list) {
        this.clientSocket = clientSocket;
        this.list = list;

        username = "user " + list.size() + ": ";

        try {
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();

        }
    }



    @Override
    public void run() {
        System.out.println(username + " thread number " + Thread.currentThread().getName());
                String line = "";
            try {
                while ((line = input.readLine()) != null) {

                    System.out.println("Received from " + username + " " +line);

                    if (line.startsWith("quit")) {
                        System.exit(1);
                        sendQuit();
                        break;
                    } else if (line.startsWith("list")) {
                        sendClientList();
                    } else if (line.startsWith("name")) {
                        changeName();
                    } else if (line.startsWith("help")) {
                        System.out.println("test......");
                        sendHelpMenu();
                    } else if (line.startsWith("whisper")) {
                        sendWhisper();
                    }else {
                        sendMessageToAll(username + ": " + line);
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();

            } finally {
                closeResources();
            }

    }

    public  void sendMessage(String message) {
       output.println(message);
       output.flush();
    }

    public  void sendMessageToAll (String message) {

        for (ClientHandler c : list) {
            if(!c.equals(this)) {
                c.sendMessage(message);
            }
        }
    }

    private void sendQuit() {
        closeResources();
        output.println("Client disconnected");
    }

    private void sendClientList() {
        System.out.println("Connected Clients:");

        for (ClientHandler clientHandler : list) {
            clientSocket = clientHandler.getClientSocket();
            System.out.println("Client: " + clientSocket.getInetAddress());
        }
    }
    private void sendWhisper() {

    }

    private void changeName() {

    }
    private void sendHelpMenu() {

        output.println("Available commands:");
        output.println("list - list all connected clients");
        output.println("whisper - send a private message to a specific user");
        output.println("name <new_name> - change your displayed name");
        output.println("help - list all available commands");
    }
    public void closeResources() {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}

