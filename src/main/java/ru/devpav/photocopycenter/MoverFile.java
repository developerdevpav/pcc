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
                Files.createDirectory(destinationFolder);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        final String fileName = file.getName();
        final Path destinationFile = destinationFolder.resolve(fileName);

        try (final BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            Files.copy(in, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
