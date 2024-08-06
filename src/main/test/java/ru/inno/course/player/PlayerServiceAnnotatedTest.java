package ru.inno.course.player;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ru.inno.course.player.ext.GeneratePlayers;
import ru.inno.course.player.ext.TestDataResolver;
import java.util.Collection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;

@ExtendWith({TestDataResolver.class})
public class PlayerServiceAnnotatedTest {

    @Test
    @DisplayName("Проверить запуск с пустым хранилищем")
    public void runWithEmptyFile(PlayerService service) {
        Collection<Player> listBefore = service.getPlayers();
        assertEquals(0, listBefore.size());
    }

    @Test
    @GeneratePlayers(1000)
    @DisplayName("Проверить запуск с 1000 пользователей")
    public void loadTest(PlayerService service) {
        Collection<Player> listBefore = service.getPlayers();
        assertEquals(1000, listBefore.size());
    }
}
