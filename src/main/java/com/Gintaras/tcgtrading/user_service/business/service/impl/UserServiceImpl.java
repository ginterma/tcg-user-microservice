package com.Gintaras.tcgtrading.user_service.business.service.impl;

import com.Gintaras.tcgtrading.user_service.business.mapper.UserMapper;
import com.Gintaras.tcgtrading.user_service.business.repository.DAO.UserDAO;
import com.Gintaras.tcgtrading.user_service.business.repository.UserRepository;
import com.Gintaras.tcgtrading.user_service.business.service.UserService;
import com.Gintaras.tcgtrading.user_service.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    private final RestClient restClient;

    public UserServiceImpl() {
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8081/api/v1/rating").build();
    }

    @Override
    public ResponseEntity<User> saveUser(User user){
        UserDAO userDAO = userRepository.save(userMapper.UserToUserDAO(user));
        log.info("New User is saved: {}", user);
        return ResponseEntity.ok(userMapper.UserDAOToUser(userDAO));
    }

    @Override
    public ResponseEntity<User> getUserById(Long id){
        Optional<User> user = userRepository.findById(id).map(userMapper::UserDAOToUser);
        if (user.isPresent()) {
            log.info("User with id {} is found: {}", id, user.get());
            return ResponseEntity.ok(user.get());
        } else {
            log.info("User with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<User>> getUsers(){
        List<UserDAO> userList = userRepository.findAll();
        log.info("Get user list. Size is: {}", userList.size());
        List<User> users = userList.stream().map(userMapper::UserDAOToUser).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<Void> deleteUserById(Long id){
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.info("User with id {} has been deleted", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("User with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<User> updateAverageRatingById(Long id){
        Optional<User> user = userRepository.findById(id).map(userMapper::UserDAOToUser);
        if (user.isEmpty()) {
            log.warn("User with id {} does not exist", id);
            return ResponseEntity.notFound().build();
        } else {
            Double userAvgRating = restClient.get().uri("/average/{id}", id)
                    .retrieve().body(Double.class);
            user.get().setAverageRating(userAvgRating);
            userRepository.save(userMapper.UserToUserDAO(user.get()));
            log.info("User with id {} average rating has been updated: {}", id, userAvgRating);
            return ResponseEntity.ok(user.get());
        }
    }
}
