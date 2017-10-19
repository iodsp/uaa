package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.Authority;

import java.util.List;

@Mapper
public interface AuthorityMapper {
    @Insert("INSERT INTO uc_authority(name, desc) VALUES(#{name}, #{desc})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insert(Authority authority);

    @Update("UPDATE uc_authority set name=#{name}, desc=#{desc} where id=#{id}")
    int update(Authority authority);

    @Select("select * from uc_authority where id=#{id}")
    Authority findById(@Param("id") Integer id);

    @Select("select * from uc_authority where name=#{name}")
    Authority findByName(@Param("name") String name);

    @Select("select * from uc_authority")
    List<Authority> find();

    @Select("select * from uc_authority where id in (${ids})")
    List<Authority> findByIds(@Param("ids") String ids);

    @Delete("DELETE from uc_authority where id in (${ids})")
    int delete(@Param("ids") String ids);
}
