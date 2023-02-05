package com.futsey;

import com.futsey.convereter.BirthdayConverter;
import com.futsey.entity.Birthday;
import com.futsey.entity.Role;
import com.futsey.entity.User;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import java.sql.SQLException;
import java.time.LocalDate;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {
        Configuration configuration = new Configuration();
        /**
         * Инициализация конвертера ВАРИАНТ 2
         */
        configuration.addAttributeConverter(new BirthdayConverter());
        configuration.registerTypeOverride(new JsonBinaryType());
        configuration.configure();

        try (SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            System.out.println("HibernateRunner{ main()}: Project started");
            session.beginTransaction();
            User user = User.builder()
                    .username("Futsey")
                    .firstname("Andrew")
                    .lastname("Petrushin")
                    .info("""
                            {
                                "name": "Andrew",
                                "id": 5
                            }
                                """)
                    .birthDate(new Birthday(LocalDate.of(1980, 1, 1)))
                    .role(Role.ADMIN)
                    .build();
            session.save(user);
            session.getTransaction().commit();
        };
    }
}
