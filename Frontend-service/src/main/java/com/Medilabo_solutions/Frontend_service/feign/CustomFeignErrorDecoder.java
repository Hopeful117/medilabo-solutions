package com.Medilabo_solutions.Frontend_service.feign;

import com.Medilabo_solutions.Frontend_service.dto.ApiErrorDTO;
import com.Medilabo_solutions.Frontend_service.exception.ApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class CustomFeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        String body = "";

        try (InputStream is = response.body().asInputStream()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            body = "Erreur lecture body";
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            ApiErrorDTO apiError = mapper.readValue(body, ApiErrorDTO.class);

            return new ApiException(apiError.getError(), apiError.getStatus());

        } catch (Exception e) {
            // fallback si parsing échoue
            return new ApiException("Erreur Feign : " + body, response.status());
        }
    }
}