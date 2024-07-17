package org.example;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 내가 어떤 DB를 사용하는지를 정해주는 연결 주소
        String connectionString = "jdbc:sqlite:db.sqlite";
        // DB에 연결하고
        try (Connection connection
                     = DriverManager.getConnection(connectionString)) {
            System.out.println("Connection Success!!!");

            // 데이터베이스에 SQL문(SQL Statement)를 전달할 준비를 한다.
            Statement statement = connection.createStatement();
            // 실제 SQL문을 전달한다.
            statement.execute("""
            DROP TABLE IF EXISTS user;
            """);
            statement.execute("""
            CREATE TABLE user (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT,
                password TEXT,
                first_name TEXT,
                last_name TEXT,
                email TEXT
            );
            """);
            // 새로운 사용자를 추가한다.
            statement.execute("""
            INSERT INTO user(username, password, first_name, last_name, email)
            VALUES ('alex', 'aaaa', 'Alex', 'Rod', 'alex@gmail.com');
            """);
            statement.execute("""
            INSERT INTO user(username, password, first_name, last_name, email)
            VALUES ('brad', 'bbbb', 'Brad', 'Pete', 'brad@gmail.com');
            """);
            statement.execute("""
            INSERT INTO user(username, password, first_name, last_name, email)
            VALUES ('chad', 'cccc', 'Chad', 'Brad', 'chad@gmail.com');
            """);
            statement.execute("""
            INSERT INTO user(username, password, first_name, last_name, email)
            VALUES ('dave', 'dddd', 'Dave', 'Diver', 'dave@gmail.com');
            """);
//            // 전달하는 SQL은 평범한 Java 문자열이다.
//            String insertSql = """
//            INSERT INTO user(username, password)
//            VALUES ('%s', '%s');
//            """;
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("input username: ");
//            String username = scanner.nextLine();
//            System.out.print("input password: ");
//            String password = scanner.nextLine();
//            statement.execute(String.format(insertSql, username, password));

            // 조회의 결과는 ResultSet 으로 돌아온다.
            ResultSet resultSet = statement.executeQuery("""
            SELECT * FROM user;
            """);

            // resultSet.next()를 호출하면 다음 줄로 이동한다.
            // resultSet.next()는
            // 데이터가 있으면 true, 없으면 false가 된다.
            while (resultSet.next()) {
                // 데이터가 아직 있으면 출력하고
                // get{type}() 메서드로 해당 줄의 데이터를 회수한다.
                System.out.print(resultSet.getString("username") + " ");
                System.out.print(resultSet.getString("first_name") + " ");
                // 컬럼 이름이나, 몇번째 컬럼인지를 전달할 수 있다.
                System.out.println(resultSet.getString(3));
            }
            // 없으면 종료한다.

            String updateSql = """
            UPDATE user
            SET first_name = 'Alexander'
            WHERE email LIKE '%gmail.com';
            """;
            // UPDATE 또는 DELETE와 같이 테이블의 데이터를 변형시키는 SQL을 실행할 때,
            // executeUpdate()는 변경된 줄의 갯수를 반환한다.
            int rows = statement.executeUpdate(updateSql);
            System.out.println(rows);

            /*// SQL Injection
            String selectSql = "SELECT * FROM user WHERE id = %s;";
            String input = "1 OR 1 = 1";
            // SELECT * FROM user WHERE id = 1 OR 1 = 1;
            statement.executeQuery(String.format(selectSql, input));*/

            // PreparedStatement를 이용해 좀더 안전하게 SQL을 실행할 수 있다.
            String targetSql = "SELECT * FROM user WHERE id = ? AND password = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(targetSql);
            String input = "1";
            preparedStatement.setString(1, input);
            preparedStatement.setString(2, "bbbb");
            ResultSet selectResult = preparedStatement.executeQuery();
            if (selectResult.next()) {
                System.out.println(selectResult.getString("username"));
            } else {
                System.out.println("wrong user");
            }
        } catch (SQLException e) {
            System.err.println(e.getErrorCode());
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}