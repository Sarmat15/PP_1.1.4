package jm.task.core.jdbc.util;
import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";

    private static Util instance;

    private Util() {
    }

    public static Util getInstance() {
        if (instance == null) {
            instance = new Util();
        }
        return instance;
    }

    private static SessionFactory sessionFactory;
    private static final Environment environment = null;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration config = new Configuration();
                Properties prop = new Properties();
                prop.put(environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                prop.put(environment.URL, "jdbc:mysql://localhost:3306/mydbtest");
                prop.put(environment.USER, "root");
                prop.put(environment.PASS, "root");
                prop.put(environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
                prop.put(environment.SHOW_SQL, "true");
                prop.put(environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                config.setProperties(prop);
                config.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(config.getProperties()).build();
                sessionFactory = config.buildSessionFactory(serviceRegistry);

            } catch (Throwable ex) {
                System.err.println("Initial SessionFactory creation failed." + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
        return sessionFactory;
    }
    public static Connection getConnection() {
        Connection connection = null;

        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (!connection.isClosed()) {
                System.out.println("Соединение с базой данных установленно!");
            }
            connection.close();
            if (connection.isClosed()) {
                System.out.println("Соединение с базой данных закрыто!");
            }
        } catch (SQLException e) {
            System.err.println("Не удалось загрузить драйвер");
        }
        return connection;

    }

}