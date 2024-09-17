package org.cars.servlets;

import org.cars.dao.CarDAO;
import org.cars.model.Car;
import org.cars.model.CarInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class CarsSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("Start CarsSessionServlet doGet");
        response.setContentType("text/html");
        HttpSession session = request.getSession(true);
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
        List<Car> allCars = (List<Car>) session.getAttribute("cars");

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
        stringBuilder.append("<form method=\"POST\">\n" +
                "<input type=\"submit\" value=\"Save to Database\">\n" +
                "</form>");
        stringBuilder.append("<a href=\"http://localhost:8080/cars-web-app-form/cars\">Return to the main page</a> " +
                "</div>");
        out.println(stringBuilder);
        System.out.println("End CarsSessionServlet doGet");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        HttpSession session = request.getSession(true);
        CarDAO carDAO = new CarDAO();
        List<Car> allCars = (List<Car>) session.getAttribute("cars");
        for (Car car : allCars) {
            try {
                carDAO.save(car);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        doGet(request,resp);
    }
}