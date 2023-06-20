package org.example.services;

import org.example.entity.User;
import org.example.repo.UserDBRepo;

import java.util.Properties;

public class Service {
    private Properties constring;
    private UserDBRepo userRepo;

    public Service(Properties properties) {
        constring = properties;
        this.userRepo = new UserDBRepo(constring);
    }

    public boolean checkUser(String userName, String passwd) {
        User user = userRepo.search(new User(userName, ""));
        if (user == null)
            return false;
        return user.getPasswd().equals(passwd);
    }
}
