package edu.school21.sockets.services;


import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessageRepository;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component("usersService")
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final MessageRepository messageRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

    @Autowired
    public UsersServiceImpl(@Qualifier("usersRepository") UsersRepository usersRepository,
                            @Qualifier("messageRepository") MessageRepository messageRepository) {
        this.usersRepository = usersRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public boolean signUp(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, encodedPassword);
        Optional<User> existingUser = usersRepository.findByUsername(username);

        if (existingUser.isEmpty()) {
            usersRepository.save(user);
            return true;
        }

        System.out.println("Username already exists");
        return false;
    }

    @Override
    public Long signIn(String username, String password) {
        Optional<User> existingUser = usersRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            String encodedPassword = user.getPassword();

            return passwordEncoder.matches(password, encodedPassword) ?
                    user.getId() : -1L;
        }

        return -1L;
    }

    @Override
    public void saveMessage(String username, String message) {
        Message msg = new Message(null, username, message, LocalDateTime.now());
        messageRepository.save(msg);
    }
}
