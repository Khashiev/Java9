package edu.school21.sockets.server;


import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Component
public class Server {
    private ServerSocket serverSocket;
    private UsersService usersService;

    @Autowired
    public Server(UsersService usersService) {
        this.usersService = usersService;
    }

    public boolean listen(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void run() {
        while (true) {
            try (Socket socket = serverSocket.accept();
                 InputStreamReader stream = new InputStreamReader(socket.getInputStream());
                 BufferedReader in = new BufferedReader(stream);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);) {

                out.println("Hello from server");
                String line = in.readLine();
                if (line == null) {
                    break;
                }

                if (!line.equals("signUp")) {
                    out.println("ERROR: Unknown command");
                    continue;
                }

                out.println("Enter username");
                String username = in.readLine();
                if (username == null) {
                    continue;
                }

                out.println("Enter password");
                String password = in.readLine();
                if (password == null) {
                    continue;
                }

                if (usersService.signUp(username, password)) {
                    out.println("Successful!");
                } else {
                    out.println("ERROR: Username or password is incorrect");
                }
            } catch (IOException e) {
                return;
            }
        }
    }
}
