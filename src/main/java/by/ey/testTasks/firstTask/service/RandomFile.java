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
        new File("D://Files//Generated Files").mkdirs(); //Создание директории для хранения файлов
        new File("D://Files//Union File").mkdirs(); //Создание директории для хранения объединенного файла
    }

    public static void createRandomFiles() {
        try {
            for (int fileNumber = 1; fileNumber <= FILES_QUANTITY; fileNumber++) {
                File file = new File("D://Files//Generated Files//" + fileNamePattern + fileNumber + ".txt"); //создание файла
                try (Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) { //поток записи в файл
                    for (int line = 0; line < LINES_QUANTITY; line++) { //цикл создания 100000 строк в файле
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
            File fileUnion = new File("D://Files//Union File//" + fileNamePattern + "union.txt"); //создание файла
            File[] filesArray = new File("D://Files//Generated Files").listFiles(); //создание массива созданных файлов
            LinkedList<File> listOfFiles;
            if (filesArray != null) {
                listOfFiles = new LinkedList<>();
                listOfFiles.addAll(Arrays.asList(filesArray)); //создание списка из массива файлов
            }
            else {
                System.out.println("Directory is empty!");
                return;
            }
            try (Writer fileWriterUnion = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileUnion), StandardCharsets.UTF_8))) { //try с ресурсами. Создание потока для записи в объединенный файл
                //Если пользователь выбрал пунк с удалением строк по набору символов
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                if (isDelete) {
                    System.out.println("Enter combination of characters: ");
                    String combinationOfChars = reader.readLine(); //чтение из консоли набора символов для удаления строк
                    for (File file : listOfFiles) {
                        System.out.println("Checking: " + file.getName());
                        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)); //создание потока для чтения для чтения из файла строк
                        String line;
                        File fileTemp = new File("D://Files//Union File//temp.txt"); //создание временного файла
                        Writer fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileTemp), StandardCharsets.UTF_8)); //создание потока для записи во временный файл
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
                        //Удаление оригинального файла
                        file.delete();
                        //Переименовка временного файла в имя файла оригинального
                        fileTemp.renameTo(file);
                    }
                    System.out.println(countDeleteLines + " lines is deleted");
                }
                //Если пользователь не выбрал пунк с удалением строк по набору символов
                else {
                    for (File file: listOfFiles) {
                        BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)); //создание потока для чтения для чтения из файла строк
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

    //метод создания рандомной даты
    private static String randomDate() {
        String europeanDatePattern = "dd.MM.yyyy"; // шаблон для формата даты
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern(europeanDatePattern);

        LocalDate currentDate = LocalDate.now();

        int year = (int) (Math.random() * ((currentDate.getYear() - currentDate.minusYears(5).getYear()))) + 1
                + currentDate.minusYears(5).getYear(); // рандомный год в промежутке 5 лет
        int day = (int) (Math.random() * (31 - 1) + 1) + 1; //рандомный день месяца
        int month = (int) (Math.random() * (12 - 1) + 1) + 1; //рандомный месяц

        //если текущий год равен сгенерированному и текущий месяц меньше или равен сгенерированному и текущий день
        //меньше сгенерированного, то записываем текущую дату, так как рандомно сгенерированная еще не наступила
        if (currentDate.getYear() == year && currentDate.getMonthValue() <= month && currentDate.getDayOfMonth() < day) {
            month = currentDate.getMonthValue();
            day = currentDate.getDayOfMonth();
        } else
            if (currentDate.getYear() == year && currentDate.getMonthValue() < month)
                month = currentDate.getMonthValue() - 1;

            if (month == 2 && day == 29 && (year == 2020 || year == 2016)) //проверка високосного года
                return LocalDate.of(year, month, day).format(europeanDateFormatter);
            else
                if (month == 2 && day > 28) //в феврале не может быть больше 28 дней
                day = day - 3;
            else {
                //Если в месяце 30 дней, а случайное значение поля day приняло значение 31, то отнять один день
                if (((month  == 2 || month  == 4 || month  == 6 || month  == 9 || month  == 11)) && day == 31) {
                    day = day - 1;
                }
            }
            return LocalDate.of(year, month, day).format(europeanDateFormatter);
    }

    //метод создания 10 символов латиницы
    private static String randomLatinCharacters() {
        StringBuilder randomLatinCharacters = new StringBuilder();
        int character;
        for (int index = 0; index < 10; index++) {
            int choice = (int)(Math.random() * ((2 - 1) + 1)) + 1; //переменная для случайного выбора между заглавными и прописными буквами
            switch (choice) {
                case 1:
                    character = (int)(Math.random() * ((90 - 65) + 1)) + 65; //Рандомная заглавная латинская буква
                    randomLatinCharacters.append((char)character);
                    break;
                case 2:
                    character = (int)(Math.random() * ((122 - 97) + 1)) + 97; //Рандомная прописная латинская буква
                    randomLatinCharacters.append((char)character);
                    break;
            }
        }
        return randomLatinCharacters.toString();
    }

    //метод для создания 10 букв кириллицы
    private static String randomRussianCharacters() {
        StringBuilder randomRussianCharacters = new StringBuilder();
        int character;
        for (int index = 0; index < 10; index++) {
            character = (int)(Math.random() * ((1103 - 1040) + 1)) + 1040;
            randomRussianCharacters.append((char)character);
        }
        return randomRussianCharacters.toString();
    }

    //метод создания рандомного четного положительного целого числа
    private static String randomPositiveEvenIntegerNumber() {
        return String.valueOf((int) ((Math.random() * ((50000000 - 1) + 1)) + 1) * 2);
    }

    //метод создания рандомного положительного вещественного числа
    private static String randomPositiveDoubleNumber() {
        double randomPositiveDoubleNumber = (Math.random() * ((19 - 1) + 1)) + 1;
        return String.format("%.8f", randomPositiveDoubleNumber); //формат записи 8 цифр после точки
    }
}
