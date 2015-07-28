package com.adioss.tiddlyzim;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHelper {
    public FileHelper() {
    }

    File createFile(String fileName, Path root) {
        return new File(root.toString() + "\\" + fileName);
    }

    File createTxtFile(String fileName, Path root) {
        return new File(root.toString() + "\\" + fileName + ".txt");
    }

    Path createDirectory(Path dir) throws IOException {
        deleteDirectoryRecursively(dir.toFile());
        return Files.createDirectory(dir);
    }

    private void deleteDirectoryRecursively(File file) {
        File[] subFiles = file.listFiles();
        if (subFiles != null) {
            for (File subFile : subFiles) {
                deleteDirectoryRecursively(subFile);
            }
        }
        file.delete();
    }
}
