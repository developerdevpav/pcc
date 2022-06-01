package ru.devpav.photocopycenter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IniterFileSystem {

    private final Config config;

    public IniterFileSystem(Config config) {
        this.config = config;
    }


    public void init() {
        final Path from = config.getFrom();
        
        if (Files.notExists(from)) {
            throw new RuntimeException("Directory " + from.toAbsolutePath().toString() + " not found. Please checks correctness of the path.");
        } 

        Path to = config.getTo();
        
        createDirectoryIfNotExists(to);
    }


    private static void createDirectoryIfNotExists(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectory(path);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}