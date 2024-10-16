package edu.school21.sockets.services;

public interface UsersService {
    boolean signUp(String username, String password);
    Long signIn(String username, String password);
    void saveMessage(String username, String message);
}
