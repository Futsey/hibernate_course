package com.futsey;

import com.futsey.entity.User;
import com.futsey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.*;

import java.sql.SQLException;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {

        User user = User.builder()
                .username("Futsey1111")
                .firstname("Andrew")
                .lastname("Petrushin")
                .build();
        log.info("user entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session_1 = sessionFactory.openSession();
            try (session_1) {
                Transaction transaction = session_1.beginTransaction();
                log.trace("Transaction created, {}", transaction);

                session_1.saveOrUpdate(user);
                log.trace("User {} is in persistent state saved or updated, in session {}", user, session_1);
                System.out.println("HibernateRunner{main() session_1}: Dirty session: on. User in cash 1: ".concat(String.valueOf(user)));
                session_1.delete(user);
                session_1.getTransaction().commit();
            }
            log.warn("User {} is in detached state, session {} is closed", user, session_1);
        } catch (Exception e) {
            log.error("Exception occurred", e);
            throw e;
        }
    }
}
