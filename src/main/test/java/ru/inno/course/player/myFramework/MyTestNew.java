package ru.inno.course.player.myFramework;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.NoSuchElementException;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

public class MyTestNew {

    public static void main(String[] args) throws Exception {
        Files.deleteIfExists(Path.of("./data.json"));// удаление файла перед запуском теста
        String nickname = "Nikita";// + System.currentTimeMillis();//randome
        PlayerService service = new PlayerServiceImpl();

        //test 1
        /**
         * 1. добавить игрока  - проверить наличие в списке
         *    - получить список игроков
         *    - создать игроков
         *    - запросить по нему информацию / получить список
         *    - проверить, что отображается инфо (ник, id, 0 очков, online)
         */
        //получить список игроков
        boolean test1Result = test("1. добавить игрока  - проверить наличие в списке", () -> {
            Collection<Player> listBefore = service.getPlayers();
            if (listBefore.size() != 0) {
                throw new Exception("При пустом файле получили непустой список");
            }
            int nicknameId = service.createPlayer(nickname);
            Player playerById = service.getPlayerById(nicknameId);
            boolean result = playerById.getId() == nicknameId
                && playerById.getPoints() == 0
                && playerById.isOnline()
                && playerById.getNick().equals(nickname);
            System.out.println(": Тест успешен");
        });
        boolean test2Result = test("2. создать дубликат (имя уже занято)", () -> {
            try {
                //- создать игрока с таким же именем
                int duplicatId = service.createPlayer(nickname);
                //- получить сообщение об ошибке (Nickname is already in use:)
                System.out.println("Тест провален! Создали дубликат: " + duplicatId);
            } catch (IllegalArgumentException iae) {
                System.out.println(": Тест успешен");
            }

        });
        boolean test3Result = test("3. получить игрока по id, которого нет", () -> {
            try {
                Player noSuchUser = service.getPlayerById(9999);
                throw new Exception("Тест провален! Получили игрока по id: "+ 9999);
                // проверить что объект не существует

            } catch (NoSuchElementException nsee) {
                System.out.println(": Тест успешен");
            }
        });

    }

    public static boolean test(String testName, TestFunction testFunction) {
        System.out.printf("\n  Выполни тест: " + testName);
        try {
            testFunction.runTest();
            return true;
        } catch (Exception e) {
            System.out.println("Тест провален, поймали странное исключение");
            return false;
        }
    }
}
