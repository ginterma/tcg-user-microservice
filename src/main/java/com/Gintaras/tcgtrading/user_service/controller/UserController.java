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
    public ResponseEntity<?> saveUser(@ApiParam(value = "User model that we want to save", required = true)
                                      @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        return userService.saveUser(user);
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
    public ResponseEntity<?> updateUser(@ApiParam(value = "The id of the user", required = true)
                                        @PathVariable @NonNull Long id,
                                        @ApiParam(value = "The updating user model", required = true)
                                        @Valid @RequestBody User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            log.warn(HTMLResponseMessages.HTTP_400);
            return ResponseEntity.badRequest().body(HTMLResponseMessages.HTTP_400);
        }
        if (!Objects.equals(user.getId(), id)) {
            log.warn("Provided User ids are not equal: {}!={}", id, user.getId());
            return ResponseEntity.badRequest().body("Unsuccessful request: provided User ids are not equal.");
        }
        ResponseEntity<User> existingUserResponse = userService.getUserById(id);

        if (existingUserResponse.getStatusCode().is2xxSuccessful()) {
            User updatedUser = userService.saveUser(user).getBody();
            return ResponseEntity.ok(updatedUser);
        } else {
            log.info("User with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
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
    public ResponseEntity<?> deleteUserById(@ApiParam(value = "The id of the user", required = true)
                                            @PathVariable @NonNull Long id) {
        return userService.deleteUserById(id);
    }

    @GetMapping(produces = "application/json")
    @ApiOperation(value = "Get a list of all Users",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<List<User>> getAllUsers() {
        return userService.getUsers();
    }

    @GetMapping(produces = "application/json", path = "/{id}")
    @ApiOperation(value = "Get User object from database by Id",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> getUserById(@ApiParam(value = "The id of the user", required = true)
                                         @PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("average/{id}")
    @ApiOperation(value = "Updates User's avg rating in database",
            notes = "If User exists with provided Id then its average rating is updated",
            response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = HTMLResponseMessages.HTTP_200),
            @ApiResponse(code = 400, message = HTMLResponseMessages.HTTP_400),
            @ApiResponse(code = 404, message = HTMLResponseMessages.HTTP_404),
            @ApiResponse(code = 500, message = HTMLResponseMessages.HTTP_500)})
    public ResponseEntity<?> updateUsersAverageRating(@ApiParam(value = "The id of the user", required = true)
                                                      @PathVariable @NonNull Long id) {
        return userService.updateAverageRatingById(id);
    }
}
