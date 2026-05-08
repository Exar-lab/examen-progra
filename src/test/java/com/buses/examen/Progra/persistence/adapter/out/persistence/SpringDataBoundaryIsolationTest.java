package com.buses.examen.Progra.persistence.adapter.out.persistence;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SpringDataBoundaryIsolationTest {

    private static final String SPRING_DATA_IMPORT = "org.springframework.data";
    private static final Path MAIN_JAVA_PATH = Path.of("src", "main", "java");
    private static final String ADAPTER_PERSISTENCE_SEGMENT = "/adapter/out/persistence/";

    /** Verifica que las importaciones Spring Data queden aisladas en adaptadores de persistencia. */
    @Test
    public void shouldUseSpringDataTypesOnlyInsidePersistenceAdapters() throws IOException {
        try (Stream<Path> javaFiles = Files.walk(MAIN_JAVA_PATH)) {
            final List<String> springDataUsagesOutsidePersistenceAdapters = javaFiles
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .filter(path -> containsImport(path, SPRING_DATA_IMPORT))
                    .map(path -> path.toString().replace('\\', '/'))
                    .filter(path -> !path.contains(ADAPTER_PERSISTENCE_SEGMENT))
                    .toList();

            assertThat(springDataUsagesOutsidePersistenceAdapters)
                    .as("Spring Data imports must remain isolated in adapter/out/persistence")
                    .isEmpty();
        }
    }

    private static boolean containsImport(final Path path, final String importFragment) {
        try {
            return Files.readString(path).contains(importFragment);
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot read source file: " + path, exception);
        }
    }
}
