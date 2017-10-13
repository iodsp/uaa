package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.Resource;

import java.util.List;

@Mapper
public interface ResourceMapper {
    @Insert("INSERT INTO oauth_resources(resource_id, name) VALUES(#{id}, #{name})")
    void insert(Resource resource);

    @Update("UPDATE oauth_resources set name=#{name} where resource_id=#{id}")
    int update(Resource resource);

    @Select("select * from oauth_resources where resource_id=#{id}")
    @Results({
            @Result(property = "id", column = "resource_id"),
            @Result(property = "name", column = "name")
    })
    Resource findResourceById(@Param("id") String id);

    @Select("select * from oauth_resources")
    @Results({
            @Result(property = "id", column = "resource_id"),
            @Result(property = "name", column = "name")
    })
    List<Resource> findResource();

    @Delete("DELETE from oauth_resources where resource_id=#{id}")
    int deleteResource(String id);
}
