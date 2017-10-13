package org.iodsp.uaa.service;

import org.iodsp.uaa.entity.Authority;
import org.iodsp.uaa.entity.Role;
import org.iodsp.uaa.entity.RoleAuthority;
import org.iodsp.uaa.exceptions.AuthorityExceptionEnums;
import org.iodsp.uaa.exceptions.ServiceException;
import org.iodsp.uaa.mapper.AuthorityMapper;
import org.iodsp.uaa.mapper.RoleAuthorityMapper;
import org.iodsp.uaa.mapper.RoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UcAuthoritySerivce {

    final Logger logger = LoggerFactory.getLogger(UcAuthoritySerivce.class);
    @Autowired
    AuthorityMapper authorityMapper;

    @Autowired
    RoleAuthorityMapper roleAuthorityMapper;

    @Autowired
    RoleMapper roleMapper;

    @Transactional
    public void addAuthority(Authority authority, List<Integer> roleIds) {
        org.iodsp.uaa.entity.Authority hasAuthority;
        hasAuthority = authorityMapper.findByName(authority.getName());
        if (hasAuthority != null) {
            throw new ServiceException(AuthorityExceptionEnums.HAS_EXCEPTION);
        }
        authorityMapper.insert(authority);

        insertRoleAuthority(roleIds, authority.getId());
    }

    @Transactional
    public void updateAuthority(Authority authority, List<Integer> roleIds) {
        Authority hasAuthority = authorityMapper.findById(authority.getId());
        if (hasAuthority == null) {
            throw new ServiceException(AuthorityExceptionEnums.NOSUCH_EXCEPTION);
        }

        roleAuthorityMapper.deleteByAuthId(authority.getId());
        insertRoleAuthority(roleIds, authority.getId());

        authorityMapper.update(authority);
    }

    public org.iodsp.uaa.dto.Authority info(Integer id) {
        Authority authority = authorityMapper.findById(id);
        if (authority == null) {
            throw new ServiceException(AuthorityExceptionEnums.NOSUCH_EXCEPTION);
        }

        org.iodsp.uaa.dto.Authority result = new org.iodsp.uaa.dto.Authority();
        result.setDesc(authority.getDesc());
        result.setName(authority.getName());
        result.setId(authority.getId());
        List<Integer> roleIds = new ArrayList<>();
        List<RoleAuthority> roleAuthorities = roleAuthorityMapper.findByAuthId(id);
        roleAuthorities.forEach(role -> {
           roleIds.add(role.getRoleId());
        });
        List<Role> roles = roleMapper.findByIds(StringUtils.collectionToCommaDelimitedString(roleIds));
        result.setRoleIds(roleIds);
        result.setRoles(roles);
        return result;
    }

    public HashMap<Integer, org.iodsp.uaa.dto.Authority> list(List<Authority> authorities) {
        HashMap<Integer, org.iodsp.uaa.dto.Authority> result = new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        authorities.forEach(auth -> {
            org.iodsp.uaa.dto.Authority authority = new org.iodsp.uaa.dto.Authority();
            authority.setId(auth.getId());
            authority.setName(auth.getName());
            authority.setDesc(auth.getDesc());
            result.put(auth.getId(), authority);
            ids.add(auth.getId());
        });

        List<Integer> roleIds = new ArrayList<>();
        List<RoleAuthority> roleAuthorities = roleAuthorityMapper.findByAuthIds(StringUtils.collectionToCommaDelimitedString(ids));
        if (roleAuthorities == null) {
            return result;
        }

        roleAuthorities.forEach(ra -> {
            roleIds.add(ra.getRoleId());
            if (!result.containsKey(ra.getAuthId())) {
                return;
            }
            org.iodsp.uaa.dto.Authority authority = result.get(ra.getAuthId());
            List<Integer> aroleIds = authority.getRoleIds();
            if (aroleIds == null) {
                aroleIds = new ArrayList<Integer>();
            }
            aroleIds.add(ra.getRoleId());
            authority.setRoleIds(aroleIds);
            result.put(ra.getAuthId(), authority);
        });
        List<Role> roles = roleMapper.findByIds(StringUtils.collectionToCommaDelimitedString(roleIds));
        HashMap<Integer, Role> hashRoles = new HashMap<>();
        roles.forEach(r -> {
            hashRoles.put(r.getId(), r);
        });
        result.forEach((id, auth) -> {
            List<Integer> aroleIds = auth.getRoleIds();
            List<Role> aroles = (null == auth.getRoles()) ? new ArrayList<Role>() : auth.getRoles();
            if (aroleIds == null) {
                aroleIds = new ArrayList<Integer>();
            }
            aroleIds.forEach(r -> {
                if (!hashRoles.containsKey(r)) {
                    return;
                }
                aroles.add(hashRoles.get(r));
            });
            auth.setRoles(aroles);
            result.put(id, auth);
        });
        return result;
    }

    @Transactional
    public void delete(List<Integer> ids) {
        ids.forEach(id -> {
            roleAuthorityMapper.deleteByAuthId(id);
        });

        authorityMapper.delete(StringUtils.collectionToCommaDelimitedString(ids));
    }

    protected void insertRoleAuthority(List<Integer> roleIds, Integer authId) {
        roleIds.forEach(roleId -> {
            RoleAuthority hasRoleAuthority = roleAuthorityMapper.findById(roleId, authId);
            if (hasRoleAuthority != null) {
                throw new ServiceException(AuthorityExceptionEnums.PARAM_EXCEPTION);
            }
            roleAuthorityMapper.insert(new RoleAuthority(roleId, authId));
        });
    }
}
