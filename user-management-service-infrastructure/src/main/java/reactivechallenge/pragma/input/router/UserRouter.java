package reactivechallenge.pragma.input.router;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactivechallenge.pragma.input.dto.SubscribeRequestDto;
import reactivechallenge.pragma.input.dto.SubscribeResponseDto;
import reactivechallenge.pragma.input.handler.UserHandler;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/subscribe",
                    produces = {
                            MediaType.APPLICATION_NDJSON_VALUE
                    },
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "subscribeToBootcamp",
                    operation = @Operation(
                            operationId = "subscribeToBootcamp",
                            summary = "Subscribirse a bootcamps",
                            description = "Crear subscripción",
                            tags = {"Gestión de subscripción"},
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Subscribed",
                                            content = @Content(schema = @Schema(implementation = SubscribeResponseDto.class))
                                    )
                            },
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = SubscribeRequestDto.class))
                            )
                    )
            )
    })
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return route(POST("/subscribe").and(accept(MediaType.APPLICATION_NDJSON)), userHandler::subscribeToBootcamp);
    }


}
