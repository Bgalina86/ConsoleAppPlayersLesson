package ru.inno.course.player.service;

import java.io.IOException;
import java.util.Map;
import ru.inno.course.player.model.Player;

import java.util.Collection;

public interface PlayerService {
    // получить игрока по id
    Player getPlayerById(int id);

    // получить список игроков
    Collection<Player> getPlayers();

    // создать игрока (возвращает id нового игрока)
    int createPlayer(String nickname);

    // удалить игрока по id'шнику, вернет удаленного игрока
  //  Player deletePlayer(Player id);

    // Добавить очков игроку. Возвращает обновленный счет
    int addPoints(int playerId, int points);
    Map<Integer, Player> addId(int id, String playerId);
    Player deletePlayer(int id);

    void save(String nick) throws IOException;
}
