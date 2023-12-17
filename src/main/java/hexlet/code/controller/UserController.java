package hexlet.code.controller;

import hexlet.code.dto.user.UserUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

import hexlet.code.repository.UserRepository;

import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserCreateDTO;

import hexlet.code.mapper.UserMapper;

import hexlet.code.service.UserService;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public ResponseEntity<List<UserDTO>> index() {
        var  users = userService.getAllUsers();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @GetMapping(path = "/{id}")
    public UserDTO show(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userData) {
        return userService.create(userData);
    }

    @PutMapping(path = "/{id}")
    public UserDTO update(@PathVariable long id, @Valid @RequestBody UserUpdateDTO userData) {
        return userService.updateUser(id, userData);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        userService.deleteUser(id);
    }
}
