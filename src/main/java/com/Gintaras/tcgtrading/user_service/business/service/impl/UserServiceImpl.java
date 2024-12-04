package com.Gintaras.tcgtrading.user_service.business.service.impl;

import com.Gintaras.tcgtrading.user_service.business.mapper.UserMapper;
import com.Gintaras.tcgtrading.user_service.business.repository.DAO.UserDAO;
import com.Gintaras.tcgtrading.user_service.business.repository.UserRepository;
import com.Gintaras.tcgtrading.user_service.business.service.UserService;
import com.Gintaras.tcgtrading.user_service.model.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public User saveUser(User user){
        UserDAO userDAO = userRepository.save(userMapper.UserToUserDAO(user));
        log.info("New User is saved: {}", user);
        return userMapper.UserDAOToUser(userDAO);
    }
    @Override
    public Optional<User> getUserById(Long id){
        Optional<User> user = userRepository.findById(id).map(userMapper::UserDAOToUser);
        log.info("User with id {} is {}", id, user.isPresent() ? user.get() : "not found");
        return user;
    }
    @Override
    public List<User> getUsers(){
        List<UserDAO> userList = userRepository.findAll();
        log.info("Get user list. Size is: {}", userList::size);
        return userList.stream().map(userMapper::UserDAOToUser).collect(Collectors.toList());
    }

    @Override
    public void deleteUserById(Long id){
        userRepository.deleteById(id);
        log.info("User with id {} has been deleted", id);
    }
}
