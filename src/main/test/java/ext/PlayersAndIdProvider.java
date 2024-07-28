package ext;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.inno.course.player.model.Player;

public class PlayersAndIdProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

        // прочитать эксельку
        // достать данные из БД
        // взять переменные окружения
        Player player1 = new Player();
        player1.setNick("Player 1");
        player1.setId(1);

        Player player2 = new Player();
        player2.setNick("Player 2");
        player2.setId(2);

        Player player3 = new Player();
        player3.setNick("Player 3");
        player3.setId(3);

        Player player4 = new Player();
        player4.setNick("Player 4");
        player4.setId(4);

        Player player5 = new Player();
        player5.setNick("Player 5");
        player5.setId(5);

        return Stream.of(
            Arguments.of(1, player1),
            Arguments.of(2, player2),
            Arguments.of(3, player3),
            Arguments.of(4, player4),
            Arguments.of(5, player5)
        );
    }
}