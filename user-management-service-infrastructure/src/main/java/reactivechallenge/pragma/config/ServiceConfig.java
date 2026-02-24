package reactivechallenge.pragma.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactivechallenge.pragma.api.ISubscriptionServicePort;
import reactivechallenge.pragma.service.SubscriptionService;
import reactivechallenge.pragma.spi.IBootcampServicePort;
import reactivechallenge.pragma.spi.ISubscriptionRepositoryPort;

@Configuration
public class ServiceConfig {
    @Bean
    public ISubscriptionServicePort createSubscriptionServiceBean(IBootcampServicePort bootcampService
            , ISubscriptionRepositoryPort subscriptionRepositoryPort){
        return new SubscriptionService(bootcampService, subscriptionRepositoryPort);
    }
}
