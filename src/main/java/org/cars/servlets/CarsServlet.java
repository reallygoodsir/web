package org.cars.servlets;

import org.cars.dao.CarDAO;
import org.cars.model.Car;
import org.cars.model.CarInfo;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CarsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String carIdRequestParameterValue = req.getParameter("carId");
        Integer carId = Integer.valueOf(carIdRequestParameterValue);

        CarDAO carDAO = new CarDAO();
        Car car = carDAO.readById(carId);

        if (car == null) {
            PrintWriter writer = resp.getWriter();
            resp.setStatus(400);
            resp.setHeader("MY-TEST-RESPONSE-HEADER", "TestValue");
            writer.println("Car with id " + carId + " not found");
        } else {
            Integer id = car.getCarId();
            Integer carPrice = car.getCarPrice();
            String carType = car.getCarType();
            CarInfo carInfo = car.getCarInfo();
            String name = carInfo.getName();
            String make = carInfo.getMake();
            String model = carInfo.getModel();
            PrintWriter writer = resp.getWriter();

            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<style>\n" +
                    "table, th, td {\n" +
                    "  border:1px solid black;\n" +
                    "}\n" +
                    "</style>\n" +
                    "<body>\n" +
                    "\n" +
                    "<h1>CARS</h1>");

            html.append("<table style=\"width:100%\">");
            html.append("<tr>");
            html.append("<th>").append("CAR ID").append("</th>");
            html.append("<th>").append("CAR NAME").append("</th>");
            html.append("<th>").append("CAR PRICE").append("</th>");
            html.append("<th>").append("CAR TYPE").append("</th>");
            html.append("<th>").append("CAR MAKE").append("</th>");
            html.append("<th>").append("CAR MODEL").append("</th>");
            html.append("</tr>");
            html.append("<tr>");
            html.append("<td>").append(id).append("</td>");
            html.append("<td>").append(name).append("</td>");
            html.append("<td>").append(carPrice).append("</td>");
            html.append("<td>").append(carType).append("</td>");
            html.append("<td>").append(make).append("</td>");
            html.append("<td>").append(model).append("</td>");
            html.append("</tr>");
            html.append("</table>");

            html.append("</body>\n" +
                    "</html>");

            writer.println(html);
        }
    }
}