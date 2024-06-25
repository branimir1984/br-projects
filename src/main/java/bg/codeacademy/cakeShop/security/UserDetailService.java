package bg.academy.library.security;

import bg.academy.library.exception_handling.exceptions.LockedException;
import bg.academy.library.exception_handling.exceptions.UserNotFoundException;
import bg.academy.library.model.User;
import bg.academy.library.repository.UserRepository;
import bg.academy.library.services.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.getUserByName(username);
        if (user == null) {
            logger.debug("User with user name:" + username + " try to login, but can not " +
                    " because not have registration!");
            throw new UserNotFoundException("Username not found : " + username);
        }
        if (user.isLocked()) {
            logger.debug("User with user name:" + user.getUserName() + " try to login, but can not " +
                    " because is locked!");
            throw new LockedException(username + " is locked!");
        }
        return new AuthenticatedUser(user);
    }
}
