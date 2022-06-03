package ru.devpav.photocopycenter;

import java.nio.file.Path;

public interface FileNameBuilder {
    
    String buildName(Path path);

}
