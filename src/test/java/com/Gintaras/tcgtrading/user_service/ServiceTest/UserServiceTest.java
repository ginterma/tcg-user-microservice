package com.Gintaras.tcgtrading.user_service.ServiceTest;

import com.Gintaras.tcgtrading.user_service.business.mapper.UserMapper;
import com.Gintaras.tcgtrading.user_service.business.repository.DAO.UserDAO;
import com.Gintaras.tcgtrading.user_service.business.repository.UserRepository;
import com.Gintaras.tcgtrading.user_service.business.service.UserService;
import com.Gintaras.tcgtrading.user_service.business.service.impl.UserServiceImpl;
import com.Gintaras.tcgtrading.user_service.model.User;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClient;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDAO userDAO;

    @BeforeEach
    public void setUp(){
        user = new User(1L, "username", "email@site.com", "password", 0.0);
        userDAO = new UserDAO(1L, "username", "email@site.com", "password", 0.0);
    }



    @Test
    public void saveUserTest(){
        when(userRepository.save(userDAO)).thenReturn(userDAO);
        when(userMapper.UserToUserDAO(user)).thenReturn(userDAO);
        when(userMapper.UserDAOToUser(userDAO)).thenReturn(user);

        ResponseEntity<?> responseEntity = userServiceImpl.saveUser(user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        verify(userRepository, times(1)).save(userDAO);
    }

    @Test
    public void getUserByIdTest_IdValid(){
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
        when(userMapper.UserDAOToUser(userDAO)).thenReturn(user);

        ResponseEntity<?> responseEntity = userServiceImpl.getUserById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getUserByIdTest_IdInvalid(){
        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = userServiceImpl.getUserById(0L);


        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(userRepository, times(1)).findById(0L);
    }

    @Test
    public void getUsersTest(){
        List<UserDAO> userDAOList = new ArrayList<>();
        userDAOList.add(userDAO);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userDAOList);
        when(userMapper.UserDAOToUser(userDAO)).thenReturn(user);

        ResponseEntity<List<User>> responseEntity = userServiceImpl.getUsers();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userList, responseEntity.getBody());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void deleteUserTest_WhenIdExists(){
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        ResponseEntity<?> responseEntity = userServiceImpl.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void deleteUserTest_WhenIdNotExists(){
        when(userRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<?> responseEntity = userServiceImpl.deleteUserById(1L);

        verify(userRepository, times(0)).deleteById(1L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

//    @Test
//    void updateAverageRatingById_success() throws Exception {
//        Long userId = 1L;
//        Double mockRating = 4.5;
//        wireMockServer.stubFor(WireMock.get(urlEqualTo("/average/1L"))
//                .willReturn(aResponse()
//                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//                        .withBody("""
//                                {
//                                4.5
//                                }""")));
//
//        user.setAverageRating(mockRating);
//        userDAO.setAverageRating(mockRating);
//        when(userRepository.findById(userId)).thenReturn(Optional.of(userDAO));
//        when(userRepository.save(any(UserDAO.class))).thenReturn(userDAO);
//
//
//        ResponseEntity<User> responseEntity = userServiceImpl.updateAverageRatingById(userId);
//        User newUser = responseEntity.getBody();
//
//        assertEquals(userId, newUser.getId());
//        assertEquals(mockRating, newUser.getAverageRating());
//    }

//    @Test
//    void updateAverageRatingById_userNotFound() throws Exception {
//        Long userId = 1L;
//
//        // Mock the case when the user is not found in the repository
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        // Call the service method and verify the response
//        mockMvc.perform(put("/users/{id}/rating", userId))
//                .andExpect(status().isNotFound());
//
//        // Ensure no request was made to the external service since the user wasn't found
//        verify(0, getRequestedFor(urlPathEqualTo("/average/" + userId)));
//    }

}
