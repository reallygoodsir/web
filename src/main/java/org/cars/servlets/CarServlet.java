package org.cars.servlets;

import org.cars.dao.CarDAO;
import org.cars.model.Car;
import org.cars.model.CarInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class CarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        StringBuilder html = new StringBuilder();

        html.append("<h1>Add New Car</h1>\n" +
                "<form method=\"POST\">\n" +
                "  <h3>ID:     <input type=\\\"text\\\" name=\"id\"/> <h3>" +
                "  <h3>Name:   <input type=\\\"text\\\" name=\"name\"/> <h3>" +
                "  <h3>Make:   <input type=\\\"text\\\" name=\"make\" <h3>" +
                "  <h3>Model:  <input type=\\\"text\\\" name=\"model\" <h3>" +
                "  <h3>Price:  <input type=\\\"text\\\" name=\"price\" <h3>" +
                "  <h3>Type:   <input type=\\\"text\\\" name=\"type\" <h3>" +
                "</form>\n" +
                "</br></br>" +
                "<input type=\"submit\" value=\"Save\"/>" +
                "</div>");
        writer.println(html);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Car car = new Car();
        car.setCarId(Integer.valueOf(request.getParameter("id")));
        car.setCarType(request.getParameter("type"));
        car.setCarPrice(Integer.valueOf(request.getParameter("price")));
        CarInfo carInfo = new CarInfo();
        carInfo.setName(request.getParameter("name"));
        carInfo.setModel(request.getParameter("model"));
        carInfo.setMake(request.getParameter("make"));
        car.setCarInfo(carInfo);

        CarDAO carDAO = new CarDAO();
        try {
            carDAO.save(car);
        } catch (Exception e) {
            response.getWriter().println("Error to save the car");
            response.setStatus(500);
            return;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        StringBuilder stringBuilder = new StringBuilder(" ");
        String HTML = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<style>\n" +
                "table, th, td {\n" +
                "  border:1px solid black;\n" +
                "}\n" +
                "</style>\n" +
                "<body>\n" +
                "\n" +

                "<h1>CARS</h1>" +
                "<table style=\"width:100%\">" +
                "<tr>" +
                "<th>" + "CAR ID" + "</th>" +
                "<th>" + "CAR NAME" + "</th>" +
                "<th>" + "CAR PRICE" + "</th>" +
                "<th>" + "CAR TYPE" + "</th>" +
                "<th>" + "CAR MAKE" + "</th>" +
                "<th>" + "CAR MODEL" + "</th>" +
                "</tr>";
        stringBuilder.append(HTML);

        List<Car> allCars;
        try {
            allCars = carDAO.readAll();
        } catch (SQLException e) {
            response.getWriter().println("Error to get list of cars");
            response.setStatus(500);
            return;
        }

        for (int i = 0; i < allCars.size(); i++) {
            Car printCar = allCars.get(i);
            CarInfo printCarInfo = printCar.getCarInfo();
            Integer id = printCar.getCarId();
            String carName = printCarInfo.getName();
            Integer carPrice = printCar.getCarPrice();
            String carType = printCar.getCarType();
            String carMake = printCarInfo.getMake();
            String carModel = printCarInfo.getModel();
            stringBuilder.append("</tr>" +
                    "<tr>" +
                    "<td>" + id + "</td>" +
                    "<td>" + carName + "</td>" +
                    "<td>" + carPrice + "</td>" +
                    "<td>" + carType + "</td>" +
                    "<td>" + carMake + "</td>" +
                    "<td>" + carModel + "</td>" +
                    "</tr>"
            );
        }
        stringBuilder.append("</table>" +
                "</body>\n" +
                "</html>");
        out.println(stringBuilder);
    }
}
