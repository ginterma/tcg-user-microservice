package com.Gintaras.tcgtrading.user_service.business.service.impl;

import com.Gintaras.tcgtrading.user_service.business.mapper.UserMapper;
import com.Gintaras.tcgtrading.user_service.business.repository.DAO.UserDAO;
import com.Gintaras.tcgtrading.user_service.business.repository.UserRepository;
import com.Gintaras.tcgtrading.user_service.business.service.UserService;
import com.Gintaras.tcgtrading.user_service.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

   private final UserMapper userMapper;
   private final UserRepository userRepository;
   private final WebClient webClient;

   public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, WebClient webClient){
       this.userMapper = userMapper;
       this.userRepository = userRepository;
       this.webClient = webClient;
   }

    @Override
    public ResponseEntity<User> saveUser(User user){
        UserDAO userDAO = userRepository.save(userMapper.UserToUserDAO(user));
        log.info("New User is saved: {}", user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.UserDAOToUser(userDAO));
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
            Double userAvgRating = webClient.get()
                    .uri("/average/{id}", id)
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();

            user.get().setAverageRating(userAvgRating);
            UserDAO responseUser = userRepository.save(userMapper.UserToUserDAO(user.get()));
            log.info("User with id {} average rating has been updated: {}", id, userAvgRating);
            return ResponseEntity.ok(userMapper.UserDAOToUser(responseUser));
        }
    }
}
