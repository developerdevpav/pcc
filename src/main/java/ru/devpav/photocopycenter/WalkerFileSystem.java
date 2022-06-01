package ru.devpav.photocopycenter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class WalkerFileSystem {
    
    private Config config;

    public WalkerFileSystem(Config config) {
        this.config = config;
    }


    public Stream<File> getTargetFiles(Predicate<Path> predicate) {
        try {
            return Files.walk(config.getFrom())
                    .filter(predicate)
                    .map(Path::toFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
