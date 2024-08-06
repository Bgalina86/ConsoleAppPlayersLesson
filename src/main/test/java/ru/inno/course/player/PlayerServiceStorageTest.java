package ru.inno.course.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.inno.course.player.ext.DbConnectionResolver;
import ru.inno.course.player.ext.PlayerServiceResolver;
import ru.inno.course.player.ext.Players;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;

@ExtendWith({PlayerServiceResolver.class, DbConnectionResolver.class})
public class PlayerServiceStorageTest {

    @Test
    @DisplayName("Проверить запуск с пустым хранилищем")
    public void runWithEmptyFile(@Players(playerNumber = 0) PlayerService service) {
        Collection<Player> listBefore = service.getPlayers();
        assertEquals(0, listBefore.size());
    }

    @Test
    @DisplayName("Проверить запуск с 1000 пользователей")
    public void loadTest(@Players(playerNumber = 1000) PlayerService service) {
        Collection<Player> listBefore = service.getPlayers();
        assertEquals(1000, listBefore.size());
    }
}
