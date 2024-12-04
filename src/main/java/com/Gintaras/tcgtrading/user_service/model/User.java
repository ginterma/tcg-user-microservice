package com.Gintaras.tcgtrading.user_service.model;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Min(value = 1, message = "Employee Id must be greater than 0")
    @ApiModelProperty(notes = "Unique id of employee")
    private Long id;

    @ApiModelProperty(notes = "username of the User")
    private String username;

    @ApiModelProperty(notes = "email of the User")
    private String email;

    @ApiModelProperty(notes = "password of the User")
    private String password;

    @ApiModelProperty(notes = "Average rating of the user Received for Rating service")
    private Double averageRating;
}
