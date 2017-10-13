package org.iodsp.uaa.mapper;

import org.apache.ibatis.annotations.*;
import org.iodsp.uaa.entity.Resource;
import org.iodsp.uaa.entity.Scope;

import java.util.List;

@Mapper
public interface ScopeMapper {
    @Insert("INSERT INTO oauth_scopes(scope_id, name) VALUES(#{id}, #{name})")
    void insert(Scope scope);

    @Update("UPDATE oauth_scopes set name=#{name} where scope_id=#{id}")
    int update(Scope scope);

    @Select("select * from oauth_scopes where scope_id=#{id}")
    @Results({
            @Result(property = "id", column = "scope_id"),
            @Result(property = "name", column = "name")
    })
    Scope findScopeById(@Param("id") String id);

    @Select("select * from oauth_scopes")
    @Results({
            @Result(property = "id", column = "scope_id"),
            @Result(property = "name", column = "name")
    })
    List<Scope> findScope();

    @Delete("DELETE from oauth_scopes where scope_id=#{id}")
    int deleteScope(String id);
}
