package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO uc_user(name, password, enable, lock, expired) VALUES(#{name}, #{password}, #{enable}, #{lock}, #{expired})")
    @Options(keyColumn = "id", keyProperty = "id", useGeneratedKeys = true)
    void insert(User user);

    @Update("UPDATE uc_user set name=#{name}, password=#{password}, enable=#{enable}, lock=#{lock}, expired=#{expired} where id=#{id}")
    int update(User user);

    @Select("select * from uc_user where name=#{name}")
    User findUserByName(@Param("name") String name);

    @Delete("DELETE from uc_user where id in (${ids})")
    int delete(@Param("ids") String ids);


    @Select("select * from uc_user")
    List<User> find();

    @Select("select * from uc_user where id=#{id}")
    User findById(@Param("id") Integer id);
}
