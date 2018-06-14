package me.demo.email.api;

import java.util.List;

public interface EmailApi {
    boolean login(String usr, String pwd);
    enum RegisterCode {
        SUCC,
        USR_HAS_REGISTERED,
        UNKNOWN_ERROR,
    }
    RegisterCode register(String usr, String pwd);
}
