package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final Socket socket = new Socket();

    public boolean connect(int port) {
        try {
            socket.connect(new InetSocketAddress(port));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            String inputLine;
            String outputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);

                if (inputLine.equals("Successful!")) {
                    close();
                    break;
                } else if (inputLine.startsWith("ERROR")) {
                    break;
                }

                outputLine = scanner.nextLine();
                out.println(outputLine);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
