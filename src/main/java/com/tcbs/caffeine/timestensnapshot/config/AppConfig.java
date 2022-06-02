package com.tcbs.caffeine.timestensnapshot.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.input.SimpleInputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output.DataSourceOutputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.application.port.output.RecordPersistenceOutputPort;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.entity.Scenario;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.policy.ScenarioParser;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.repo.ScenarioRepository;
import com.tcbs.caffeine.timestensnapshot.hexagonal.domain.value.ScenarioId;
import com.tcbs.caffeine.timestensnapshot.hexagonal.framework.adapter.output.FtpRecordPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    RecordPersistenceOutputPort recordPersistenceOutputPort() {
        return new FtpRecordPersistenceAdapter();
    }

    @Bean
    ScenarioParser scenarioParser() {
        return new ScenarioParser();
    }

    @Bean
    ScenarioRepository scenarioRepository() throws IOException {
        ObjectMapper mapper = new JsonMapper();
        ClassPathResource resource = new ClassPathResource("scenario.json");
        TypeReference<List<Scenario>> typeRef = new TypeReference<>(){};
        List<Scenario> scenario = mapper.readValue(resource.getInputStream(), typeRef);
        Map<ScenarioId, Scenario> repo = scenario.stream().collect(Collectors.toMap(Scenario::getId, Function.identity()));
        return new ScenarioRepository() {
            @Override
            public Optional<Scenario> find(ScenarioId id) {
                return Optional.ofNullable(repo.get(id));
            }
        };
    }

    @Bean
    SimpleInputPort simpleInputPort(RecordPersistenceOutputPort outputPort, DataSourceOutputPort dataSource) {
        return new SimpleInputPort(clock(), outputPort, dataSource);
    }

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
