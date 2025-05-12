package com.storesync.app.exception;

import jakarta.mail.FetchProfile;

public class ItemNotFoundException extends RuntimeException{

    public ItemNotFoundException(String message){

        super(message);
    }
}
