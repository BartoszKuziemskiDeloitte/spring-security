package com.example.springsecurity.user;

import com.example.springsecurity.user.dto.ChangeRolesDto;
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

    @PutMapping("/change-roles")
    public ResponseEntity<UserDto> changeRoles(@RequestBody ChangeRolesDto changeRolesDto) {
        return new ResponseEntity<>(userService.changeRoles(changeRolesDto), HttpStatus.OK);
    }

    @DeleteMapping("{username}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable String username) {
        return new ResponseEntity<>(userService.deleteUser(username),HttpStatus.OK);
    }

}
