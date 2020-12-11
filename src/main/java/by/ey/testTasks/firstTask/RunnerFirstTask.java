package by.ey.testTasks.util;

import by.ey.testTasks.firstTask.service.RandomFile;
import by.ey.testTasks.firstTask.service.RandomFileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RunnerFirstTask {
    static {
        RandomFile.createRandomFiles();
        Connection connection;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            Statement statement = connection.createStatement();
            final String SQL = "TRUNCATE TABLE RANDOM_FILES";
            statement.execute(SQL);
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) throws SQLException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("\nChoose an action: ");
            System.out.println("\t1. Merge files. (Press '1')");
            System.out.println("\t2. Import files' data into database. (Press '2')");
            System.out.println("\t3. Search sum of integers in MySQL and median of doubles in MySQL. (Press '3')");
            System.out.println("Enter 'exit' to exit the program.");

            String choice = reader.readLine();

            switch (choice.toLowerCase()) {
                case "1":
                    System.out.println("You chose merge files.");
                    System.out.println("Do you want to remove some lines? (Enter 'true' or 'false')");
                    while (true) {
                        String isDelete = reader.readLine().toLowerCase();
                        if (isDelete.toLowerCase().equals("true")) {
                            RandomFile.unionFiles(true);
                            break;
                        }
                        else
                            if (isDelete.equals("false")) {
                            RandomFile.unionFiles(false);
                            break;
                            }
                            else {
                                System.out.println("You entered incorrect word! Try again. (Enter 'true' or 'false')");
                            }
                    }
                    System.out.println("Done!");
                    break;
                case "2":
                    System.out.println("You chose import files.");
                    RandomFileService.addFilesInDatabase();
                    System.out.println("Done!");
                    break;
                case "3":
                    System.out.println("You chose search sum of integers in MySQL and median of doubles in MySQL.");
                    RandomFileService.getSumAndMedianFromFiles();
                    break;
                case "exit":
                    System.out.println("See you late!");
                    reader.close();
                    System.exit(0);
                default:
                    System.out.println("Try again! You entered incorrect parameter.");
                    break;
            }
        }
    }
}
