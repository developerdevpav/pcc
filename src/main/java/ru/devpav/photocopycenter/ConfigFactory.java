package ru.devpav.photocopycenter;

import java.nio.file.Path;
import java.util.Set;

public class ConfigFactory {

    final private ParserArgs parserArgs;

    public ConfigFactory(ParserArgs parserArgs) {
        this.parserArgs = parserArgs;
    }


    public Config buildConfig() {
        final Set<String> exts = parserArgs.getExts();
        final Path from = parserArgs.getFrom();
        final Path to = parserArgs.getTo();

        return new Config(from, to, exts);
    }

}