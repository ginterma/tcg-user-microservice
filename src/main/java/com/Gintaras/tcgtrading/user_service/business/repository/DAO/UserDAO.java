package com.Gintaras.tcgtrading.user_service.business.repository.DAO;

import io.swagger.annotations.ApiModelProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Entity
@Table(name = "User")
@NoArgsConstructor
@AllArgsConstructor
public class UserDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "average_rating")
    private Double averageRating;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDAO userDAO = (UserDAO) o;
        return Objects.equals(id, userDAO.id) && Objects.equals(username, userDAO.username) && Objects.equals(email, userDAO.email) && Objects.equals(password, userDAO.password) && Objects.equals(averageRating, userDAO.averageRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, averageRating);
    }

    @Override
    public String toString() {
        return "UserDAO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }
}
