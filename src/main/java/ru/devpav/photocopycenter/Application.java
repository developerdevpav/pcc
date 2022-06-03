package ru.devpav.photocopycenter;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
import org.apache.commons.io.FilenameUtils;


import java.io.File;
import java.io.IOException;
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

        final CounterFile counterFile = new CounterFile(fileCopyConfig.getExts());

        final Predicate<Path> containsExtensions = (path) -> {
            final String nameFile = path.toAbsolutePath().toString();
            final String extensionFile = FilenameUtils.getExtension(nameFile);
            return fileCopyConfig.getExts().contains(extensionFile.toLowerCase());
        };

        final FileNameBuilder creationTimeNameBuilder = new CreationTimeNameBuilder();

        final MoverFile moverFile = new MoverFile();

        final Consumer<File> moveFileConsumer = file -> {
            final String creationTimeName = creationTimeNameBuilder.buildName(file.toPath());

            final String year = creationTimeName.substring(0, 4);
            final String month = creationTimeName.substring(8);
            final String day = creationTimeName.substring(4, 7).trim();

            final Path rootFolder = fileCopyConfig.getTo().resolve(year);
            final Path destinationFolder = rootFolder.resolve(month).resolve(day);

            moverFile.move(file, destinationFolder);
        };

        final WalkerFileSystem walkerFileSystem = new WalkerFileSystem(fileCopyConfig);
        walkerFileSystem.getTargetFiles(containsExtensions)
                .peek(file -> {
                    String extension = FilenameUtils.getExtension(file.getName());
                    counterFile.changeCounter(extension);
                })
                .forEach(moveFileConsumer);

        counterFile.getStringResultCounter();
        counterFile.defaultCounter();
    }

}
