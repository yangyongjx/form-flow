package com.gyfish.formflow.dao;

import com.gyfish.formflow.domain.AppUser;
import com.gyfish.formflow.vo.UserQuery;

import java.util.List;

/**
 * 用户
 *
 * @author geyu
 */
public interface AppUserMapper {


    /**
     * 向应用中添加用户
     *
     * @param user 用户实体
     * @return 入库的数量
     */
    Integer insert(AppUser user);

    /**
     * 查询用户
     *
     * @param userQuery 查询参数
     * @return 用户列表
     */
    List<AppUser> userList(UserQuery userQuery);
}
