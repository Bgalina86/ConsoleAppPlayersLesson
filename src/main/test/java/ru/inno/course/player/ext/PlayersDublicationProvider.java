package ru.inno.course.player.ext;

import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.inno.course.player.model.Player;

public class PlayersDublicationProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        Player player1 = new Player(1, "Player 1", 0,true);
        Player player2 = new Player(2, "Player 1", 0, false);
        Player player3 = new Player(3, "Player 3", 0, true);
        player3.setPoints(0);

        return Stream.of(
            Arguments.of(player1),
            Arguments.of(player2),
            Arguments.of(player3)
        );
    }

}
