package com.ted.aggredata.server.dao;

public class GatewayExistsException extends Exception {

    public GatewayExistsException(String gatewayId){
        super("Gateway already in database: " + gatewayId);
    }
}
