package com.snippet.spring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snippet.spring.dao.entity.User;
import com.snippet.spring.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author will
 * @since 2023-01-28 07:41:02
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> implements IService<User> {

}
