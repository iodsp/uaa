package org.iodsp.uaa.service;

import org.iodsp.uaa.entity.*;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.exceptions.UserExceptionEnums;
import org.iodsp.uaa.mapper.RoleMapper;
import org.iodsp.uaa.mapper.RoleUserMapper;
import org.iodsp.uaa.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UcUserSerivce {

    final Logger logger = LoggerFactory.getLogger(UcUserSerivce.class);
    @Autowired
    UserMapper userMapper;

    @Autowired
    RoleUserMapper roleUserMapper;

    @Autowired
    RoleMapper roleMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void addUser(User user, List<Integer> roleIds) {
        org.iodsp.uaa.entity.User hasUser;
        hasUser = userMapper.findUserByName(user.getName());
        if (hasUser != null) {
            throw new ServiceException(UserExceptionEnums.HAS_USER_EXCEPTION);
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userMapper.insert(user);

        insertRoleUser(roleIds, user.getId());
    }

    public HashMap<Integer, org.iodsp.uaa.dto.User> list(List<User> users) {
        HashMap<Integer, org.iodsp.uaa.dto.User> result = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        users.forEach(user -> {
            org.iodsp.uaa.dto.User dtoUser = new org.iodsp.uaa.dto.User(user);
            result.put(user.getId(), dtoUser);
            ids.add(user.getId());
        });

        List<Integer> roleIds = new ArrayList<>();
        List<RoleUser> roleUsers = roleUserMapper.findByUserIds(StringUtils.collectionToCommaDelimitedString(ids));
        if (roleUsers == null) {
            return result;
        }

        roleUsers.forEach(ru -> {
            roleIds.add(ru.getRoleId());
            if (!result.containsKey(ru.getUserId())) {
                return;
            }
            org.iodsp.uaa.dto.User user = result.get(ru.getUserId());
            List<Integer> uroleIds = user.getRoleIds();
            if (uroleIds == null) {
                uroleIds = new ArrayList<>();
            }
            uroleIds.add(ru.getRoleId());
            user.setRoleIds(uroleIds);
            result.put(ru.getUserId(), user);
        });
        List<Role> roles = roleMapper.findByIds(StringUtils.collectionToCommaDelimitedString(roleIds));
        HashMap<Integer, Role> hashRoles = new HashMap<>();
        roles.forEach(r -> {
            hashRoles.put(r.getId(), r);
        });
        result.forEach((id, user) -> {
            List<Integer> uroleIds = user.getRoleIds();
            List<Role> uroles = (null == user.getRoles()) ? new ArrayList<>() : user.getRoles();
            if (uroleIds == null) {
                uroleIds = new ArrayList<>();
            }
            uroleIds.forEach(r -> {
                if (!hashRoles.containsKey(r)) {
                    return;
                }
                uroles.add(hashRoles.get(r));
            });
            user.setRoles(uroles);
            result.put(id, user);
        });
        return result;
    }

    @Transactional
    public void delete(List<Integer> ids) {
        ids.forEach(id -> {
            roleUserMapper.deleteByUserId(id);
        });

        userMapper.delete(StringUtils.collectionToCommaDelimitedString(ids));
    }

    public org.iodsp.uaa.dto.User info(Integer id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new ServiceException(UserExceptionEnums.NOSUCH_USER_EXCEPTION);
        }

        org.iodsp.uaa.dto.User result = new org.iodsp.uaa.dto.User(user);
        List<Integer> roleIds = new ArrayList<>();
        List<RoleUser> roleUsers = roleUserMapper.findByUserId(id);
        roleUsers.forEach(role -> {
            roleIds.add(role.getRoleId());
        });
        List<Role> roles = roleMapper.findByIds(StringUtils.collectionToCommaDelimitedString(roleIds));
        result.setRoleIds(roleIds);
        result.setRoles(roles);
        return result;
    }

    @Transactional
    public void update(User user, List<Integer> roleIds) {
        User hasUser = userMapper.findById(user.getId());
        if (hasUser == null) {
            throw new ServiceException(UserExceptionEnums.NOSUCH_USER_EXCEPTION);
        }

        roleUserMapper.deleteByUserId(user.getId());
        insertRoleUser(roleIds, user.getId());

        if (!user.getPassword().equals(hasUser.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userMapper.update(user);
    }

    protected void insertRoleUser(List<Integer> roleIds, Integer userId) {
        roleIds.forEach(roleId -> {
            RoleUser hasRoleUser = roleUserMapper.findById(roleId, userId);
            if (hasRoleUser != null) {
                throw new ServiceException(UserExceptionEnums.PARAM_EXCEPTION);
            }
            roleUserMapper.insert(new RoleUser(roleId, userId));
        });
    }
}
