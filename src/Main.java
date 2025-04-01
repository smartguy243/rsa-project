
import client.Client;
import server.Server;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        // Инициализация сервера
        Server server = new Server();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в демонстрацию работы с ЭЦП!");
        System.out.print("Введите уникальный ID клиента: ");
        String clientId = scanner.nextLine();

        // Регистрация клиента
        Client client = new Client(clientId);
        server.registerClient(clientId, client.getPublicKey());

        do {
        System.out.println("Выберите сценарий:");
        System.out.println("1. Клиент подписывает сообщение и отправляет его на сервер.");
        System.out.println("2. Клиент проверяет случайное сообщение от сервера.");
        System.out.print("Введите номер сценария (1 или 2): ");

        int scenario = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера


            switch (scenario) {
                case 1:
                    runScenario1(server, client, scanner);
                    break;
                case 2:
                    runScenario2(server, client, scanner);
                    break;
                default:
                    System.out.println("Неверный выбор. Пожалуйста, выберите 1 или 2.");

                    scanner.close();
            }

        } while (true);
    }

    private static void runScenario1(Server server, Client client, Scanner scanner) throws Exception {
        System.out.println("\n--- Сценарий 1: Клиент подписывает сообщение и отправляет его на сервер ---");

        // Шаг 1: Клиент создает сообщение
        System.out.print("Введите сообщение для отправки на сервер: ");
        String message = scanner.nextLine();

        // Установка публичного ключа сервера клиенту
        client.setServerPublicKey(server.getPublicKey());

        // Шаг 2: Клиент подписывает сообщение
        String signedMessage = client.createSignedMessage(message);
        System.out.println("Подписанное сообщение: " + signedMessage);

        // Шаг 3: Сервер проверяет подпись
        String[] parts = signedMessage.split("\\|");
        boolean isVerified = server.verifyClientMessage(client.getClientId(), parts[0], parts[1]);

        if (isVerified) {
            System.out.println("Сервер: Подпись верна. Сообщение успешно принято.");
        } else {
            System.out.println("Сервер: Подпись неверна. Сообщение отклонено.");
        }
    }

    private static void runScenario2(Server server, Client client, Scanner scanner) throws Exception {
        System.out.println("\n--- Сценарий 2: Клиент проверяет случайное сообщение от сервера ---");

        // Шаг 1: Сервер генерирует случайное сообщение
        System.out.print("Введите случайное сообщение для сервера (или оставьте пустым для автоматической генерации): ");
        String randomMessage = scanner.nextLine();
        if (randomMessage.isEmpty()) {
            randomMessage = "Случайное сообщение от сервера";
            System.out.println("Сервер сгенерировал сообщение: " + randomMessage);
        }

        // Шаг 2: Сервер подписывает сообщение
        String serverSignedMessage = server.createSignedMessage(randomMessage);
        System.out.println("Сервер отправил подписанное сообщение: " + serverSignedMessage);

        // Шаг 3: Клиент проверяет подпись
        String[] parts = serverSignedMessage.split("\\|");
        boolean isVerified = client.verifyServerMessage(parts[0], parts[1]);

        if (isVerified) {
            System.out.println("Клиент: Подпись верна. Сообщение успешно проверено.");
        } else {
            System.out.println("Клиент: Подпись неверна. Сообщение отклонено.");
        }
    }
}