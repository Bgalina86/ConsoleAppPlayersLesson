package ru.inno.course.player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.logging.FileHandler;
import javax.swing.text.html.HTMLDocument.HTMLReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.inno.course.player.ext.MyTestWatcher;
import ru.inno.course.player.ext.TestDataResolver;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

@ExtendWith({TestDataResolver.class})
public class PlayerServiceTestsForWorkingWithFile {

    // ����� �� ������ � ������
    @DisplayName("����� �� ������ � ������")
    @ExtendWith(value = {MyTestWatcher.class, TestDataResolver.class})
    public class FileGeneratorTest {

        private PlayerService service;
        private final static String PLAYER_NAME = "Player name";
        private final static String FILE_CONTENT = """
            [{"id":1,"nick":"Player name_1","points":10,"online":true},{"id":2,"nick":"Player name_2","points":20,"online":true},{"id":3,"nick":"Player name_3","points":30,"online":true}]
            """.trim();
        private static Path filePath;

        @BeforeEach
        public void setUp() {
            filePath = Path.of("data.json");
            service = new PlayerServiceImpl();
        }

        @Test
        @Tag("CRITICAL")
        @DisplayName("���������� � 8 ���������, ��� ��������� ����������� json-���� (����� �������� ��������� �����, ��������� �����)")
        public void shouldSaveFileProperly() throws IOException {
            int p1 = service.createPlayer(PLAYER_NAME + "_1");
            int p2 = service.createPlayer(PLAYER_NAME + "_2");
            int p3 = service.createPlayer(PLAYER_NAME + "_3");

            service.addPoints(p1, 10);
            service.addPoints(p2, 20);
            service.addPoints(p3, 40); //30

            List<String> lines = Files.readAllLines(filePath);
            assertEquals(1, lines.size());

            String fileContentAsIs = lines.get(0);
            assertEquals(FILE_CONTENT, fileContentAsIs);
        }

        @Test
        @DisplayName("���������� 9 ���������, ��� ��������� �������� json-���� (�� ������� ���������� json-���� � ������� ��� ��������� ���������)")
        public void shouldLoadFileProperly() throws IOException {
            Files.write(filePath, FILE_CONTENT.getBytes());
            service = new PlayerServiceImpl();
            assertEquals(1, service.getPlayers().size()); // 3
            assertEquals(PLAYER_NAME + "_1", service.getPlayerById(1).getNick());
            assertEquals(10, service.getPlayerById(1).getPoints());

            assertEquals(PLAYER_NAME + "_2", service.getPlayerById(2).getNick());
            assertEquals(20, service.getPlayerById(2).getPoints());

            assertEquals(PLAYER_NAME + "_3", service.getPlayerById(3).getNick());
            assertEquals(30, service.getPlayerById(3).getPoints());
        }

        @Test
        @DisplayName("���������� 8 ���������, ��� ��������� ��������� json-���� (������ ���������)")
        public void shouldSaveEmptyCollectionProperly() throws IOException {
            int newId = service.createPlayer(PLAYER_NAME);
            service.deletePlayer(newId);
            assertEquals(0, service.getPlayers().size());

            List<String> lines = Files.readAllLines(filePath);
            assertEquals(1, lines.size());

            String fileContentAsIs = lines.get(0);
            assertEquals("[]", fileContentAsIs);
        }

        @Test
        @DisplayName("���������, ��� ��������� �������� json-���� (������ ���������)")
        public void shouldLoadEmptyCollectionProperly() throws IOException {
            Files.write(filePath, "[]".getBytes());
            service = new PlayerServiceImpl();
            assertEquals(0, service.getPlayers().size());
        }

        //���������
        String NICKNAME="nr";
        @Test
        @Tag("����������")//���������� ����� ����������
        @DisplayName("���������� �4 (���� json-����) �������� ������ (1, 6)")
        public void iCanAddFileJSON() {
            Collection<Player> listBefore = service.getPlayers();
            assertTrue(listBefore.isEmpty());

            int nikitaId = service.createPlayer("Player1");
            Player playerById = service.getPlayerById(nikitaId);
            assertEquals(NICKNAME, playerById.getNick());
        }
        @Test
        @Tag("����������")//���������� ����� ����������
        @DisplayName("���������� �8 ��������� ������������ ���������� � ���� (1, 4, 6)")
        public void iCanCorrectSavingToFile(){
            Collection<Player> listBefore = service.getPlayers();
            assertTrue(listBefore.isEmpty());

            int nikitaId = service.createPlayer("Player1");
            Player playerById = service.getPlayerById(nikitaId);
            assertEquals(NICKNAME, playerById.getNick());
        }

        // @DisplayName("���������� �9 ��������� ������������ �������� json-����� - �� �������� ������ (7)- �� "������" ������ (4, 7)")
        // @DisplayName("���������� �7. ��������� �������� ������� � ������ json-������ (7))
        // @DisplayName("���������� �8. ��������� ������������ �������� json-�����- ���� ��������� (9))
    }
}
