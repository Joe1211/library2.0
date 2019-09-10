package com.joe.library.mapper;

import com.joe.library.pojo.Permission;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    //根据角色id查权限
    @Select("select id,permission_name,remarks from permission where id in (select permission_id from role_permission where role_id=#{id})")
    List<Permission> selectByRoleId(Integer id);
}