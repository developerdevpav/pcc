package ru.devpav.photocopycenter;

import org.apache.commons.io.FilenameUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {

    private static Set<String> extensions;

    public static void main(String[] args) {
        final CheckerArgs cpcca = new CheckerArgs(args);

        if (cpcca.isNotValid()) cpcca.throwException();
        

        final Path pathFolderPhoto = Paths.get(args[0]);

        createDirectoryIfNotExists(pathFolderPhoto);

        final Path pathFolderCopyPhoto = Paths.get(args[1]);

        createDirectoryIfNotExists(pathFolderCopyPhoto);

        extensions = Arrays.stream(args[2].split(","))
                .map(String::toLowerCase)
                .collect(Collectors.toCollection(HashSet::new));

        getTargetFiles(pathFolderPhoto)
                .forEach(file -> {
                    final Path destinationFolder = buildFolderPath(file.toPath(), pathFolderCopyPhoto);

                    createDirectoryIfNotExists(destinationFolder);

                    copyFileToTargetFolder(file, destinationFolder);
                });


    }

    private static void copyFileToTargetFolder(File file, Path destinationFolder) {
        final Path destinationFile = destinationFolder.resolve(file.getName());

        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            Files.copy(in, destinationFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Stream<File> getTargetFiles(Path root) {
        try {
            return Files.walk(root)
                    .filter(Application::isExistsExtension)
                    .map(Path::toFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isExistsExtension(Path path) {
        final String nameFile = path.toAbsolutePath().toString();
        final String extensionFile = FilenameUtils.getExtension(nameFile);
        return extensions.contains(extensionFile.toLowerCase());
    }


    private static Path buildFolderPath(Path file, Path destination) {
        final Date from = getFileCreationDate(file);

        final String fileName = getFolderName(from);

        return destination.resolve(fileName);
    }


    private static Date getFileCreationDate(Path path) {
        BasicFileAttributes attrs = null;
        try {
            attrs = Files.readAttributes(path, BasicFileAttributes.class);
        } catch (IOException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
        
        final FileTime fileTime = attrs.creationTime();
        
        return Date.from(fileTime.toInstant());
    }

    private static String getFolderName(Date from) {
        final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        return df.format(from);
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
