package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.RoleUser;

import java.util.List;

@Mapper
public interface RoleUserMapper {
    @Insert("INSERT INTO uc_role_user(role_id, user_id) VALUES(#{roleId}, #{userId})")
    @Options(useGeneratedKeys = true)
    void insert(RoleUser roleUser);

    @Select("select * from uc_role_user where role_id=#{roleId} and user_id=#{userId}")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "userId", column = "user_id")
    })
    RoleUser findById(@Param("roleId") Integer roleId, @Param("userId") Integer userId);

    @Select("select * from uc_role_user where role_id=#{roleId}")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "userId", column = "user_id")
    })
    List<RoleUser> findByRoleId(@Param("roleId") Integer roleId);

    @Select("select * from uc_role_user where user_id=#{userId}")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "userId", column = "user_id")
    })
    List<RoleUser> findByUserId(@Param("userId") Integer userId);

    @Select("select * from uc_role_user where user_id in (${userId})")
    @Results({
            @Result(property = "roleId", column = "role_id"),
            @Result(property = "userId", column = "user_id")
    })
    List<RoleUser> findByUserIds(@Param("userId") String userId);

    @Delete("DELETE from uc_role_user where id in (${ids})")
    int delete(String ids);

    @Delete("DELETE from uc_role_user where user_id=#{id}")
    int deleteByUserId(Integer id);
}
