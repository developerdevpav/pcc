package ru.devpav.photocopycenter;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Application {

    public static void main(String[] args) {
        final CheckerArgs checkerArgs = new CheckerArgs(args);

        if (checkerArgs.isNotValid()) checkerArgs.throwException();

        final ParserArgs parserArgs = new ParserArgs(args);

        final ConfigFactory fileCopyConfigFactory = new ConfigFactory(parserArgs);

        final Config fileCopyConfig = fileCopyConfigFactory.buildConfig();

        final IniterFileSystem initerFileSystem = new IniterFileSystem(fileCopyConfig);

        initerFileSystem.init();

        final Predicate<Path> containsExtensions = (path) -> {
            final String nameFile = path.toAbsolutePath().toString();
            final String extensionFile = FilenameUtils.getExtension(nameFile);
            return fileCopyConfig.getExts().contains(extensionFile.toLowerCase());
        };

        final FileNameBuilder creationTimeNameBuilder = new CreatinTimeNameBuilder();

        final MoverFile moverFile = new MoverFile();

        final Consumer<File> moveFileConsumer = file -> {
            final String creationTimeName = creationTimeNameBuilder.buildName(file.toPath());
            final Path destinationFolder = fileCopyConfig.getTo().resolve(creationTimeName);
            
            moverFile.move(file, destinationFolder);
        };

        final WalkerFileSystem walkerFileSystem = new WalkerFileSystem(fileCopyConfig);
        walkerFileSystem.getTargetFiles(containsExtensions).forEach(moveFileConsumer);
    }

}
