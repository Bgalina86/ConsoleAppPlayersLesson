package ru.inno.course.player.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import ru.inno.course.player.model.Player;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import ru.inno.course.player.service.PlayerService;

public class DataProviderJSON implements DataProvider, PlayerService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final static Path FILEPATH = Path.of("./data.json");

    @Override
    public Player getPlayerById(int id) {
        return null;
    }

    @Override
    public Collection<Player> getPlayers() {
        return null;
    }

    @Override
    public int createPlayer(String nickname) {
        return 0;
    }

    @Override
    public int addPoints(int playerId, int points) {
        return 0;
    }

    @Override
    public Map<Integer, Player> addId(int id, String playerId) {
        return null;
    }

    @Override
    public Player deletePlayer(int id) {
        return null;
    }

    @Override
    public void save(String players) throws IOException {
        mapper.writeValue(FILEPATH.toFile(), players);
    }

    @Override
    public Collection<Player> load() throws IOException {
        return mapper.readValue(FILEPATH.toFile(), new TypeReference<Collection<Player>>(){});
    }

    @Override
    public void save(Collection<Player> values) {

    }
}
