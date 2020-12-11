package by.ey.testTasks.firstTask.service;

import by.ey.testTasks.firstTask.dao.RandomFileDAO;

import java.sql.SQLException;

public class RandomFileService {
    public static void addFilesInDatabase() throws SQLException {
        RandomFileDAO.add();
    }

    public static void getSumAndMedianFromFiles() throws SQLException {
        RandomFileDAO.getSumAndMedian();
    }
}
