package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.Role;

import java.util.List;

@Mapper
public interface RoleMapper {
    @Insert("INSERT INTO uc_role(name, desc) VALUES(#{name}, #{desc})")
    @Options(useGeneratedKeys = true)
    void insert(Role role);

    @Update("UPDATE uc_role set name=#{name}, desc=#{desc} where id=#{id}")
    int update(Role role);

    @Select("select * from uc_role where id=#{id}")
    Role findById(@Param("id") Integer id);

    @Select("select * from uc_role where name=#{name}")
    Role findByName(@Param("name") String name);

    @Select("select * from uc_role")
    List<Role> find();

    @Select("select * from uc_role where id in (${ids})")
    List<Role> findByIds(@Param("ids") String ids);

    @Delete("DELETE from uc_role where id=#{id}")
    int delete(String id);
}
