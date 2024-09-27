package ru.skypro.homework.exceptions;

public class InvalidCurrentPassword extends RuntimeException{
    public InvalidCurrentPassword(String message) {
        super(message);
    }
}
