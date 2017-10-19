package org.iodsp.uaa.service;

import org.iodsp.uaa.entity.Authority;
import org.iodsp.uaa.entity.RoleAuthority;
import org.iodsp.uaa.entity.RoleUser;
import org.iodsp.uaa.entity.User;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.exceptions.UserExceptionEnums;
import org.iodsp.uaa.mapper.*;
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
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class UcUserSerivceOauth implements UserDetailsService {

    final Logger logger = LoggerFactory.getLogger(UcUserSerivceOauth.class);
    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleUserMapper roleUserMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UcUserSerivce ucUserSerivce;

    @Autowired
    RoleAuthorityMapper roleAuthorityMapper;

    @Autowired
    AuthorityMapper authorityMapper;

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

        List<User> users = new ArrayList<>();
        users.add(user);

        HashMap<Integer, org.iodsp.uaa.dto.User> userInfos = ucUserSerivce.list(users);
        if (userInfos.containsKey(user.getId())) {
            List<Integer> roleIds = userInfos.get(user.getId()).getRoleIds();
            List<RoleAuthority> roleAuthorities = roleAuthorityMapper.findByRoleIds(
                    StringUtils.collectionToCommaDelimitedString(roleIds)
            );
            List<Integer> authIds = new ArrayList<>();
            roleAuthorities.forEach((item) -> {
                authIds.add(item.getAuthId());
            });
            List<Authority> auths = authorityMapper.findByIds(
                    StringUtils.collectionToCommaDelimitedString(authIds)
            );

            auths.forEach(auth -> {
                authorities.add(new SimpleGrantedAuthority(auth.getName()));
            });
        }

        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(
                user.getName(), user.getPassword(),
                userEnable, userNoExpired,
                userNoExpired, userNoLock,
                authorities
        );
        return userDetail;
    }
}
