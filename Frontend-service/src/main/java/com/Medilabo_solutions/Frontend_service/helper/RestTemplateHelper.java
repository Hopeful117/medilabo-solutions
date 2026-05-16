package com.Medilabo_solutions.Frontend_service.helper;

import com.Medilabo_solutions.Frontend_service.dto.ApiErrorDTO;
import com.Medilabo_solutions.Frontend_service.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpStatusCodeException;
import tools.jackson.databind.ObjectMapper;

/**
 * Helper class to handle RestTemplate exceptions and convert them into ApiException.
 */
@Slf4j
public class RestTemplateHelper {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static ApiException handleException(HttpStatusCodeException ex) {

        try {
            String body = ex.getResponseBodyAsString();

            if (!body.isEmpty()) {
                ApiErrorDTO apiError = mapper.readValue(body, ApiErrorDTO.class);

                return new ApiException(
                        apiError.getError(),
                        apiError.getStatus()

                );
            }

        } catch (Exception ignored) {
            log.error("Failed to parse error response: {}", ex.getResponseBodyAsString(), ignored);
        }

        return new ApiException(
                "Erreur serveur",
                ex.getStatusCode().value()
        );
    }
}
