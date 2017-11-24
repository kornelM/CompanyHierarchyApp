package com.mycompany.exceptions;

public class TypeNotFoundException extends Exception {
    public TypeNotFoundException(String type) {
        super(type);
    }
}
