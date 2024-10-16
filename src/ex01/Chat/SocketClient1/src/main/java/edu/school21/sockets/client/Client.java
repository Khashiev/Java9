package edu.school21.sockets.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
    private final Socket socket = new Socket();

    public boolean connect(int port) {
        try {
            socket.connect(new InetSocketAddress(port));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void run() {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
            Thread printerThread = new Thread(() -> {
                String inputLine;

                try {
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        if (inputLine.startsWith("ERROR")) {
                            break;
                        } else if (inputLine.equals("You have left the chat.")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.printf("ERROR: %s\n", e.getMessage());
                }
            });

            Thread readerThread = new Thread(() -> {
                String inputLine;

                try {
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);
                        if (inputLine.startsWith("ERROR")) {
                            break;
                        } else if (inputLine.equals("You have left the chat.")) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    System.out.printf("ERROR: %s\n", e.getMessage());
                }
            });

            printerThread.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (printerThread.isAlive()) {
                if (reader.ready()) {
                    out.println(reader.readLine());
                }
            }

        } catch (IOException e) {
            System.out.printf("ERROR: %s\n", e.getMessage());
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (Exception e) {
            return;
        }
    }
}
