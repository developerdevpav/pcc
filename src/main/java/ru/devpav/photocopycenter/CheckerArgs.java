package ru.devpav.photocopycenter;

import java.util.*;

public class CheckerArgs {

    private RuntimeException exception;


    public CheckerArgs(String[] args) {
        this.test(args);
    }


    private void test(String[] strings) {
        if (Objects.isNull(strings)) {
            exception = new RuntimeException("Args must be is not NULL");
        }

        if (strings.length != 3) {
            exception = new RuntimeException("Args must has 2 values 1: [path to folder with photos] " +
                    "2: [path to folder for copied photos] 3: [extensions jpg,PNG]");
        }

    }

    public boolean isNotValid() {
        return exception != null;
    }

    public void throwException() {
        throw exception;
    }

}
