package com.example.springsecurity.user;

import com.example.springsecurity.user.dto.AddRoleDto;
import com.example.springsecurity.user.dto.UserDto;
import com.example.springsecurity.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.registerUser(user), HttpStatus.OK);
    }

    @PutMapping("/add-role")
    public ResponseEntity<UserDto> addRole(@RequestBody AddRoleDto addRoleDto) {
        return new ResponseEntity<>(userService.addUserRole(addRoleDto), HttpStatus.OK);
    }

}
