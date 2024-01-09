package com.driver;

public class GroupDoesNotExistException extends Exception{
    public GroupDoesNotExistException(String message){
        super(message);
    }
}
