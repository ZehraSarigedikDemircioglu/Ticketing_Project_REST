package com.cydeo.controller;

import com.cydeo.annotation.ExecutionTime;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.UserDTO;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "UserController", description = "User API")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ExecutionTime
    @GetMapping()
    @RolesAllowed({"Manager", "Admin"})
    @Operation(summary = "Get users")
    public ResponseEntity<ResponseWrapper> getUsers() {
        return ResponseEntity.ok(new ResponseWrapper("Users are successfully retrieved", userService.listAllUsers(), HttpStatus.OK));
    }
    @ExecutionTime
    @GetMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Get user by username")
    public ResponseEntity<ResponseWrapper> getUserByUserName(@PathVariable("username") String userName) {
        return ResponseEntity.ok(new ResponseWrapper("User is retrieved", userService.findByUserName(userName), HttpStatus.OK));
    }

    @PostMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Create user")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("User is successfully created", HttpStatus.CREATED));
    }

    @PutMapping
    @RolesAllowed("Admin")
    @Operation(summary = "Update user")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(new ResponseWrapper("User is successfully updated", userService.update(userDTO), HttpStatus.OK));
    }

    @DeleteMapping("/{username}")
    @RolesAllowed("Admin")
    @Operation(summary = "Delete user")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable("username") String username) throws TicketingProjectException {
        userService.delete(username);
        return ResponseEntity.ok(new ResponseWrapper("User is successfully deleted", HttpStatus.OK));

    }
}
