package com.futsey;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            System.out.println("HibernateRunner{ main()}: Project started");
        };
    }
}
