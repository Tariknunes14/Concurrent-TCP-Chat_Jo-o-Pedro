package org.codeforall.tcpchat.client;

import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        Client client = new Client();
    }

    private Socket clientSocket = null;
    private PrintWriter output;
    private BufferedReader input;

    public Client() {

        init();
    }

    public void init() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        connectionToServer("localhost", 8080);

        messageToServer(in);

    }

    public void connectionToServer(String host, int port) {
        try {
            // Establish connection to the server
            clientSocket = new Socket(host, port);
            // Initialize output stream to write to the server
            output = new PrintWriter(clientSocket.getOutputStream(), true);
            // Initialize input stream to read from the server
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Start a new thread to handle incoming messages from the server
            Thread thread = new Thread(new ReadMessage());
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
            // Close resources if connection fails
            closeResources();
        }

    }

    private void messageToServer(BufferedReader in) {
        System.out.println("Enter messages to send (type 'quit' to exit):");

        try {
            String message;
            while ((message = in.readLine()) != null && !message.equalsIgnoreCase("quit")) {
                output.println(message);
                output.flush();
            }
        } catch (IOException e) {
            System.err.println("Error sending message to server: " + e.getMessage());
        } finally {
            // Close the output PrintWriter
            if (output != null) {
                output.close();
            }
        }
    }
    private class ReadMessage implements Runnable {
        @Override
        public void run() {
            try {
                String message;
                // Continuously read messages from the server
                while ((message = input.readLine()) != null) {

                    System.out.println("Received message from: " + message);

                }
            } catch (IOException e) {
                // Handle read errors
                System.err.println("Error reading from server: " + e.getMessage());
            } finally {
                // Close the input stream when done
                try {
                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                    System.err.println("Error closing input stream: " + e.getMessage());
                }
            }
        }


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
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

}





