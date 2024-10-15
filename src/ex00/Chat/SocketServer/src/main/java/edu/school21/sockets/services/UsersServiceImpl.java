package edu.school21.sockets.services;


import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("usersService")
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(4);

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
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
}
