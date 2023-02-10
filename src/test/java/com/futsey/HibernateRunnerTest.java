package com.futsey;

import com.futsey.entity.Birthday;
import com.futsey.entity.Company;
import com.futsey.entity.PersonalInfo;
import com.futsey.entity.User;
import com.futsey.util.HibernateUtil;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

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

        var company = Company.builder()
                .name("Apple")
                .build();

        var user = User.builder()
                .username("Oleg")
                .build();
//        user.setCompany(company);
//        company.getUsers().add(user);
        company.addUser(user);
        session.save(company);

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