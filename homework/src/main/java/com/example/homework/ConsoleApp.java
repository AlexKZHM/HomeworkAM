package com.example.homework;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {

    private final UserDao userDao = new UserDaoImpl();
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        while (true) {
            printMenu();
            String choice = scanner.nextLine().trim();
            try {
                switch (choice) {
                    case "1":
                        createUser();
                        break;
                    case "2":
                        listUsers();
                        break;
                    case "3":
                        findUser();
                        break;
                    case "4":
                        updateUser();
                        break;
                    case "5":
                        deleteUser();
                        break;
                    case "0":
                        System.out.println("Выход...");
                        HibernateUtil.shutdown();
                        return;
                    default:
                        System.out.println("Нет такой команды, попробуй ещё раз.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("USER SERVICE");
        System.out.println("1 - Добавить пользователя");
        System.out.println("2 - Список всех пользователей");
        System.out.println("3 - Найти пользователя по id");
        System.out.println("4 - Обновить пользователя");
        System.out.println("5 - Удалить пользователя");
        System.out.println("0 - Выход");
        System.out.print("Выбор: ");
    }

    private void createUser() {
        System.out.print("Имя (не может быть пустым): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Ошибка: Имя не может быть пустым!");
            return;
        }

        System.out.print("Email (формат example@mail.com): ");
        String email = scanner.nextLine().trim();
        if (!email.contains("@") || !email.contains(".")) {
            System.out.println("Ошибка: Похоже на неверный email (нет @ или .)");
            return;
        }

        System.out.print("Возраст (число): ");
        String ageInput = scanner.nextLine().trim();
        int age;
        try {
            age = Integer.parseInt(ageInput);
            if (age < 0 || age > 120) {
                System.out.println("Ошибка: Возраст должен быть от 0 до 120.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Возраст должен быть числом!");
            return;
        }

        userDao.save(new User(name, email, age));
        System.out.println("Пользователь добавлен.");
    }

    private void listUsers() {
        List<User> users = userDao.findAll();
        if (users.isEmpty()) {
            System.out.println("Пользователей пока нет.");
        } else {
            users.forEach(System.out::println);
        }
    }

    private void findUser() {
        System.out.print("id: ");
        User user = userDao.findById(Long.parseLong(scanner.nextLine()));
        System.out.println(user != null ? user : "Пользователь не найден.");
    }

    private void updateUser() {
        System.out.print("id пользователя для обновления: ");
        Long id = Long.parseLong(scanner.nextLine());
        User user = userDao.findById(id);
        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }
        System.out.print("Новое имя (Enter - оставить '" + user.getName() + "'): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            user.setName(name);
        }
        System.out.print("Новый email (Enter - оставить '" + user.getEmail() + "'): ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            user.setEmail(email);
        }
        System.out.print("Новый возраст (Enter - оставить " + user.getAge() + "): ");
        String age = scanner.nextLine();
        if (!age.isEmpty()) {
            user.setAge(Integer.parseInt(age));
        }
        userDao.update(user);
        System.out.println("Обновлено: " + user);
    }

    private void deleteUser() {
        System.out.print("id пользователя для удаления: ");
        userDao.delete(Long.parseLong(scanner.nextLine()));
        System.out.println("Удалено.");
    }
}
