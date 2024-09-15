package org.cars.dao;

import org.cars.model.Car;
import org.cars.model.CarInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarDAO {
    private static final String DB_URL = "jdbc:mysql://localhost/car_db";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static final String QUERY_ADD_CARS = "INSERT INTO cars VALUES (?, ?, ?, ?, ?, ?)";
    private static final String QUERY_READ_ALL = "SELECT * FROM cars";

    public void save(Car car) throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        try {
            PreparedStatement preparedStatementCars = connection.prepareStatement(QUERY_ADD_CARS);
            connection.setAutoCommit(false);
            CarInfo carInfo = car.getCarInfo();
            preparedStatementCars.setInt(1, car.getCarId());
            preparedStatementCars.setString(2, carInfo.getName());
            preparedStatementCars.setString(3, carInfo.getMake());
            preparedStatementCars.setString(4, carInfo.getModel());
            preparedStatementCars.setInt(5, car.getCarPrice());
            preparedStatementCars.setString(6, car.getCarType());
            preparedStatementCars.execute();
        } catch (Exception e) {
            connection.rollback();
            throw new SQLException(e);
        } finally {
            connection.commit();
            connection.close();
        }
    }

    public List<Car> readAll() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
        try {
            PreparedStatement preparedStatementPlanes = connection.prepareStatement(QUERY_READ_ALL);
            ResultSet rs = preparedStatementPlanes.executeQuery();

            List<Car> cars = new ArrayList<>();
            while (rs.next()) {
                Car car = new Car();
                CarInfo carInfo = new CarInfo();
                car.setCarId(rs.getInt(1));
                carInfo.setName(rs.getString(2));
                carInfo.setMake(rs.getString(3));
                carInfo.setModel(rs.getString(4));
                car.setCarPrice(rs.getInt(5));
                car.setCarType(rs.getString(6));
                car.setCarInfo(carInfo);
                cars.add(car);
            }
            return cars;
        } catch (Exception e) {
            connection.close();
        } finally {
            connection.close();
        }
        return Collections.emptyList();
    }
}

