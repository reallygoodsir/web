package org.cars.servlets;

import org.cars.model.Car;
import org.cars.model.CarInfo;

import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class CarServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        StringBuilder html = new StringBuilder();

        Cookie cookie1 = new Cookie("my-key", "my-value");
        Cookie cookie2 = new Cookie("my-test-key", "my-test-value");
        resp.addCookie(cookie1);
        resp.addCookie(cookie2);


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
                "<input type=\"submit\" value=\"Add\"/>" +
                "</br></br>" +
                "<a href=\"http://localhost:8080/cars-web-app-form/cars-in-session\">Show all cars in session</a> " +
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

        HttpSession session = request.getSession(true);

        List<Car> cars = (List<Car>) session.getAttribute("cars");
        if (cars == null) {
            cars = new ArrayList<>();
            session.setAttribute("cars", cars);
        }
        cars.add(car);

        response.setContentType("text/html");
        doGet(request, response);
    }
}
