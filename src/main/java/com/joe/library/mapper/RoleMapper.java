package com.joe.library.mapper;

import com.joe.library.pojo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);


    @Select("select id,role_name,remarks from role r where id in (select role_id from user_role where user_id = #{id})")
    List<Role> selectByUserId(Integer id);
}