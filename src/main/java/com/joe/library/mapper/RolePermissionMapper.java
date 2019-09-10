package com.joe.library.mapper;

import com.joe.library.pojo.Role;
import com.joe.library.pojo.RolePermission;
import org.apache.ibatis.annotations.Select;

public interface RolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RolePermission record);

    int insertSelective(RolePermission record);

    RolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RolePermission record);


}