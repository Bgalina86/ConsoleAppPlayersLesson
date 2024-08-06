package ru.inno.course.player.myFramework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.NoSuchElementException;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

public class MyTestOld {

    public static void main(String[] args) throws Exception {
        Files.deleteIfExists(Path.of("./data.json"));// удаление файла перед запуском теста
        String nickname = "Nikita";// + System.currentTimeMillis();//randome
        PlayerService service = new PlayerServiceImpl();
        System.out.println("**** TEST 1 ****");
        //test 1
        /**
         * 1. добавить игрока  - проверить наличие в списке
         *    - получить список игроков
         *    - создать игроков
         *    - запросить по нему информацию / получить список
         *    - проверить, что отображается инфо (ник, id, 0 очков, online)
         */
        //получить список игроков
        try {
            Collection<Player> listBefore = service.getPlayers();
            if (listBefore.size() != 0) {
                throw new Exception("При пустом файле получили непустой список");
            }
            //создать игроков
            int nicknameId = service.createPlayer(nickname);
            //запросить по нему информацию
            Player playerById = service.getPlayerById(nicknameId);
            //проверить, что отображается инфо (ник, id, 0 очков, online)
            //Старый вариант
//        System.out.println(
//            "У игрока id равен " + nicknameId + ": " + (playerById.getId() == nicknameId));
//        System.out.println("У игрока 0 очков: " + (playerById.getPoints() == 0));
//        System.out.println("Игрок онлайн: " + (playerById.isOnline() == true));
//        System.out.println("Ник игрока сохранен правильно " + nickname + ": " + playerById.getNick()
//            .equals(nickname));
            boolean result = playerById.getId() == nicknameId && playerById.getPoints() == 0
                && playerById.isOnline() && playerById.getNick().equals(nickname);
            if (result) {
                System.out.println("Тест успешен");
            } else {
                throw new Exception("Тест провален" + playerById);
            }
        } catch (Exception e) {
            System.out.println("Тест провален, поймали странное исключение");
        }

        System.out.println("**** TEST 2 ****");
        /**
         * test 2
         * 2. создать дубликат (имя уже занято) (9)
         *  - создать игрока см. тест 1
         *  - создать игрока с таким же именем
         *  - получить сообщение об ошибке (Nickname is already in use:)
         */
        try {
            //- создать игрока с таким же именем
            int duplicatId = service.createPlayer(nickname);
            //- получить сообщение об ошибке (Nickname is already in use:)
            System.out.println("Тест провален! Создали дубликат: " + duplicatId);

        } catch (IllegalArgumentException iae) {
            System.out.println("Тест успешен");
        } catch (Exception e) {
            System.out.println("Тест провален, поймали странное исключение");
        }

        System.out.println("**** TEST 3 ****");
        //3. получить игрока по id, которого нет (7)
        // запрос с большим id
        try {
            Player noSuchUser = service.getPlayerById(9999);
            // проверить что объект не существует
            System.out.println("Тест провален! Получили игрока по id: " + 9999);
        } catch (NoSuchElementException nsee) {
            System.out.println("Тест успешен");
        } catch (Exception e) {
            System.out.println("Тест провален, поймали странное исключение");
        }


    }
}
