package co.com.bancolombia.iastpatternconfig.config;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class PatternModelConfig {

    // Profundidad de busqueda
    public static final int MAX_DEPTH = 1;
    private static final Logger log = LoggerFactory.getLogger(PatternModelConfig.class);

    @Bean
    public Map<String, PatternModel> configModels(
            @Value("${iast-pattern.config.files}") String configFiles) throws IOException {
        var mapper = new ObjectMapper(new YAMLFactory())
            //Ignorar propiedades no mapeadas durante la deserialización
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try (Stream<Path> stream = Files.walk(Paths.get(configFiles), MAX_DEPTH)) {
            return stream
                    .parallel()
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".yaml"))
                    .map(path -> getConfigModel(path, mapper))
                    .collect(Collectors.toUnmodifiableMap(PatternModel::name, Function.identity()));
        }
    }


    @SneakyThrows
    private static PatternModel getConfigModel(Path path, ObjectMapper mapper) {
        log.info("Reading config file: {}", path.getFileName());
        return mapper.readValue(Files.readAllBytes(path), PatternModel.class);
    }
}
