
import com.zaxxer.hikari.HikariDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author w00623538
 * @since 2023-03-02
 */

@Configuration
@EnableTransactionManagement
public class TransactionConfiguration {

    /**
     * 默认beanName为transactionManager
     * 如果其他名称，需要指定：@Transactional(transactionManager = "xxxxTransactionManager", rollbackFor = Exception.class)
     */
    @Bean
    @Qualifier("transactionManager")
    public DataSourceTransactionManager txManager(@Autowired HikariDataSource hikariDataSource) {
        return new DataSourceTransactionManager(hikariDataSource);
    }
}
