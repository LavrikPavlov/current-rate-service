package ru.kazan.currencyrateservice.config.banner;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

import java.io.PrintStream;

public class CustomApplicationBanner implements Banner {

    private static final String BANNER = "\n\n" +
            "██╗      █████╗ ██╗   ██╗██████╗ ██╗██╗  ██╗\n" +
            "██║     ██╔══██╗██║   ██║██╔══██╗██║██║ ██╔╝\n" +
            "██║     ███████║██║   ██║██████╔╝██║█████╔╝\n" +
            "██║     ██╔══██║╚██╗ ██╔╝██╔══██╗██║██╔═██╗     ,---------------------------.\n" +
            "███████╗██║  ██║ ╚████╔╝ ██║  ██║██║██║  ██╗    | (c) Mikhail Kazan P. 2024 |\n" +
            "╚══════╝╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝╚═╝╚═╝  ╚═╝    `---------------------------'";

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println(BANNER);
        out.println();
        infoService(AnsiColor.BRIGHT_BLUE, "Spring Boot", out, SpringBootVersion.getVersion());
        String verApp = environment.getProperty("management.info.app.version");
        verApp = verApp.endsWith("-SNAPSHOT") ? verApp.substring(0, verApp.length() - 9) : verApp;
        infoService(AnsiColor.BRIGHT_BLUE, sourceClass.getSimpleName(), out, verApp);
        out.println();
    }

    private void infoService(AnsiColor color, String text, PrintStream out, String version) {
        String fullName = String.format(" :: %s :: ", text);
        String padding = " ".repeat(Math.max(0, 42 - (version.length() + fullName.length())));
        out.println(
                AnsiOutput.toString(
                        new Object[]{
                                color,
                                fullName,
                                AnsiColor.DEFAULT,
                                padding,
                                AnsiStyle.FAINT,
                                String.format("version: %s", version)
                        })
        );

    }
}
