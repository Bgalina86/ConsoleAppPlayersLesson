import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ext.PlayersAndIdProvider;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

public class PlayerServiceTest {

    private PlayerService service;
    private static final String NICKNAME = "Nikita";

    // hooks - ����
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
    @Tag("����������")
    @DisplayName("���������� �1 ������� ������ � ��������� ��� �������� �� ������� + ���������� �7 (�������� ������) - �������� ������ �� id (3, 5)")
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
    @Tag("����������")
    @DisplayName("���������� �2 (�������� ������) - ������� ������ - ��������� ���������� � ������ (2,3)")
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
    @Tag("����������")
    @DisplayName("���������� �7 (�������� ������) - �������� ������ �� id")
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
    @Tag("����������")
    @DisplayName("���������� �5 ���������� ����� ������")
    public void iCanAddPoints2(int pointsToAdd, int pointsToBe) {

        int playerId = service.createPlayer(NICKNAME);
        service.addPoints(playerId, pointsToAdd);
        Player playerById = service.getPlayerById(playerId);
        assertEquals(pointsToBe, playerById.getPoints());
    }

    @ParameterizedTest
    @Tag("����������")
    @ArgumentsSource(ru.inno.course.player.ext.PlayersAndPointsProvider.class)
    @DisplayName("���������� �6 ���������� ����� ������ c ��������� ��������")
    public void iCanAddPoints3(Player player, int pointsToAdd, int pointsToBe) {
        int id = service.createPlayer(player.getNick());
        service.addPoints(id, player.getPoints());

        service.addPoints(id, pointsToAdd);
        Player playerById = service.getPlayerById(id);
        assertEquals(pointsToBe, playerById.getPoints());
    }

    @Test
    @Tag("����������")
    @DisplayName("���������� �10 ���������, ��� id ������ ����������. ������� 5, ������� 3-��, �������� ��� ������ -> id == 6 (� �� 3) (1, 2)")
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
    @Tag("����������")
    @DisplayName("���������� �3 (��� json-�����) �������� ������ (1, 6, 8)")
    public void iCanAddPlayer1() {
        Player player1 = new Player();
        player1.setNick("Player 1");
        player1.setId(1);

        assertEquals(1, player1.getId());
    }

    @Test
    @Tag("����������")// ������� ����� service
    @DisplayName("���������� �12 ��������� �������� ������ � 15 ��������� (10)")
    public void iCanAddPlayer15Size() {
        Player player1 = new Player();
        player1.setNick("012345678901234");
        player1.setId(1);
        String playerNik = player1.getNick();
        int sizePlayerNik = playerNik.length();

        assertEquals(sizePlayerNik,15);
    }

    @Test
    @Tag("����������")
    @DisplayName("���������� �4 (���� json-����) �������� ������ (1, 6)")
    public void iCanAddFileJSON(){

    }

    //   @DisplayName("���������� �8 ��������� ������������ ���������� � ���� (1, 4, 6)")
    //   @DisplayName("���������� �9 ��������� ������������ �������� json-����� - �� �������� ������ (7)- �� "������" ������ (4, 7)")

    //   @DisplayName("���������� �11 (��� json-�����) ��������� ������ ������� (3, 8)")


    //���������� �����
    // @DisplayName("���������� �1. ������� ������, �������� ��� - ������� ������ 10, ���� ��������� - 8 (2, 3))
    // @DisplayName("���������� �4. ��������� ������ � ������ ����� (10))
    // @DisplayName("���������� �6. �������� ����� ������, �������� ��� (4))
    // @DisplayName("���������� �7. �������� ����� ��� �������� id (4))
    // @DisplayName("���������� �8. ������ ���������� id (String) (5))
    // @DisplayName("���������� �9. ��������� �������� ������� � ������ json-������ (7))
    // @DisplayName("���������� �10. ��������� 1.5 ����� ������ (10))
    // @DisplayName("���������� �11. ��������� ������������ �������� json-�����- ���� ��������� (9))
    // @DisplayName("���������� �12. ��������� �������� ������ � 16 ��������� (10) - �������� (���? ���? �����?)- ���� ��������������� (curl)
    //- ��������� ��������� - ���������� ��������� - ���� / ������ / ����� - ��������� (�������) - ����������� (�������) - �� ����� ������ - ������
    //- �������� ���� fix'� - ���������� - ����� �� ������ ������ - ���������� - ����� - ������� ������� - ���� - �����������
    @Test
    @Tag("����������")
    @DisplayName("���������� �2 ������ ������� �������� ������")
    public void iCannotCreateADuplicate() {
        service.createPlayer(NICKNAME);
        assertThrows(IllegalArgumentException.class, () -> service.createPlayer(NICKNAME));
    }

    @Test
    @Tags({@Tag("����������"), @Tag("CRITICAL")})
    @DisplayName("���������� �3 ������ �������� ��������������� ������������")
    public void iCannotGetEmptyUser() {
        assertThrows(NoSuchElementException.class, () -> service.getPlayerById(9999));
    }

    @ParameterizedTest
    @Tag("����������")
    @ValueSource(ints = {10, 100, -50, 0, 100, -5000000})
    @DisplayName("���������� �5 ���������� ����� ������")
    public void iCanAddPoints(int points) {
        int playerId = service.createPlayer(NICKNAME);
        service.addPoints(playerId, points);
        Player playerById = service.getPlayerById(playerId);
        assertEquals(points, playerById.getPoints());
    }


}
