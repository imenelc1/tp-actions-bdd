import org.junit.jupiter.api.*;
import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsTest {

    private Connection conn;

    @BeforeEach
    void setUp() throws SQLException {
        conn = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/tp",
            "postgres",
            "postgres"
        );

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE students RESTART IDENTITY");
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    void testAddAndGetStudent() throws SQLException {
        int id = Students.addStudent(conn, "Imene", 16);

        Students.Student student = Students.getStudent(conn, id);

        assertEquals("Imene", student.name);
        assertEquals(16.0, student.grade);
    }

    @Test
    void testGetStudentNotFound() throws SQLException {
        assertNull(Students.getStudent(conn, 9999));
    }

    @Test
    void testAverageGrade() throws SQLException {
        Students.addStudent(conn, "Amel", 10);
        Students.addStudent(conn, "Karim", 14);

        assertEquals(12.0, Students.averageGrade(conn));
    }
}
