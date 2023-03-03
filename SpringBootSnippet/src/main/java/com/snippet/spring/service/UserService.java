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
    @Transactional(rollbackFor = Exception.class)
    public void insertOne() {
        PublicAlert publicAlert = new PublicAlert();
        publicAlert.setId(IdUtil.getSnowflakeNextId());
        publicAlert.setSite("xxxx");
        publicAlert.setApplicationId("xxxx");
        publicAlert.setServiceId("xxxx");
        publicAlert.setTenantId("xxxx");
        publicAlert.setPublicAlertName("xxx");
        publicAlert.setStartTime(LocalDateTime.now());
        publicAlert.setCategory("OS");
        publicAlertMapper.insert(publicAlert);
        int s = 1 / 0;
    }

    @Autowired
    DataSourceTransactionManager transactionManager;

    public void insertOneCode() {
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            PublicAlert publicAlert = new PublicAlert();
            publicAlert.setId(IdUtil.getSnowflakeNextId());
            publicAlert.setSite("xxxx");
            publicAlert.setApplicationId("xxxx");
            publicAlert.setServiceId("xxxx");
            publicAlert.setTenantId("xxxx");
            publicAlert.setPublicAlertName("xxx");
            publicAlert.setStartTime(LocalDateTime.now());
            publicAlert.setCategory("OS");
            publicAlertMapper.insert(publicAlert);
            // int s = 1 / 0;
            transactionManager.commit(transactionStatus);
        } catch (Exception e) {
            transactionManager.rollback(transactionStatus);
        }
    }
}
