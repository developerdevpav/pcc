package ru.devpav.photocopycenter;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class CounterFile {

    private Map<String, AtomicLong> counterExtension;
    final private Set<String> extensions;

    public CounterFile(Set<String> extensions) {
        this.extensions = extensions;
        this.defaultCounter();
    }

    public synchronized void changeCounter(String extension) {
        this.counterExtension.get(extension.toLowerCase()).incrementAndGet();
    }

    public String getStringResultCounter() {
        return this.counterExtension.entrySet()
                .stream()
                .map(entity -> entity.getKey() + ":" + entity.getValue().get() + "\n")
                .collect(Collectors.joining());
    }

    public void defaultCounter() {
        counterExtension = extensions.stream()
                .collect(Collectors.toMap(key -> key, value -> new AtomicLong(0), (obj, obj1) -> obj));
    }

}
