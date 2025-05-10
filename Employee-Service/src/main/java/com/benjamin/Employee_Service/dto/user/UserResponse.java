package com.benjamin.Employee_Service.model.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private User user;
    private String message;
}
