package org.iodsp.uaa.service;

import org.iodsp.uaa.entity.User;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.exceptions.UserExceptionEnums;
import org.iodsp.uaa.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

@Service
public class UcUserSerivce implements UserDetailsService {

    final Logger logger = LoggerFactory.getLogger(UcUserSerivce.class);
    @Autowired
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userMapper.findUserByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("not found user name");
        }

        boolean userEnable = user.getEnable() == User.USER_ENABLE;
        boolean userNoExpired = user.getExpired() != User.USER_EXPIRED;
        boolean userNoLock = user.getLock() != User.USER_LOCKED;
        LinkedHashSet<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ALL"));
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(
                user.getName(), user.getPassword(),
                userEnable, userNoExpired,
                userNoExpired, userNoLock,
                authorities
        );
        return userDetail;
    }

    public void addUser(User user) {
        User hasUser = userMapper.findUserByName(user.getName());
        if (hasUser != null) {
            throw new ServiceException(UserExceptionEnums.HAS_USER_EXCEPTION);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        try {
            userMapper.insert(user);
        } catch (Throwable e) {
            throw new ServiceException(UserExceptionEnums.ADD_EXCEPTION);
        }
    }
}
