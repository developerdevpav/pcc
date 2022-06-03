package ru.devpav.photocopycenter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MoverFile {

    public void move(File file, Path destinationFolder) {

        if (Files.notExists(destinationFolder)) {
            try {
                Files.createDirectories(destinationFolder);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        final String fileName = file.getName();
        final Path destinationFile = destinationFolder.resolve(fileName);

        try (final BufferedInputStream in = new BufferedInputStream(Files.newInputStream(file.toPath()))) {
            Files.copy(in, destinationFile);
        } catch (IOException ignored) {
        }
    }

}
