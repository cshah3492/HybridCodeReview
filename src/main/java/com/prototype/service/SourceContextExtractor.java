package com.prototype.service;

import com.prototype.model.Finding;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class SourceContextExtractor {

    private static final int CONTEXT_LINES_BEFORE = 20;
    private static final int CONTEXT_LINES_AFTER = 20;

    private final Path sourceRoot;

    public SourceContextExtractor(String sourceRoot) {
        this.sourceRoot = Paths.get(sourceRoot);
    }

    public String extract(Finding finding) {
        try {
            Path sourcePath = resolveSourcePath(finding);

            if (sourcePath == null || !Files.exists(sourcePath)) {
                return "Source code could not be located for: "
                        + finding.getClassName();
            }

            List<String> lines = Files.readAllLines(
                    sourcePath,
                    StandardCharsets.UTF_8
            );

            if (lines.isEmpty()) {
                return "Source file is empty: " + sourcePath;
            }

            int targetLine = finding.getStartLine();

            if (targetLine <= 0) {
                targetLine = 1;
            }

            int startIndex = Math.max(
                    0,
                    targetLine - 1 - CONTEXT_LINES_BEFORE
            );

            int endIndex = Math.min(
                    lines.size(),
                    targetLine + CONTEXT_LINES_AFTER
            );

            StringBuilder context = new StringBuilder();

            context.append("File: ")
                   .append(sourcePath)
                   .append("\n");

            context.append("Target class: ")
                   .append(finding.getClassName())
                   .append("\n");

            context.append("Target method: ")
                   .append(finding.getMethodName())
                   .append("\n\n");

            for (int i = startIndex; i < endIndex; i++) {
                context.append(String.format(
                        "%4d: %s%n",
                        i + 1,
                        lines.get(i)
                ));
            }

            return context.toString();

        } catch (IOException e) {
            return "Unable to read source code: " + e.getMessage();
        }
    }

    private Path resolveSourcePath(Finding finding)
            throws IOException {

        String className = finding.getClassName();

        if (className != null && !className.trim().isEmpty()) {
            String topLevelClass = className.split("\\$")[0];

            String relativePath = topLevelClass
                    .replace('.', '/')
                    + ".java";

            Path classBasedPath = sourceRoot.resolve(relativePath);

            if (Files.exists(classBasedPath)) {
                return classBasedPath;
            }
        }

        String sourceFile = finding.getSourceFile();

        if (sourceFile == null || sourceFile.trim().isEmpty()) {
            return null;
        }

        try (Stream<Path> paths = Files.walk(sourceRoot)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path ->
                            path.getFileName()
                                    .toString()
                                    .equals(sourceFile)
                    )
                    .findFirst()
                    .orElse(null);
        }
    }
}