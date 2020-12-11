package by.ey.testTasks.firstTask.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedList;

public class RandomFile {
    private static final int LINES_QUANTITY = 1;
    private static final int FILES_QUANTITY = 10;
    private static final String fileNamePattern = "file_";

    static {
        new File("D://Files//Generated Files").mkdirs(); //�������� ���������� ��� �������� ������
        new File("D://Files//Union File").mkdirs(); //�������� ���������� ��� �������� ������������� �����
    }

    public static void createRandomFiles() {
        try {
            for (int fileNumber = 1; fileNumber <= FILES_QUANTITY; fileNumber++) {
                File file = new File("D://Files//Generated Files//" + fileNamePattern + fileNumber + ".txt"); //�������� �����
                try (Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) { //����� ������ � ����
                    for (int line = 0; line < LINES_QUANTITY; line++) { //���� �������� 100000 ����� � �����
                        fw.write(randomDate() + "||" + randomLatinCharacters() + "||" + randomRussianCharacters() + "||" +
                                randomPositiveEvenIntegerNumber() + "||" + randomPositiveDoubleNumber() + "\n");
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void unionFiles(boolean isDelete) {
        try {
            int countDeleteLines = 0;
            File fileUnion = new File("D://Files//Union File//" + fileNamePattern + "union.txt"); //�������� �����
            File[] filesArray = new File("D://Files//Generated Files").listFiles(); //�������� ������� ��������� ������
            LinkedList<File> listOfFiles;
            if (filesArray != null) {
                listOfFiles = new LinkedList<>();
                listOfFiles.addAll(Arrays.asList(filesArray)); //�������� ������ �� ������� ������
            }
            else {
                System.out.println("Directory is empty!");
                return;
            }
            try (Writer fileWriterUnion = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileUnion), StandardCharsets.UTF_8))) { //try � ���������. �������� ������ ��� ������ � ������������ ����
                //���� ������������ ������ ���� � ��������� ����� �� ������ ��������
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                if (isDelete) {
                    System.out.println("Enter combination of characters: ");
                    String combinationOfChars = reader.readLine(); //������ �� ������� ������ �������� ��� �������� �����
                    for (File file : listOfFiles) {
                        System.out.println("Checking: " + file.getName());
                        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)); //�������� ������ ��� ������ ��� ������ �� ����� �����
                        String line;
                        File fileTemp = new File("D://Files//Union File//temp.txt"); //�������� ���������� �����
                        Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp), StandardCharsets.UTF_8)); //�������� ������ ��� ������ �� ��������� ����
                        while ((line = fr.readLine()) != null) {
                            if (!line.contains(combinationOfChars)) {
                                fw.write(line + "\n");
                                fileWriterUnion.write(line + "\n");
                            }
                            else
                                countDeleteLines++;
                        }
                        fr.close();
                        fw.close();
                        //�������� ������������� �����
                        file.delete();
                        //������������ ���������� ����� � ��� ����� �������������
                        fileTemp.renameTo(file);
                    }
                    System.out.println(countDeleteLines + " lines is deleted");
                }
                //���� ������������ �� ������ ���� � ��������� ����� �� ������ ��������
                else {
                    for (File file: listOfFiles) {
                        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)); //�������� ������ ��� ������ ��� ������ �� ����� �����
                        String line;
                        while ((line = fr.readLine()) != null)
                            fileWriterUnion.write(line + "\n");
                        fr.close();
                    }
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //����� �������� ��������� ����
    private static String randomDate() {
        String europeanDatePattern = "dd.MM.yyyy"; // ������ ��� ������� ����
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);

        LocalDate currentDate = LocalDate.now();

        int year = (int) (Math.random() * ((currentDate.getYear() - currentDate.minusYears(5).getYear()))) + 1
                + currentDate.minusYears(5).getYear(); // ��������� ��� � ���������� 5 ���
        int day = (int) (Math.random() * (31 - 1) + 1) + 1; //��������� ���� ������
        int month = (int) (Math.random() * (12 - 1) + 1) + 1; //��������� �����

        //���� ������� ��� ����� ���������������� � ������� ����� ������ ��� ����� ���������������� � ������� ����
        //������ ����������������, �� ���������� ������� ����, ��� ��� �������� ��������������� ��� �� ���������
        if (currentDate.getYear() == year && currentDate.getMonthValue() <= month && currentDate.getDayOfMonth() < day) {
            month = currentDate.getMonthValue();
            day = currentDate.getDayOfMonth();
        } else
            if (currentDate.getYear() == year && currentDate.getMonthValue() < month)
                month = currentDate.getMonthValue() - 1;

            if (month == 2 && day == 29 && (year == 2020 || year == 2016)) //�������� ����������� ����
                return LocalDate.of(year, month, day).format(europeanDateFormatter);
            else
                if (month == 2 && day > 28) //� ������� �� ����� ���� ������ 28 ����
                day = day - 3;
            else {
                //���� � ������ 30 ����, � ��������� �������� ���� day ������� �������� 31, �� ������ ���� ����
                if (((month  == 2 || month  == 4 || month  == 6 || month  == 9 || month  == 11)) && day == 31) {
                    day = day - 1;
                }
            }
            return LocalDate.of(year, month, day).format(europeanDateFormatter);
    }

    //����� �������� 10 �������� ��������
    private static String randomLatinCharacters() {
        StringBuilder randomLatinCharacters = new StringBuilder();
        int character;
        for (int index = 0; index < 10; index++) {
            int choice = (int)(Math.random() * ((2 - 1) + 1)) + 1; //���������� ��� ���������� ������ ����� ���������� � ���������� �������
            switch (choice) {
                case 1:
                    character = (int)(Math.random() * ((90 - 65) + 1)) + 65; //��������� ��������� ��������� �����
                    randomLatinCharacters.append((char)character);
                    break;
                case 2:
                    character = (int)(Math.random() * ((122 - 97) + 1)) + 97; //��������� ��������� ��������� �����
                    randomLatinCharacters.append((char)character);
                    break;
            }
        }
        return randomLatinCharacters.toString();
    }

    //����� ��� �������� 10 ���� ���������
    private static String randomRussianCharacters() {
        StringBuilder randomRussianCharacters = new StringBuilder();
        int character;
        for (int index = 0; index < 10; index++) {
            character = (int)(Math.random() * ((1103 - 1040) + 1)) + 1040;
            randomRussianCharacters.append((char)character);
        }
        return randomRussianCharacters.toString();
    }

    //����� �������� ���������� ������� �������������� ������ �����
    private static String randomPositiveEvenIntegerNumber() {
        return String.valueOf((int) ((Math.random() * ((50000000 - 1) + 1)) + 1) * 2);
    }

    //����� �������� ���������� �������������� ������������� �����
    private static String randomPositiveDoubleNumber() {
        double randomPositiveDoubleNumber = (Math.random() * ((19 - 1) + 1)) + 1;
        return String.format("%.8f", randomPositiveDoubleNumber); //������ ������ 8 ���� ����� �����
    }
}
