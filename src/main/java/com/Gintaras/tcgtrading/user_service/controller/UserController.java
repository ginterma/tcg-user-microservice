package com.Gintaras.tcgtrading.user_service.controller;

import com.Gintaras.tcgtrading.user_service.business.service.UserService;
import com.Gintaras.tcgtrading.user_service.model.User;
import com.Gintaras.tcgtrading.user_service.swagger.HTMLResponseMessages;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Log4j2
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    @ApiOperation(value = "Saves User to database",
            notes = "If valid User body is provided it is saved in the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> saveUser(@ApiParam(value = "User model that we want to save", required = true)
                               @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates User in database",
            notes = "If User exists with provided Id then it is updated according to provided body",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.ACCEPTED)
    ResponseEntity<?> updateUser(@ApiParam(value = "The id of the user", required = true)
                                 @PathVariable @NonNull Long id,
                                 @ApiParam(value = "The updating user model", required = true)
                                 @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        if (!Objects.equals(user.getId(), id)) {
            log.warn("Provided User ids are not equal: {}!={}", id, user.getId());
            return ResponseEntity.badRequest().body("Unsuccessful request responds with this code." +
                    "Passed data has errors - provided User ids are not equal.");
        }
        Optional<User> userById = userService.getUserById(id);
        if (userById.isEmpty()) {
            log.info("User with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
        User updatedUser = userService.saveUser(user);
        log.info("User with id {} is updated: {}", id, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes User in database",
            notes = "If User exists with provided Id then it is deleted from the database",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = HTMLResponseMessages.HTTP_204_WITH_DATA),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<?> deleteUserById(@ApiParam(value = "The id of the user", required = true)
                                     @PathVariable @NonNull Long id) {
        Optional<User> userById = userService.getUserById(id);
        if (userById.isEmpty()) {
            log.warn("User for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        userService.deleteUserById(id);
        log.info("User with id {} is deleted: {}", id, userById);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @ApiOperation(
            value = "Get a list of all Users",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> foundUsers = userService.getUsers();
        if (foundUsers.isEmpty()) {
            log.warn("User list is empty: {}", foundUsers);
            return ResponseEntity.notFound().build();
        } else {
            log.info("User list is: {}", foundUsers::size);
            return new ResponseEntity<>(foundUsers, HttpStatus.OK);
        }
    }

    @ApiOperation(
            value = "Get User object from database by Id",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)
    })
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/{id}")
    public ResponseEntity<?> getUserById(@ApiParam(value = "The id of the user", required = true)
                                         @PathVariable Long id) {
        Optional<User> userById = userService.getUserById(id);
        if (userById.isEmpty()) {
            log.info("User with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        } else {
            log.info("User with id {} is found: {}", id, userById);
            return ResponseEntity.ok(userById);
        }
    }


}
