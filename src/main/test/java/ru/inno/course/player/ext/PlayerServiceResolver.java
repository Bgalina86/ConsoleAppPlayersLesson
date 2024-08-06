package ru.inno.course.player.ext;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.helpers.AnnotationHelper;
import ru.inno.course.player.ext.PlayerGenerator;
import ru.inno.course.player.ext.Players;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

public class PlayerServiceResolver implements ParameterResolver, AfterAllCallback {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(PlayerService.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        // спрашиваем аннотацию Players
        Players annotation = AnnotationHelper.findAnnotation(parameterContext.getAnnotatedElement(), Players.class);
        // забираем значение из скобок @Players(0)
        int num = annotation.playerNumber();

        // генерим Х игроков
        Set<Player> generated = PlayerGenerator.generate(num);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(Path.of("./data.json").toFile(), generated);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new PlayerServiceImpl();
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        Files.deleteIfExists(Path.of("./data.json"));
    }
}