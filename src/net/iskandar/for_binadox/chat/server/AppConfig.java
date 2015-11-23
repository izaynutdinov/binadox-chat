package net.iskandar.for_binadox.chat.server;

import java.util.Properties;

import javax.sql.DataSource;

import net.iskandar.for_binadox.chat.server.service.ChatService;
import net.iskandar.for_binadox.chat.server.service.impl.ChatServiceHibernateImpl;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        System.out.println("================================= CREATING LocalSessionFactoryBean =======================================");
        System.out.println("dataSource=" + dataSource().toString());
        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
        lsfb.setDataSource(dataSource());
        lsfb.setPackagesToScan(new String[]{"net.iskandar.for_binadox.chat.server.domain"});
        Properties props = new Properties();
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        lsfb.setHibernateProperties(props);
        return lsfb;
    }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager tm = new HibernateTransactionManager(sessionFactory);
        return tm;
    }

    @Bean
    public DataSource dataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost/chat");
        dataSource.setUsername("chat");
        dataSource.setPassword("qwerty");
        return dataSource;
    }
    
    @Bean
    public ChatService chatService(){
    	ChatServiceHibernateImpl chatService = new ChatServiceHibernateImpl();
    	return chatService;
    }
    
    @Bean
    public ChatFacadeImpl chatFacade(){
    	ChatFacadeImpl chatFacadeImpl = new ChatFacadeImpl();
    	chatFacadeImpl.setChatService(chatService());
    	return chatFacadeImpl;
    }

}
