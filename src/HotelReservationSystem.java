import java.sql.*;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "8861102894Aa#"; // replace with your password

    private static Connection connection;
    private static Statement statement;0
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // 1. Load and register the JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 2. Establish the connection
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();

            // 3. Show menu
            while (true) {
                System.out.println("\n=== HOTEL MANAGEMENT SYSTEM ===");
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number by Reservation ID");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1 -> reserveRoom();
                    case 2 -> viewReservations();
                    case 3 -> getRoomNumber();
                    case 4 -> updateReservation();
                    case 5 -> deleteReservation();
                    case 0 -> {
                        exitSystem();
                        return;
                    }
                    default -> System.out.println("Invalid option!");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reserveRoom() {
        try {
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();
            System.out.print("Enter Room Number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter Contact Number: ");
            String contactNumber = scanner.nextLine();

            String sql = "INSERT INTO reservations (guest_name, room_number, contact_number) VALUES ('" +
                    guestName + "', " + roomNumber + ", '" + contactNumber + "')";
            int rowsInserted = statement.executeUpdate(sql);
            if (rowsInserted > 0) {
                System.out.println("Room reserved successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Reservation failed: " + e.getMessage());
        }
    }

    private static void viewReservations() {
        try {
            String sql = "SELECT * FROM reservations";
            ResultSet rs = statement.executeQuery(sql);

            System.out.println("\n--- All Reservations ---");
            while (rs.next()) {
                System.out.println("Reservation ID: " + rs.getInt("reservation_id"));
                System.out.println("Guest Name: " + rs.getString("guest_name"));
                System.out.println("Room Number: " + rs.getInt("room_number"));
                System.out.println("Contact Number: " + rs.getString("contact_number"));
                System.out.println("Reservation Date: " + rs.getTimestamp("reservation_date"));
                System.out.println("-------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve reservations: " + e.getMessage());
        }
    }

    private static void getRoomNumber() {
        try {
            System.out.print("Enter Reservation ID: ");
            int reservationId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            String sql = "SELECT room_number FROM reservations WHERE reservation_id = " + reservationId +
                    " AND guest_name = '" + guestName + "'";

            ResultSet rs = statement.executeQuery(sql);

            if (rs.next()) {
                int roomNumber = rs.getInt("room_number");
                System.out.println("Room Number: " + roomNumber);
            } else {
                System.out.println("Reservation not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateReservation() {
        try {
            System.out.print("Enter Reservation ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("New Guest Name: ");
            String guestName = scanner.nextLine();
            System.out.print("New Room Number: ");
            int roomNumber = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("New Contact Number: ");
            String contactNumber = scanner.nextLine();

            String sql = "UPDATE reservations SET guest_name = '" + guestName +
                    "', room_number = " + roomNumber +
                    ", contact_number = '" + contactNumber +
                    "' WHERE reservation_id = " + id;

            int rowsUpdated = statement.executeUpdate(sql);

            if (rowsUpdated > 0) {
                System.out.println("Reservation updated successfully!");
            } else {
                System.out.println("Reservation not found.");
            }

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    private static void deleteReservation() {
        try {
            System.out.print("Enter Reservation ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String sql = "DELETE FROM reservations WHERE reservation_id = " + id;
            int rowsDeleted = statement.executeUpdate(sql);

            if (rowsDeleted > 0) {
                System.out.println("Reservation deleted successfully!");
            } else {
                System.out.println("Reservation not found.");
            }
        } catch (SQLException e) {
            System.out.println("Deletion failed: " + e.getMessage());
        }
    }

    private static void exitSystem() {
        try {
            System.out.print("Exiting");
            for (int i = 0; i < 5; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
            System.out.println("\nGoodbye!");

            if (statement != null) statement.close();
            if (connection != null) connection.close();

        } catch (Exception e) {
            System.out.println("Exit error: " + e.getMessage());
        }
    }
}
