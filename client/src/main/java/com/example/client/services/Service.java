package com.example.client.services;

import com.example.client.out.Operations;

import java.io.IOException;

public class Service {

    private Operations server;

    public Service() throws IOException {
        server = new Operations();
    }

    public boolean checkUser(String userName, String passwd) throws Exception {
        return server.checkUser(userName, passwd);
    }

}
