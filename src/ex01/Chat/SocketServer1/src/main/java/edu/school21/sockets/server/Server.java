package edu.school21.sockets.server;


import edu.school21.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

@Component
public class Server {
    private final UsersService service;
    private ServerSocket serverSocket;
    private final Set<UserThread> threadList = new HashSet<>();

    @Autowired
    public Server(UsersService service) {
        this.service = service;
    }

    public UsersService getService() {
        return service;
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
        if (serverSocket == null) {
            return;
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                UserThread user = new UserThread(socket, this);
                threadList.add(user);
                user.start();
            } catch (IOException e) {
                return;
            }
        }
    }

    public synchronized void broadcast(String user, String message) {
        service.saveMessage(user, message);
        threadList.forEach(userThread -> userThread
                .sendMessage(String.format("%s: %s", user, message)));
    }

    public synchronized void removeUser(UserThread user) {
        threadList.remove(user);
    }

    public synchronized String signUp(String username, String password) {
        if (service.signUp(username, password)) {
            return "Successful!";
        } else {
            return "Failed";
        }
    }

    public synchronized Long signIn(String username, String password) {
        Long id = service.signIn(username, password);
        if (id == -1L) {
            return id;
        }

        long isConnected = threadList.stream().filter(u -> u.getId() == id).count();
        return isConnected == 0L ? id : -1L;
    }
}
