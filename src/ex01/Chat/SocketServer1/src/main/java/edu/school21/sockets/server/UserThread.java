package edu.school21.sockets.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class UserThread extends Thread {
    private final Socket socket;
    private final Server server;
    private PrintWriter out;
    private State state = State.Initial;
    private Long id = -1L;
    private String username = null;
    private String password = null;

    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public Long getUserId() {
        return id;
    }

    public void run() {
        try (InputStreamReader stream = new InputStreamReader(socket.getInputStream());
             BufferedReader in = new BufferedReader(stream);) {
            out = new PrintWriter(socket.getOutputStream(), true);

            String inputLine = null;
            String outputLine = null;

            out.println("Hello from server");

            while ((inputLine = in.readLine()) != null) {
                if (state == State.Initial) {
                    outputLine = processCommand(inputLine);
                } else if (state == State.SignUp || state == State.SignIn) {
                    outputLine = processSign(inputLine);
                } else if (inputLine.equals("Exit")) {
                    state = State.Exit;
                    outputLine = "You have left the chat.";
                }

                if (outputLine != null) {
                    out.println(outputLine);
                    outputLine = null;
                } else {
                    server.broadcast(username, inputLine);
                }

                if (state == State.Exit) {
                    break;
                }
            }
            server.removeUser(this);
            socket.close();
        } catch (Exception e) {
            server.removeUser(this);
        }
    }

    public void sendMessage(String message) {
        if (state == State.Talk) {
            out.println(message);
        }
    }

    private String processCommand(String command) {
        switch (command) {
            case "signUp":
                state = State.SignUp;
                return "Enter your username";
            case "signIn":
                state = State.SignIn;
                return "Enter your username";
            case "Exit":
                return "You have left the chat.";
            default:
                return "ERROR: Unknown command";
        }
    }

    private String processSign(String inputLine) {
        if (username == null) {
            username = inputLine;
            return "Enter password:";
        } else if (password == null) {
            password = inputLine;
            switch (state) {
                case SignUp:
                    String output = server.signUp(username, password);
                    username = null;
                    password = null;
                    state = State.Initial;
                    return output;
                case SignIn:
                    id = server.signIn(username, password);
                    if (id == -1) {
                        return "ERROR: Failed to Sign In";
                    } else {
                        state = State.Talk;
                        return "Start messaging";
                    }
                default:
                    return "ERROR: Unexpected error";
            }
        } else {
            return "ERROR: Unexpected error";
        }
    }


    enum State {
        Initial,
        SignUp,
        SignIn,
        Talk,
        Exit
    }
}
