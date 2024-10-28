# Installation Instructions

1. **Install JDK 8.**

2. **Install MySQL Workbench 8.0.38**  
   Use the following credentials:
    - Username: `root`
    - Password: `root`

3. **Install Apache Tomcat 9.**

4. **Install Maven 3.**

5. **Import the `servlets-db.sql` file to MySQL DB.**

6. **Add an admin user to the users database by executing the following SQL:**
   ```sql
   INSERT INTO `servlets-db`.`users` (
       `name`,
       `password`
   ) VALUES (
       'admin',
       'admin'
   );
   ```

7. **Add the following environment variables:**
    - `SERVLETS_DB_URL=jdbc:mysql://localhost/servlets-db`
    - `SERVLETS_DB_USER_NAME=root`
    - `SERVLETS_DB_PASSWORD=root`

8. **Open in Browser:**  
   Go to [http://localhost:8080/servlets-quiz/admin](http://localhost:8080/servlets-quiz/admin) and log in using the
   credentials:
    - Username: `admin`
    - Password: `admin`

9. **Admin page URL:**  
   [http://localhost:8080/servlets-quiz/questions](http://localhost:8080/servlets-quiz/questions)

10. **Quiz page URL:**  
    [http://localhost:8080/servlets-quiz/quiz](http://localhost:8080/servlets-quiz/quiz)

---