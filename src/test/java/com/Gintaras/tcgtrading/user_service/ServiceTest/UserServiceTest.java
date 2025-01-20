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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RestClient restClient;

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

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
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
//    public void updateAverageRatingByIdTest_WhenUserExists(){
//        when(userRepository.findById(1L)).thenReturn(Optional.of(userDAO));
//
//        // Mock the RestClient behavior
//        // Mock the behavior of restClient.get() and subsequent calls
//        when(restClient.get()).thenReturn(mock(RestClient.RequestHeadersUriSpec.class)); // Mock the URI specification
//        when(restClient.get().uri("/average/{id}", 1L)).thenReturn(mock(RestClient.RequestHeadersUriSpec.class)); // Mock uri method
//        when(restClient.get().uri("/average/{id}", 1L).retrieve()).thenReturn(mock(RestClient.ResponseSpec.class)); // Mock retrieve()
//        when(restClient.get().uri("/average/{id}", 1L).retrieve().body(Double.class)).thenReturn(4.5); // Mock body() to return 4.5
//
//        ResponseEntity<?> responseEntity = userServiceImpl.updateAverageRatingById(1L);
//
//        verify(userRepository, times(1)).save(userDAO);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(4.5, user.getAverageRating()); // Check that the user's average rating is updated
//        assertEquals(user, responseEntity.getBody());
//    }

    @Test
    public void updateAverageRatingByIdTest_WhenUserNotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> responseEntity = userServiceImpl.updateAverageRatingById(1L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(userRepository, times(0)).save(any());
    }

}
