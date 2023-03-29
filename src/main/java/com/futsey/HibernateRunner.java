package com.futsey;

import com.futsey.entity.Birthday;
import com.futsey.entity.Company;
import com.futsey.entity.PersonalInfo;
import com.futsey.entity.User;
import com.futsey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.*;

import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) throws SQLException {

        Company company = Company.builder()
                .name("Google")
                .build();

//        User user = User.builder()
//                .username("Fut")
//                .personalInfo(PersonalInfo.builder()
//                        .firstname("Andrew")
//                        .lastname("Petrushin")
//                        .birthDate(new Birthday(LocalDate.of(1980, 1, 1)))
//                        .build())
//                .build();
//        log.info("user entity is in transient state, object {}", user);
//
//        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
//            Session session_1 = sessionFactory.openSession();
//            try (session_1) {
//                Transaction transaction = session_1.beginTransaction();
//                log.trace("Transaction created, {}", transaction);
//                session_1.saveOrUpdate(company);
//                log.info("Company {} is in persistent state saved or updated, in session {}", company, session_1);
//                session_1.saveOrUpdate(user);
//                log.info("User {} is in persistent state saved or updated, in session {}", user, session_1);
//                session_1.getTransaction().commit();
//            }
//            log.warn("User {} is in detached state, session {} is closed", user, session_1);
//        } catch (Exception e) {
//            log.error("Exception occurred", e);
//            throw e;
//        }
    }
}
