package com.example.task_management_system_rest.security;

import com.example.task_management_system_rest.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;
@Getter
public class CurrentUser extends org.springframework.security.core.userdetails.User{
    private User user;

    public CurrentUser(User user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
