package org.freeeed.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.net.URL;
import java.util.Properties;

public class HibernateUtil {

    private static volatile HibernateUtil mInstance;

    private HibernateUtil() {
    }

    public static HibernateUtil getInstance() {
        if (mInstance == null) {
            synchronized (HibernateUtil.class) {
                if (mInstance == null) {
                    mInstance = new HibernateUtil();
                }
            }
        }
        return mInstance;
    }

    public SessionFactory getSessionFactory() {
        Configuration config = new Configuration();
        Properties properties = new Properties();
        config.configure();
        properties.put("hibernate.connection.username", "root");
        properties.put("hibernate.connection.password", "");
        config.setProperties(properties);
        config.addURL(getClass().getClassLoader().getResource("hibernate.cfg.xml"));
        return config.buildSessionFactory();
    }
}
