package org.cars.dao;

import org.cars.model.Car;
import org.cars.model.CarInfo;

import java.sql.*;

public class CarDAO {
    private static final String DB_URL = "jdbc:mysql://localhost/car_db";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static final String QUERY_SELECT_CAR_BY_ID = "SELECT * FROM cars where id = ?";

    public Car readById(Integer carId) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
            PreparedStatement preparedStatementCars = connection.prepareStatement(QUERY_SELECT_CAR_BY_ID);
            preparedStatementCars.setInt(1, carId);
            ResultSet rsCars = preparedStatementCars.executeQuery();

            boolean exists = rsCars.next();
            if (exists) {
                Car car = new Car();
                car.setCarId(rsCars.getInt(1));
                car.setCarPrice(rsCars.getInt(5));
                car.setCarType(rsCars.getString(6));

                CarInfo carInfo = new CarInfo();
                carInfo.setName(rsCars.getString(2));
                carInfo.setMake(rsCars.getString(3));
                carInfo.setModel(rsCars.getString(4));

                car.setCarInfo(carInfo);

                connection.close();

                return car;
            } else {
                connection.close();
                return null;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

