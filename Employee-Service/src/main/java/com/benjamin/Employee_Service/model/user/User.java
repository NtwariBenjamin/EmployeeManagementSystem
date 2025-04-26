package com.benjamin.Employee_Service.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @JsonProperty
    private String username;

    @Column
    @JsonProperty
    private String password;

    @Column
    @JsonProperty
    @Enumerated(EnumType.STRING)
    private Role role;

}


