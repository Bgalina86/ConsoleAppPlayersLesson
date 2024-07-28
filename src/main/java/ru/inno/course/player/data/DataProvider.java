package ru.inno.course.player.data;

import ru.inno.course.player.model.Player;

import java.io.IOException;
import java.util.Collection;

public interface DataProvider {

    void save(String players) throws IOException;

    Collection<Player> load() throws IOException;

    void save(Collection<Player> values);
}
