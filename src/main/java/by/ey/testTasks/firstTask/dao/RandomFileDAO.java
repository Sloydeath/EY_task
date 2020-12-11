package by.ey.testTasks.firstTask.dao;

import by.ey.testTasks.util.ConnectionPool;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

public class RandomFileDAO {

    private static LinkedList<File> listOfFiles;

    static {
        new File("D://Files//Generated Files").mkdirs(); //Создание директории для хранения файлов
        new File("D://Files//Union File").mkdirs(); //Создание директории для хранения объединенного файла

        File[] filesArray = new File("D://Files//Generated Files").listFiles(); //создание массива созданных файлов
        if (filesArray != null) {
            listOfFiles = new LinkedList<>();
            listOfFiles.addAll(Arrays.asList(filesArray)); //создание списка из массива файлов
        }
        else {
            System.out.println("Directory is empty!");
        }
    }

    public static void add() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        PreparedStatement preparedStatement;
        int countAddLines = 0;

        final String SQL = "INSERT INTO RANDOM_FILES(RANDOM_DATE, LATIN_CHARS, RUSSIAN_CHARS, INTEGERS, DOUBLES) VALUES (?, ?, ?, ?, ?)";

        for (File file: listOfFiles) {
            try (BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));) {  //создание потока для чтения для чтения из файла строк
                String line;
                while ((line = fr.readLine()) != null) {
                    String[] lineArray = line.split("[|]{2}");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(lineArray[0]);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                    try {
                        preparedStatement = connection.prepareStatement(SQL);

                        assert date != null;
                        preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
                        preparedStatement.setString(2, lineArray[1]);
                        preparedStatement.setString(3, lineArray[2]);
                        preparedStatement.setInt(4, Integer.parseInt(lineArray[3]));
                        preparedStatement.setDouble(5, Double.parseDouble(lineArray[4].replace(',', '.')));

                        preparedStatement.executeUpdate();
                        countAddLines++;
                        System.out.println(countAddLines);
                    } catch (SQLException exSQL) {
                        exSQL.printStackTrace();
                    }
                }
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void getSumAndMedian() throws SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        CallableStatement callableStatement = null;
        try {
            String SQL = "{call sum_and_median (?, ?)}";
            callableStatement = connection.prepareCall(SQL);
            callableStatement.registerOutParameter(1, Types.BIGINT);
            callableStatement.registerOutParameter(2, Types.DOUBLE);
            callableStatement.execute();
            long sum = callableStatement.getLong(1);
            double median = callableStatement.getDouble(2);
            if (sum == 0 && Double.compare(median, 0) == 0) {
                System.out.println("Database is empty. First, add lines into database.");
                return;
            }
            System.out.println("Sum = " + sum);
            System.out.println("Median = " + median);
            System.out.println("Done!");
        } catch(SQLException e) {
            if(callableStatement != null){
                callableStatement.close();
            }
            e.printStackTrace();
        } finally {
            if(callableStatement != null){
                callableStatement.close();
            }
        }
    }
}
