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

public class CarsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        boolean trueDeleteLater = true;
        if (!trueDeleteLater) {
            PrintWriter writer = resp.getWriter();
            resp.setStatus(400);
            resp.setHeader("MY-TEST-RESPONSE-HEADER", "TestValue");
            writer.println("crazy error");
        } else {
//
//            Integer id = car.getCarId();
//            Integer carPrice = car.getCarPrice();
//            String carType = car.getCarType();
//            CarInfo carInfo = car.getCarInfo();
//            String name = carInfo.getName();
//            String make = carInfo.getMake();
//            String model = carInfo.getModel();
            PrintWriter writer = resp.getWriter();
            StringBuilder html = new StringBuilder();
//            html.append("<!DOCTYPE html>\n" +
//                    "<html>\n" +
//                    "<style>\n" +
//                    "table, th, td {\n" +
//                    "  border:1px solid black;\n" +
//                    "}\n" +
//                    "</style>\n" +
//                    "<body>\n" +
//                    "\n" +
//                    "<h1>CARS</h1>");

            // <html>
            //<body>
            //<form action="/car-web-app/cars" method="POST">
            //    ID: <input type="text" name="ID">
            //    <br/>
            //    Name: <input type="text" name="Name"/>
            //    <br/>
            //    Model: <input type="text" name="Model">
            //    <br/>
            //    Price: <input type="text" name="Price"/>
            //    <br/>
            //    Type: <input type="text" name="Type"/>
            //    <input type="submit" value="Save"/>
            //</form>
            //</body>
            //</html>
//            html.append("ID:\n" +
//                    "<input type=\"text\" name=\"ID\">\n" +
//                    "<br/>\n" +
//                    "Name:\n" +
//                    "<input type=\"text\" name=\"NAME\">\n" +
//                    "<br/>\n" +
//                    "Make:\n" +
//                    "<input type=\"text\" name=\"ID\">\n" +
//                    "<br/>\n" +
//                    "Model:\n" +
//                    "<input type=\"text\" name=\"ID\">\n" +
//                    "<br/>\n" +
//                    "Price:\n" +
//                    "<input type=\"text\" name=\"ID\">\n" +
//                    "<br/>\n" +
//                    "Type:\n" +
//                    "<input type=\"text\" name=\"ID\">");
            html.append("<h1>ADDER</h1>\n" +
                    "<form method=\"POST\">\n" +
                    "  <h3>ID:  <input type=\\\"text\\\" name=\"id\"/> <h3>" + //might have to delete the h3 at the end
                    "  <h3>Name:  <input type=\\\"text\\\" name=\"name\"/> <h3>" +
                    "  <h3>Make:  <input type=\\\"text\\\" name=\"make\" <h3>" +
                    "  <h3>Model:  <input type=\\\"text\\\" name=\"model\" <h3>" +
                    "  <h3>Price:  <input type=\\\"text\\\" name=\"price\" <h3>" +
                    "  <h3>Type:  <input type=\\\"text\\\" name=\"type\" <h3>" +
                    "</form>\n" +
//                    "<button onclick=\"my()\">Submit now</button>\n" +
                    "<input type=\"submit\" value=\"Save\"/>" +
                    "<p id=\"demo\"></p>\n" +
                    "</div>");
            writer.println(html);
        }
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
//        String title = "Using GET Method to Read Form Data";
//        String docType =
//                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
//
//        out.println(docType +
//                "<html>\n" +
//                "<head><title>" + title + "</title></head>\n" +
//                "<body bgcolor = \"#f0f0f0\">\n" +
//                "<h1 align = \"center\">" + title + "</h1>\n" +
//                "<ul>\n" +
//                "  <li><b>ID: </b>: "
//                + request.getParameter("id") + "\n" +
//                "  <li><b>Name</b>: "
//                + request.getParameter("name") + "\n" +
//                "  <li><b>Make: </b>: "
//                + request.getParameter("make") + "\n" +
//                "  <li><b>Model: </b>: "
//                + request.getParameter("model") + "\n" +
//                "  <li><b>Price: </b>: "
//                + request.getParameter("price") + "\n" +
//                "  <li><b>Type: </b>: "
//                + request.getParameter("type") + "\n" +
//                "</ul>\n" +
//                "</body>" +
//                "</html>"
//        );
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

        List<Car> allCars = carDAO.readAll(Integer.valueOf(request.getParameter("id") + 1));
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
