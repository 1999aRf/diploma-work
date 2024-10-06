package ru.skypro.homework.exceptions;

public class InvalidPassword extends RuntimeException{
    public InvalidPassword(String message) {
        super(message);
    }
}
