package ru.inno.course.player.ext;

import com.github.javafaker.Faker;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import ru.inno.course.player.model.Player;

public class PlayersAndListProvider implements ArgumentsProvider {

    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {

            Faker faker = new Faker();

            Set<Player> playerSet = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                int c = i + 1;
                String username = faker.name().username();
                int points = faker.number().numberBetween(1, 5);
                boolean isOnline = faker.bool().bool();

                playerSet.add(new Player(c, username, points, isOnline));
            }
            return (Stream<? extends Arguments>) playerSet;
    }

}
