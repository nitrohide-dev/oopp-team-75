package server.api.configs;

import commons.SubTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.List;

@Configuration
public class ConsumerTracker {
    @Bean
    public HashMap<Long, List<DeferredResult<List<SubTask>>>> consumers(){
        return new HashMap<>();
    }
}