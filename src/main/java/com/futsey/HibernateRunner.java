package com.futsey;

import com.futsey.entity.User;
import com.futsey.util.HibernateUtil;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class HibernateRunner {

    private static final Logger LOG = LoggerFactory.getLogger(HibernateRunner.class);

    public static void main(String[] args) throws SQLException {

        User user = User.builder()
                .username("Futsey1111")
                .firstname("Andrew")
                .lastname("Petrushin")
                .build();
        LOG.info("user entity is in transient state, object {}", user);

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session_1 = sessionFactory.openSession();
            try (session_1) {
                Transaction transaction = session_1.beginTransaction();
                LOG.trace("Transaction created, {}", transaction);

                session_1.saveOrUpdate(user);
                LOG.trace("User {} is in persistent state saved or updated, in session {}", user, session_1);
                System.out.println("HibernateRunner{main() session_1}: Dirty session: on. User in cash 1: ".concat(String.valueOf(user)));
                session_1.delete(user);
                session_1.getTransaction().commit();
            }
            LOG.warn("User {} is in detached state, session {} is closed", user, session_1);
        } catch (Exception e) {
            LOG.error("Exception occurred", e);
            throw e;
        }
    }
}
