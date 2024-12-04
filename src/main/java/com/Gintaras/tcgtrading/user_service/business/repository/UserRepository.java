package com.Gintaras.tcgtrading.user_service.business.repository;

import com.Gintaras.tcgtrading.user_service.business.repository.DAO.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserDAO, Long> {
}
