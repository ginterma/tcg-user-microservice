package com.Gintaras.tcgtrading.user_service.business.service;

import com.Gintaras.tcgtrading.user_service.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserById(Long id);

    User saveUser(User user);

    void deleteUserById(Long id);

    List<User> getUsers();

}
