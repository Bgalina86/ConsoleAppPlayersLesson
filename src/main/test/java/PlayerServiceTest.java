import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.inno.course.player.data.DataProvider;
import ru.inno.course.player.data.DataProviderJSON;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

public class PlayerServiceTest {

    private PlayerService service;
    private static final String NICKNAME = "Nikita";

    // hooks - хуки
    @BeforeEach
    public void setUp() {
        service = new PlayerServiceImpl();
    }

    @AfterEach
    public void tearDown() {
        try {
            try {
                Files.deleteIfExists(Path.of("./data.json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {

        }
    }

    @Test
    @Tag("позитивные")
    @DisplayName("позитивные №1 Создаем игрока и проверяем его значения по дефолту + позитивные №7 (добавить игрока) - получить игрока по id (3, 5)")
    public void iCanAddNewPlayer() {
        Collection<Player> listBefore = service.getPlayers();
        assertEquals(0, listBefore.size());

        int nikitaId = service.createPlayer(NICKNAME);
        Player playerById = service.getPlayerById(nikitaId);

        assertEquals(nikitaId, playerById.getId());
        assertEquals(0, playerById.getPoints());
        assertEquals(NICKNAME, playerById.getNick());
        assertTrue(playerById.isOnline());
    }

    @Test
    @Tag("позитивные")
    @DisplayName("позитивные №2 (добавить игрока) - удалить игрока - проверить отсутствие в списке (2,3)")
    public void iCanDeletePlayer() {
        Collection<Player> listBefore = service.getPlayers();
        assertTrue(listBefore.isEmpty());

        int nikitaId = service.createPlayer(NICKNAME);
        Collection<Player> listAfterAdd = service.getPlayers();
        assertFalse(listAfterAdd.isEmpty());

        Player playerById = service.getPlayerById(nikitaId);

        assertEquals(nikitaId, playerById.getId());
        assertEquals(0, playerById.getPoints());
        assertEquals(NICKNAME, playerById.getNick());
        assertTrue(playerById.isOnline());

        playerById = service.deletePlayer(nikitaId);
        assertEquals(nikitaId, playerById.getId());

        Collection<Player> listAfter = service.getPlayers();
        assertTrue(listAfter.isEmpty());
    }

    @Test
    @Tag("позитивные")
    @DisplayName("позитивные №7 (добавить игрока) - получить игрока по id")
    public void iCanAddPlayer() {
        Collection<Player> listBefore = service.getPlayers();
        assertTrue(listBefore.isEmpty());

        int nikitaId = service.createPlayer(NICKNAME);
        Collection<Player> listAfterAdd = service.getPlayers();
        assertFalse(listAfterAdd.isEmpty());

        Player probe = service.getPlayerById(nikitaId);
        assertEquals(NICKNAME, probe.getNick());
    }


    @ParameterizedTest
    @ArgumentsSource(ru.inno.course.player.ext.PointsProvider.class)
    @Tag("позитивные")
    @DisplayName("позитивные №5 Добавление очков игроку")
    public void iCanAddPoints2(int pointsToAdd, int pointsToBe) {

        int playerId = service.createPlayer(NICKNAME);
        service.addPoints(playerId, pointsToAdd);
        Player playerById = service.getPlayerById(playerId);
        assertEquals(pointsToBe, playerById.getPoints());
    }

    @ParameterizedTest
    @Tag("позитивные")
    @ArgumentsSource(ru.inno.course.player.ext.PlayersAndPointsProvider.class)
    @DisplayName("позитивные №6 Добавление очков игроку c ненулевым балансом")
    public void iCanAddPoints3(Player player, int pointsToAdd, int pointsToBe) {
        int id = service.createPlayer(player.getNick());
        service.addPoints(id, player.getPoints());

        service.addPoints(id, pointsToAdd);
        Player playerById = service.getPlayerById(id);
        assertEquals(pointsToBe, playerById.getPoints());
    }

    @Test
    @Tag("позитивные")
    @DisplayName("позитивные №10 Проверить, что id всегда уникальный. Создать 5, удалить 3-го, добавить еще одного -> id == 6 (а не 3) (1, 2)")
    public void iCanIdUnique5() {

        int playerId1 = service.createPlayer("Player1");
        int playerId2 = service.createPlayer("Player2");
        int playerId3 = service.createPlayer("Player3");
        int playerId4 = service.createPlayer("Player4");
        int playerId5 = service.createPlayer("Player5");

        Collection<Player> listAfterAdd = service.getPlayers();
        assertFalse(listAfterAdd.isEmpty());

        service.deletePlayer(3);
        int playerId6 = service.createPlayer("Player6");
        Player probeAddPlayer = service.getPlayerById(playerId6);
        assertThrowsExactly(NoSuchElementException.class, () -> {
            service.getPlayerById(3);
        });
        assertEquals(playerId6, probeAddPlayer.getId());

    }

    @Test
    @Tag("позитивные")
    @DisplayName("позитивные №3 (нет json-файла) добавить игрока (1, 6, 8)")
    public void iCanAddPlayer1() {
        Player player1 = new Player();
        player1.setNick("Player 1");
        player1.setId(1);

        assertEquals(1, player1.getId());
    }

    @Test
    @Tag("позитивные")// сделать через service
    @DisplayName("позитивные №12 Проверить создание игрока с 15 символами (10)")
    public void iCanAddPlayer15Size() {
        Player player1 = new Player();
        player1.setNick("012345678901234");
        player1.setId(1);
        String playerNik = player1.getNick();
        int sizePlayerNik = playerNik.length();

        assertEquals(sizePlayerNik, 15);
    }

    @Test
    @Tag("позитивные")
    @DisplayName("позитивные №4 (есть json-файл) добавить игрока (1, 6)")
    public void iCanAddFileJSON() {
        Collection<Player> listBefore = service.getPlayers();
        assertTrue(listBefore.isEmpty());

        int nikitaId = service.createPlayer("Player1");
        Player playerById = service.getPlayerById(nikitaId);
        assertEquals(NICKNAME, playerById.getNick());
    }
    @Test
    @Tag("позитивные")
     @DisplayName("позитивные №8 проверить корректность сохранения в файл (1, 4, 6)")
    public void iCanCorrectSavingToFile(){
        Collection<Player> listBefore = service.getPlayers();
        assertTrue(listBefore.isEmpty());

        int nikitaId = service.createPlayer("Player1");
        Player playerById = service.getPlayerById(nikitaId);
        assertEquals(NICKNAME, playerById.getNick());
    }
    //   @DisplayName("позитивные №9 проверить корректность загрузки json-файла - не потеряли записи (7)- не "побили" записи (4, 7)")

    //   @DisplayName("позитивные №11 (нет json-файла) запросить список игроков (3, 8)")


    //НЕГАТИВНЫЕ ТЕСТЫ
    // @DisplayName("негативный №1. удалить игрока, которого нет - удалить игрока 10, хотя последний - 8 (2, 3))
    // @DisplayName("негативный №4. сохранить игрока с пустым ником (10))
    // @DisplayName("негативный №6. Накинуть очков игроку, которого нет (4))
    // @DisplayName("негативный №7. Накинуть очков без указания id (4))
    // @DisplayName("негативный №8. Ввести невалидный id (String) (5))
    // @DisplayName("негативный №9. Проверить загрузку системы с другим json-файлом (7))
    // @DisplayName("негативный №10. Начислить 1.5 балла игроку (10))
    // @DisplayName("негативный №11. проверить корректность загрузки json-файла- есть дубликаты (9))
    // @DisplayName("негативный №12. Проверить создание игрока с 16 символами (10) - название (что? где? когда?)- шаги воспроизведения (curl)
    //- ожидаемый результат - полученный результат - логи / скрины / видео - приоритет (средний) - серьезность (средняя) - на каком стенде - версия
    //- плановая дата fix'а - компоненты - линки на другие задачи - назначение - автор - причина дефекта - даты - предусловия
    @Test
    @Tag("негативный")
    @DisplayName("негативный №2 Нельзя создать дубликат игрока")
    public void iCannotCreateADuplicate() {
        service.createPlayer(NICKNAME);
        assertThrows(IllegalArgumentException.class, () -> service.createPlayer(NICKNAME));
    }

    @Test
    @Tags({@Tag("негативный"), @Tag("CRITICAL")})
    @DisplayName("негативный №3 Нельзя получить несуществующего пользователя")
    public void iCannotGetEmptyUser() {
        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(9999));
    }

    @ParameterizedTest
    @Tag("негативный")
    @ValueSource(ints = {10, 100, -50, 0, 100, -5000000})
    @DisplayName("негативный №5 Добавление очков игроку")
    public void iCanAddPoints(int points) {
        int playerId = service.createPlayer(NICKNAME);
        service.addPoints(playerId, points);
        Player playerById = service.getPlayerById(playerId);
        assertEquals(points, playerById.getPoints());
    }

}
