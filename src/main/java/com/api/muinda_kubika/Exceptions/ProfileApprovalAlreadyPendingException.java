package com.api.muinda_kubika.Exceptions;

public class ProfileApprovalAlreadyPendingException extends RuntimeException{
    public  ProfileApprovalAlreadyPendingException(){
        super("Pedido de aprovação já esta em processamento");
    }
}
