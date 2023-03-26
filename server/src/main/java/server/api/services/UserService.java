package server.api.services;

import commons.User;
import org.springframework.stereotype.Service;
import server.database.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
