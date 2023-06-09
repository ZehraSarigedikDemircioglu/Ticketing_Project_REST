package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @RolesAllowed({"Manager","Admin"})
    public ResponseEntity<ResponseWrapper> getUsers() {
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userService.listAllUsers(), HttpStatus.OK));
    }

    @GetMapping("/{username}")
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String userName) {
        return ResponseEntity.ok(new ResponseWrapper("User is retrieved", userService.findByUserName(userName), HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User is successfully created", HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(new ResponseWrapper("User is successfully updated", userService.update(userDTO), HttpStatus.OK));
    }

    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) {
        userService.delete(username);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully deleted", HttpStatus.OK));

    }
}
