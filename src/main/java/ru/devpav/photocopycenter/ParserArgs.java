package ru.devpav.photocopycenter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ParserArgs {
    
    private Path to;

    private Path from;

    private Set<String> exts;


    public ParserArgs(String[] args) {
        this.parse(args);
    }


    private void parse(String[] args) {
        this.from = Paths.get(args[0]);
        this.to = Paths.get(args[1]);

        exts = Arrays.stream(args[2].split(","))
        .map(String::toLowerCase)
        .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<String> getExts() {
        return exts;
    }

    public Path getFrom() {
        return from;
    }

    public Path getTo() {
        return to;
    }

}
