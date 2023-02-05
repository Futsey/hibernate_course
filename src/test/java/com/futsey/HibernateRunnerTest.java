package com.futsey;

import com.futsey.entity.Birthday;
import com.futsey.entity.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {

    /** Простая реализация класса Session
     * @see org.hibernate.Session
     * @throws SQLException
     * @throws IllegalAccessException
     */
    @Test
    void checkReflectionAPI() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .username("Futsey")
                .firstname("Andrew")
                .lastname("Petrushin")
                .birthDate(new Birthday(LocalDate.of(1980, 1, 1)))
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