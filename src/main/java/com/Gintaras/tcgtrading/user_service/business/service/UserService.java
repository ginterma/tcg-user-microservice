package com.Gintaras.tcgtrading.user_service.business.service;

import com.Gintaras.tcgtrading.user_service.model.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<User> getUserById(Long id);

    ResponseEntity<User> saveUser(User user);

    ResponseEntity<Void> deleteUserById(Long id);

    ResponseEntity<List<User>> getUsers();

    ResponseEntity<User> updateAverageRatingById(Long id);
}
