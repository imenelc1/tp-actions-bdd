import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Students {

    public static int addStudent(Connection conn, String name, double grade)
            throws SQLException {

        String sql = "INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, grade);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }

    public static Student getStudent(Connection conn, int id)
            throws SQLException {

        String sql = "SELECT id, name, grade FROM students WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("grade")
                    );
                }
                return null;
            }
        }
    }

    public static Double averageGrade(Connection conn)
            throws SQLException {

        String sql = "SELECT AVG(grade) FROM students";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            double average = rs.getDouble(1);

            return rs.wasNull() ? null : average;
        }
    }

    public static class Student {
        public int id;
        public String name;
        public double grade;

        public Student(int id, String name, double grade) {
            this.id = id;
            this.name = name;
            this.grade = grade;
        }
    }
}
