package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.User;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO uc_user(name, password, enable, lock, expired) VALUES(#{name}, #{password}, #{enable}, #{lock}, #{expired})")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    void insert(User user);

    @Select("select * from uc_user where name=#{name}")
    User findUserByName(@Param("name") String name);
}
