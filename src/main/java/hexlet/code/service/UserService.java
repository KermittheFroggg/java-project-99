package hexlet.code.service;

import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;

import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder encoder;


    public List<UserDTO> getAllUsers() {
        var users = userRepository.findAll();
        return users.stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO create(UserCreateDTO userData) {
        var user = userMapper.map(userData);
        user.setPasswordDigest(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.map(user);
    }

    public UserDTO getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow();
        return userMapper.map(user);
    }

    public UserDTO updateUser(Long id, UserUpdateDTO userData) {
        var user = userRepository.findById(id)
                .orElseThrow();

        userMapper.update(userData, user);
        user.setPasswordDigest(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void deleteUser(Long id) {
        userRepository.findById(id)
                .orElseThrow();
        userRepository.deleteById(id);
    }

    @Override
    public void createUser(UserDetails user) {
        var newUser = new User();
        newUser.setEmail(user.getUsername());
        newUser.setPasswordDigest(encoder.encode(user.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
    }

    @Override
    public boolean userExists(String username) {
        throw new UnsupportedOperationException("Unimplemented method 'userExists'");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
