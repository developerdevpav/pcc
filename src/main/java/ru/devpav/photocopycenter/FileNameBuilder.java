package ru.devpav.photocopycenter;

import java.nio.file.Path;

public interface FileNameBuilder {
    
    public String buildName(Path path);

}
