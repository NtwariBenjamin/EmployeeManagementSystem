package com.benjamin.Department_Service.exception;

import java.io.IOException;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(String message){
        super(message);
    }
}
