package com.Gintaras.tcgtrading.user_service.model;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
public class User {
    public @Min(value = 1, message = "User Id must be greater than 0") Long getId() {
        return id;
    }
    @Min(value = 1, message = "User Id must be greater than 0")
    @ApiModelProperty(notes = "Unique id of User")
    private Long id;

    @ApiModelProperty(notes = "username of the User")
    private String username;

    @ApiModelProperty(notes = "email of the User")
    private String email;

    @ApiModelProperty(notes = "password of the User")
    private String password;

    @ApiModelProperty(notes = "Average rating of the user Received for Rating service")
    private Double averageRating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(averageRating, user.averageRating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, averageRating);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", averageRating=" + averageRating +
                '}';
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(@Min(value = 1, message = "User Id must be greater than 0") Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Double getAverageRating() {
        return averageRating;
    }


}
