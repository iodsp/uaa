package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.Role;
import org.iodsp.uaa.entity.RoleAuthority;

import java.util.List;

@Mapper
public interface RoleAuthorityMapper {
    @Insert("INSERT INTO uc_role_auth(role_id, auth_id) VALUES(#{roleId}, #{authId})")
    @Options(useGeneratedKeys = true)
    void insert(RoleAuthority roleAuthority);

    @Select("select * from uc_role_auth where role_id=#{roleId} and auth_id=#{authId}")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "authId", column = "auth_id")
    })
    RoleAuthority findById(@Param("roleId") Integer roleId, @Param("authId") Integer authId);

    @Select("select * from uc_role_auth where role_id=#{roleId}")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "authId", column = "auth_id")
    })
    List<RoleAuthority> findByRoleId(@Param("roleId") Integer roleId);

    @Select("select * from uc_role_auth where auth_id=#{authId}")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "authId", column = "auth_id")
    })
    List<RoleAuthority> findByAuthId(@Param("authId") Integer authId);

    @Select("select * from uc_role_auth where auth_id in (${authId})")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "authId", column = "auth_id")
    })
    List<RoleAuthority> findByAuthIds(@Param("authId") String authId);

    @Delete("DELETE from uc_role_auth where id in (${ids})")
    int delete(String ids);

    @Delete("DELETE from uc_role_auth where auth_id=#{id}")
    int deleteByAuthId(Integer id);
}
