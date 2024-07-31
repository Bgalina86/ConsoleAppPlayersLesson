package ext;

import com.github.javafaker.Faker;
import java.util.HashSet;
import java.util.Set;
import ru.inno.course.player.model.Player;

public class PlayerGenerator {

    public static Set<Player> generate(int count) {
        Faker faker = new Faker();

        Set<Player> playerSet = new HashSet<>();
        for (int i = 0; i < count; i++) {
            int c = i + 1;
            String username = faker.name().username();
            int points = faker.number().numberBetween(1, 1000);
            boolean isOnline = faker.bool().bool();

            playerSet.add(new Player(c, username, points, isOnline));
        }
        return playerSet;
    }

}
