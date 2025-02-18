package com.Gintaras.tcgtrading.user_service.ServiceTest;

import com.Gintaras.tcgtrading.user_service.business.mapper.UserMapper;
import com.Gintaras.tcgtrading.user_service.business.repository.DAO.UserDAO;
import com.Gintaras.tcgtrading.user_service.business.repository.UserRepository;
import com.Gintaras.tcgtrading.user_service.business.service.impl.UserServiceImpl;
import com.Gintaras.tcgtrading.user_service.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private UserDAO userDAO;


    @BeforeEach
    public void setUp() {
        user = new User(1L, "username", "email@site.com", "password", 4.5);
        userDAO = new UserDAO(1L, "username", "email@site.com", "password", 4.5);
    }

    @Test
    public void saveUserTest() {
        when(userRepository.save(userDAO)).thenReturn(userDAO);
        when(userMapper.UserToUserDAO(user)).thenReturn(userDAO);
        when(userMapper.UserDAOToUser(userDAO)).thenReturn(user);

        ResponseEntity<?> responseEntity = userServiceImpl.saveUser(user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        verify(userRepository, times(1)).save(userDAO);
    }

    @Test
    public void getUserByIdTest_IdValid() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
        when(userMapper.UserDAOToUser(userDAO)).thenReturn(user);

        ResponseEntity<?> responseEntity = userServiceImpl.getUserById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(user, responseEntity.getBody());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void getUserByIdTest_IdInvalid() {
        when(userRepository.findById(0L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = userServiceImpl.getUserById(0L);


        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(userRepository, times(1)).findById(0L);
    }

    @Test
    public void getUsersTest() {
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
    public void deleteUserTest_WhenIdExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        ResponseEntity<?> responseEntity = userServiceImpl.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void deleteUserTest_WhenIdNotExists() {
        when(userRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<?> responseEntity = userServiceImpl.deleteUserById(1L);

        verify(userRepository, times(0)).deleteById(1L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void updateAverageRatingById_success() throws Exception {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userDAO));
        when(userMapper.UserDAOToUser(userDAO)).thenReturn(user);
        when(userMapper.UserToUserDAO(user)).thenReturn(userDAO);
        when(userRepository.save(userDAO)).thenReturn(userDAO);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/average/{id}", userId)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Double.class)).thenReturn(Mono.just(6.0));


        ResponseEntity<User> responseEntity = userServiceImpl.updateAverageRatingById(userId);
        User newUser = responseEntity.getBody();

        assertEquals(userId, responseEntity.getBody().getId());
        assertEquals(6.0, responseEntity.getBody().getAverageRating());
    }

}
