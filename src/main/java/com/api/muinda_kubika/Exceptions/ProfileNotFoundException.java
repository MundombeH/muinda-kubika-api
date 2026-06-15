package com.api.muinda_kubika.Exceptions;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(){
        super("Perfil não econtrado");
    }

}
