package com.futsey;

import com.futsey.entity.User;
import com.futsey.util.HibernateUtil;
import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class HibernateRunner {

    public static void main(String[] args) throws SQLException {

        User user = User.builder()
                .username("Futsey1111")
                .firstname("Andrew")
                .lastname("Petrushin")
                .build();

        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            try (Session session_1 = sessionFactory.openSession()) {
                session_1.beginTransaction();

                session_1.saveOrUpdate(user);
                System.out.println("HibernateRunner{main() session_1}: Dirty session: on. User in cash 1: ".concat(String.valueOf(user)));
                session_1.delete(user);
                session_1.getTransaction().commit();
            }
            try (Session session_2 = sessionFactory.openSession()) {
                session_2.beginTransaction();
                System.out.println("HibernateRunner{main() session_1}: Dirty session: off. User in DB after session_1.delete(user): "
                        .concat(String.valueOf(session_2.get(User.class, user.getFirstname()))));
                user.setFirstname("Ivan");
                System.out.println("HibernateRunner{main() session_2}: Dirty session: on. User in cash 1 with overridden firstname : "
                        .concat(String.valueOf(user)));
                // example .refresh() !!!!!UserInDB важнее User!!!!!
                    // session_2.refresh(user);
                    // User userInDB = session_2.get(User.class, user.getFirstname());
                    // user.setUsername(userInDB.getUsername());
                    // user.setFirstname(userInDB.getFirstname());
                    // user.setLastname(userInDB.getLastname());
                    // etc...
                // example .merge() !!!!!User важнее userInDB!!!!!
                    // session_2.merge(user);
                    // User userInDB = session_2.get(User.class, user.getFirstname());
                    // userInDB.setUsername(user.getUsername());
                    // userInDB.setFirstname(user.getFirstname());
                    // userInDB.setLastname(user.getLastname());
                    // etc...
                session_2.getTransaction().commit();
            }
        }
    }
}
