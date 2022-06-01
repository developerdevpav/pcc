package ru.devpav.photocopycenter;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Application {

    public static void main(String[] args) {
        final CheckerArgs cpcca = new CheckerArgs(args);

        if (cpcca.isNotValid()) cpcca.throwException();

        final ParserArgs parserArgs = new ParserArgs(args);

        final ConfigFactory fileCopyConfigFactory = new ConfigFactory(parserArgs);

        final Config fileCopyConfig = fileCopyConfigFactory.buildConfig();

        final IniterFileSystem initerFileSystem = new IniterFileSystem(fileCopyConfig);

        initerFileSystem.init();

        final MoverFile moverFile = new MoverFile();

        final WalkerFileSystem walkerFileSystem = new WalkerFileSystem(fileCopyConfig);

        final FileNameBuilder creationTimeNameBuilder = new CreatinTimeNameBuilder();

        final Predicate<Path> containsExtebsions = (path) -> {
            final String nameFile = path.toAbsolutePath().toString();
            final String extensionFile = FilenameUtils.getExtension(nameFile);
            return fileCopyConfig.getExts().contains(extensionFile.toLowerCase());
        };

        final Consumer<File> moveFileConsumer = file -> {
            final String creationTimeName = creationTimeNameBuilder.buildName(file.toPath());
            final Path destinationFolder = fileCopyConfig.getTo().resolve(creationTimeName);
            
            moverFile.move(file, destinationFolder);
        };

        walkerFileSystem.getTargetFiles(containsExtebsions).forEach(moveFileConsumer);
    }

}
