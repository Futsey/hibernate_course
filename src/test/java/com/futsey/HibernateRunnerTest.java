package com.futsey;

import com.futsey.entity.*;
import com.futsey.util.HibernateUtil;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    @Test
    void checkManyToMany() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var user = session.get(User.class, 16L);
            var chat = session.get(Chat.class, 1L);
            var userChat = UserChat.builder()
                    .created_add(Instant.now())
                    .created_by(user.getUsername())
                    .build();

            userChat.setUser(user);
            userChat.setChat(chat);

            session.save(userChat);

            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOneToOne() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            var user = session.get(User.class, 16L);

//            var user = User.builder()
//                    .username("Andrew2@gmail.com")
//                    .build();
//            var profile = Profile.builder()
//                    .street("Lenina")
//                    .language("ru")
//                    .build();
//            profile.setUser(user);
//            session.save(user);
            System.out.println();

            session.getTransaction().commit();
        }
    }

    @Test
    void checkOrphanRemoval() {
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
             var session = sessionFactory.openSession()) {
            session.beginTransaction();

            Company company = session.getReference(Company.class, 9);
            company.getUsers().removeIf(user -> user.getId() == 9L);
            session.getTransaction().commit();
        }
    }

    @Test
    void checkLazyInit() {
        Company company = null;
        try (var sessionFactory = HibernateUtil.buildSessionFactory();
            var session = sessionFactory.openSession()) {
            session.beginTransaction();

            company = session.getReference(Company.class, 1);
            System.out.println();

            session.getTransaction().commit();
        }
        var users = company.getUsers();
        System.out.println(users.size());
    }

    @Test
    void deleteUserFromCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 7);
        session.delete(company);

        session.getTransaction().commit();
    }

    @Test
    void addUserToCompany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

//        var company = Company.builder()
//                .name("Apple")
//                .build();

        var company = session.get(Company.class, 9);

        var user = User.builder()
                .username("Oleg1")
                .build();
//        user.setCompany(company);
//        company.getUsers().add(user);
        company.addUser(user);
        session.saveOrUpdate(company);

        session.getTransaction().commit();
    }

    @Test
    void oneToMany() {
        @Cleanup var sessionFactory = HibernateUtil.buildSessionFactory();
        @Cleanup var session = sessionFactory.openSession();
        session.beginTransaction();

        var company = session.get(Company.class, 35);
        System.out.println("");

        session.getTransaction().commit();
    }


    /** Простая реализация класса Session
     * @see org.hibernate.Session
     * @throws SQLException
     * @throws IllegalAccessException
     */
    @Test
    void checkReflectionAPI() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("Futsey")
                .personalInfo(PersonalInfo.builder()
                        .firstname("Andrew")
                        .lastname("Petrushin")
                        .birthDate(new Birthday(LocalDate.of(1980, 1, 1)))
                        .build())
                .build();
        String sql = """
                INSERT
                INTO
                %s
                (%s)
                VALUSES
                (%s)
                
                """;
        String tableName = ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        Field[] deckaredFields = user.getClass().getDeclaredFields();

        String columnNames = Arrays.stream(deckaredFields)
                .map(field -> ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(deckaredFields)
                .map(field -> "?")
                .collect(Collectors.joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        /** Пример базового коннекта:

        Connection connection = null;
        PreparedStatement ps = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        for(Field declaredField : deckaredFields) {
            declaredField.setAccessible(true);
            ps.setObject(1, declaredField.get(user));
        }

         */
    }
}