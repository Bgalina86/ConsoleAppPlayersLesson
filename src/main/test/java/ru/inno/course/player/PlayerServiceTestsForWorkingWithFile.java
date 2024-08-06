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

    // Тесты на работу с файлам
    @DisplayName("Тесты на работу с файлом")
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
        @DisplayName("Позитивный № 8 проверить, что корректно сформирован json-файл (после парсинга итогового файла, коллекции равны)")
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
        @DisplayName("позитивный 9 проверить, что корректно прочитан json-файл (вы создали корректный json-файл и система его правильно прочитала)")
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
        @DisplayName("позитивный 8 проверить, что корректно сохраняет json-файл (пустая коллекция)")
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
        @DisplayName("проверить, что корректно прочитан json-файл (пустая коллекция)")
        public void shouldLoadEmptyCollectionProperly() throws IOException {
            Files.write(filePath, "[]".getBytes());
            service = new PlayerServiceImpl();
            assertEquals(0, service.getPlayers().size());
        }

        //перенесен
        String NICKNAME="nr";
        @Test
        @Tag("позитивные")//переписать найти реализацию
        @DisplayName("позитивные №4 (есть json-файл) добавить игрока (1, 6)")
        public void iCanAddFileJSON() {
            Collection<Player> listBefore = service.getPlayers();
            assertTrue(listBefore.isEmpty());

            int nikitaId = service.createPlayer("Player1");
            Player playerById = service.getPlayerById(nikitaId);
            assertEquals(NICKNAME, playerById.getNick());
        }
        @Test
        @Tag("позитивные")//переписать найти реализацию
        @DisplayName("позитивные №8 проверить корректность сохранения в файл (1, 4, 6)")
        public void iCanCorrectSavingToFile(){
            Collection<Player> listBefore = service.getPlayers();
            assertTrue(listBefore.isEmpty());

            int nikitaId = service.createPlayer("Player1");
            Player playerById = service.getPlayerById(nikitaId);
            assertEquals(NICKNAME, playerById.getNick());
        }

        // @DisplayName("позитивные №9 проверить корректность загрузки json-файла - не потеряли записи (7)- не "побили" записи (4, 7)")
        // @DisplayName("негативный №7. Проверить загрузку системы с другим json-файлом (7))
        // @DisplayName("негативный №8. проверить корректность загрузки json-файла- есть дубликаты (9))
    }
}
