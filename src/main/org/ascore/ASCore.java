package org.ascore;

import java.io.*;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class ASCore {

    public static void makeProject(String dest, String langName, Template template, Lang lang, boolean log) throws IOException {
        var classLangName = langName.substring(0, 1).toUpperCase() + langName.substring(1);
        var packageName = langName.toLowerCase();

        var resourcePath = "/templates/" + template.name().toLowerCase() + "/" + lang.name().toLowerCase();

        var templateURL = ASCore.class.getResource(resourcePath);
        if (templateURL == null) {
            System.out.println("Files not found for template '" + template.name().toLowerCase() + "'");
            return;
        }
        var templatePath = Paths.get(templateURL.getPath().substring(6));
        var destPath = Paths.get(dest + "/" + packageName);
        if (!destPath.toFile().mkdirs()) {
            System.out.println("The project's directory ('" + destPath + "') couldn't be created or already existed");
        }

        Map<String, String> zipProperties = new HashMap<>();
        /* We want to read an existing ZIP File, so we set this to False */
        zipProperties.put("create", "false");
        zipProperties.put("encoding", "UTF-8");
        URI zipFile = URI.create("jar:file:" + templatePath.toUri().getPath());

        try (FileSystem zipfs = FileSystems.newFileSystem(zipFile, zipProperties)) {
            Path rootPath = zipfs.getPath(resourcePath);

            FileVisitor<Path> templateFileVisitor = new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    Path newDirectory = destPath.resolve(rootPath.relativize(dir).toString());

                    if (newDirectory.toFile().mkdir() && log) {
                        System.out.println("Created directory '" + newDirectory.toFile().getName() + "'");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                    Path newFile = destPath.resolve(rootPath.relativize(path).toString());

                    var file = new File(newFile.toAbsolutePath().toString());
                    if (!file.createNewFile()) {
                        throw new IOException("Couldn't create " + file);
                    }

                    try (var writer = new FileWriter(file);
                         var reader = new Scanner(Objects.requireNonNull(ASCore.class.getResourceAsStream(path.toString())))) {

                        StringBuilder lines = new StringBuilder();
                        while (reader.hasNextLine()) {
                            lines.append(reader.nextLine()
                                            .replaceAll("MyLang", classLangName)
                                            .replaceAll("myLang", langName)
                                            .replaceAll("mylang", packageName))
                                    .append("\n");
                        }
                        writer.write(lines.toString());
                    }
                    if (file.renameTo(new File(file.getAbsolutePath()
                            .replaceAll("MyLang", classLangName)
                            .replaceAll("myLang", langName)))
                        && log) {
                        System.out.println("Created file '" + file.getName() + "' in directory '" + file.getParentFile().getName() + "'");
                    }

                    return FileVisitResult.CONTINUE;
                }
            };
            Files.walkFileTree(rootPath, templateFileVisitor);
        }
    }

    public enum Template {
        BLANK,
        BASIC
    }

    public enum Lang {
        JAVA,
        KOTLIN
    }
}
