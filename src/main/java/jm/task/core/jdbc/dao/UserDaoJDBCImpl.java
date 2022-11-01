package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    //private static Connection connection = Util.getConnection();
    Connection connection = null;

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() throws SQLException {
        try {
            Connection connection = Util.getConnection();
            // команда создания таблицы
            String sqlCommand = "CREATE TABLE person (id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20), lastName VARCHAR(20), age INT)";

            Statement statement = connection.createStatement();
            // создание таблицы
            statement.executeUpdate(sqlCommand);

            System.out.println("Database has been created!");

        } catch (Exception ex) {
            System.out.println("Connection failed...");

            System.out.println(ex);
        }
        finally {
            connection.close();
        }

    }

    public void dropUsersTable() throws SQLException {

        Statement statement = null;
        try {
            Connection connection = Util.getConnection();
            statement = connection.createStatement();
            String sql = "DROP TABLE person";
            statement.executeUpdate(sql);
            System.out.println("Database dropped successfully...");
        } catch (Exception e) {
            System.out.println("Database  not found...");
            System.out.println(e);
        }
        finally {
            connection.close();
        }


    }

    public void saveUser(String name, String lastName, byte age) throws SQLException {
        User user = new User(name, lastName, age);
        String sql = "INSERT INTO Person (name, lastname, age) VALUES (?, ?, ? )";
        try {
            Connection connection = Util.getConnection();
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());

            preparedStatement.executeUpdate();
            System.out.println("User с именем –" + user.getName() + " добавлен в базу данных");
        } catch ( SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            connection.close();
        }

    }

    public void removeUserById(long id) throws SQLException {
        PreparedStatement preparedStatement =
                null;
        try {
            Connection connection = Util.getConnection();
            preparedStatement = connection.prepareStatement("DELETE FROM Person WHERE id=?");

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        finally {
            connection.close();
        }

    }

    public List<User> getAllUsers() throws SQLException {

        List<User> people = new ArrayList<>();

        try(Statement statement = connection.createStatement();) {
            Connection connection = Util.getConnection();

            String SQL = "SELECT * FROM Person";
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()) {
                User person = new User();

                person.setId(resultSet.getLong("id"));
                person.setName(resultSet.getString("name"));
                person.setLastName(resultSet.getString("lastName"));
                person.setAge((byte) resultSet.getInt("age"));

                people.add(person);
                System.out.println(person);
            }

        } catch (SQLException | NullPointerException throwables) {
            throwables.printStackTrace();
        }
        finally {
            connection.close();
        }

        return people;
    }

    public void cleanUsersTable() throws SQLException {
        int i = 0;
        PreparedStatement preparedStatement =
                null;
        String QUERY = "SELECT id, name, lastname, age FROM Person";
        try (Statement statement = connection.createStatement();) {
            Connection connection = Util.getConnection();


            ResultSet rs = statement.executeQuery(QUERY);
            while (rs.next()) {
                String sql = "DELETE FROM Person " +
                        "WHERE" + rs.getInt("id");
                statement.executeUpdate(sql);


            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Table is empty");
            ;
        }
        finally {
            connection.close();
        }

    }
}