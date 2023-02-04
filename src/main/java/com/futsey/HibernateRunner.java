package com.futsey;

import com.futsey.entity.Role;
import com.futsey.entity.User;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            System.out.println("HibernateRunner{ main()}: Project started");
            session.beginTransaction();
            User user = User.builder()
                    .username("Futsey")
                    .firstname("Andrew")
                    .lastname("Petrushin")
                    .birthDate(LocalDate.of(1980, 1, 1))
                    .age(42)
                    .role(Role.ADMIN)
                    .build();
            session.save(user);
            session.getTransaction().commit();
        };
    }
}
