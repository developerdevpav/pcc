package ru.devpav.photocopycenter;

import java.nio.file.Path;
import java.util.Set;

public class Config {

    final private Path from;
    final private Path to;
    final private Set<String> exts;

    public Config(Path from, Path to, Set<String> extensions) {
        this.from = from;
        this.to = to;
        this.exts = extensions;
    }

    
    public Path getFrom() {
        return from;
    }

    public Path getTo() {
        return to;
    }

    public Set<String> getExts() {
        return exts;
    }

}
