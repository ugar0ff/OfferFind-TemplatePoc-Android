package com.offerfind.template.poc.core.api.strongloop;

/**
 * Created by ugar on 16.02.16.
 */
public class UserRepository extends com.strongloop.android.loopback.UserRepository<User> {

    public interface LoginCallback extends com.strongloop.android.loopback.UserRepository.LoginCallback<User> {
    }

    public UserRepository() {
        super("customer", null, User.class);
    }
}
