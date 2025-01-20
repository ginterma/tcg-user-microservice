package com.Gintaras.tcgtrading.user_service.ControllerTest;

import com.Gintaras.tcgtrading.user_service.business.repository.DAO.UserDAO;
import com.Gintaras.tcgtrading.user_service.business.service.UserService;
import com.Gintaras.tcgtrading.user_service.controller.UserController;
import com.Gintaras.tcgtrading.user_service.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final String USER_URI = "/api/v1/user";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    private User user;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp(){
        user = new User(1L, "username", "email@site.com", "password", 0.0);
        userDAO = new UserDAO(1L, "username", "email@site.com", "password", 0.0);
    }

    @Test
    public void saveUserTest_WhenUserExist() throws Exception {
        when(userService.saveUser(user)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(user));
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(USER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void saveUserTest_WhenUserNotExist() throws Exception {
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post(USER_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void updateUserTest_WhenUserExist() throws Exception {
        when(userService.saveUser(user)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(user));
        when(userService.getUserById(1L)).thenReturn(ResponseEntity.ok(user));
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(USER_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void updateUserTest_WhenUserNotExist() throws Exception {
        when(userService.getUserById(1L)).thenReturn(ResponseEntity.notFound().build());
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(USER_URI + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }
    @Test
    public void updateUserTest_WhenIdNotMatch() throws Exception {
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(USER_URI + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
    @Test
    public void updateUserTest_UserIsNull() throws Exception {
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders
                        .put(USER_URI + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonString(null))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void deleteUserTest_UserExists() throws Exception {
        Long userId = 1L;


        when(userService.deleteUserById(userId)).thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(MockMvcRequestBuilders.delete(USER_URI + "/" + userId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteUserTest_UserNotFound() throws Exception {
        Long userId = 1L;

        when(userService.deleteUserById(userId)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(MockMvcRequestBuilders.delete(USER_URI + "/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getUserTest_UserExists() throws Exception {

        when(userService.getUserById(1L)).thenReturn(ResponseEntity.ok(user));

        mockMvc.perform(MockMvcRequestBuilders.get(USER_URI + "/" + 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    public void getUserTest_UserNotExists() throws Exception {

        when(userService.getUserById(1L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(MockMvcRequestBuilders.get(USER_URI + "/" + 1L))
                .andExpect(status().isNotFound());
    }
    @Test
    public void getAllUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userService.getUsers()).thenReturn(ResponseEntity.ok(userList));

        mockMvc.perform(MockMvcRequestBuilders.get(USER_URI))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("username"));
    }



    @Test
    public void updateAverageRatingTest_UserExists() throws Exception {
        Long userId = 1L;
        double newRating = 4.5;
        User updatedUser = new User(userId, "username", "email@site.com", "password", newRating);
        when(userService.updateAverageRatingById(userId)).thenReturn(ResponseEntity.ok(updatedUser));

        mockMvc.perform(MockMvcRequestBuilders.put(USER_URI + "/average/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.averageRating").value(newRating));
    }

    @Test
    public void updateAverageRatingTest_UserNotFound() throws Exception {
        Long userId = 999L;

        when(userService.updateAverageRatingById(userId)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(MockMvcRequestBuilders.put(USER_URI + "/average/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateAverageRatingTest_BadRequest() throws Exception {
        Long userId = null;

        mockMvc.perform(MockMvcRequestBuilders.put(USER_URI + "/average/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static String JsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
