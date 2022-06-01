package ru.devpav.photocopycenter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreatinTimeNameBuilder implements FileNameBuilder {

    public CreatinTimeNameBuilder() {}

    @Override
    public String buildName(Path path) {
        final Date dateCreationDate = getFileCreationDate(path);
        
        return getFolderName(dateCreationDate);
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

}
