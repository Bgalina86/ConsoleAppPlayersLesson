package ru.inno.course.player.ext;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MyTestWatcher implements TestWatcher, BeforeAllCallback, AfterAllCallback {

    private Map<String, List<Method>> statuses;
    private static final String SUCCESS = "s";
    private static final String FAIL = "f";
    private static final String ABORT = "a";
    private static final String DISABLE = "d";

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        List<Method> methods = statuses.get(SUCCESS);
        methods.add(context.getTestMethod().get());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

        if (context.getTestMethod().get().isAnnotationPresent(Tags.class)) {
            Tag[] tags = context.getTestMethod().get().getAnnotation(Tags.class).value();
            for (Tag tag : tags) {
                if (tag.value().equalsIgnoreCase("CRITICAL")) {
                    // tg channel alert -> tg bot
                    System.out.println("Упал критичный тест");
                }
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        String head = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>Отчет</title>
                </head>
                <body>     
                    <table>                
                """;
        String tail = """
                        </table>
                    </body>
                </html>
                """;


        String content = "";

        List<Method> num = statuses.get(SUCCESS);
        content += "<tr><td>Success</td><td>"+num+"</td></tr>";

        num = statuses.get(FAIL);
        content += "<tr><td>Failed</td><td>"+num+"</td></tr>";

        num = statuses.get(DISABLE);
        content += "<tr><td>Disabled</td><td>"+num+"</td></tr>";

        num = statuses.get(ABORT);
        content += "<tr><td>Aborted</td><td>"+num+"</td></tr>";

        String report = head + content + tail;

        Files.writeString(Path.of("report.html"), report);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        statuses = new HashMap<>();
        statuses.put(SUCCESS, new ArrayList<>());
        statuses.put(FAIL, new ArrayList<>());
        statuses.put(ABORT, new ArrayList<>());
        statuses.put(DISABLE, new ArrayList<>());
    }
}


// TODO: почему store обнуляется