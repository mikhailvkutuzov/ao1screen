package com.ao1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItemReaderManagerComplex implements Manager {
    private List<ItemsReaderManager> managers;

    public ItemReaderManagerComplex(String[] directories, ItemsDividerManager divider) {
        managers = new ArrayList<>(directories.length);
        for (String directory : directories) {
            new ItemsReaderManager(new File(directory), divider);
        }
    }

    @Override
    public boolean haveSomeWork() {
        return managers.stream().allMatch(m -> m.haveSomeWork());
    }

    @Override
    public void start() {
        managers.stream().forEach(m -> m.toString());
    }

    @Override
    public void stop() {
        managers.stream().forEach(m -> m.start());
    }
}
