package com.raiffeisen.processor.controller;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.raiffeisen.processor.exception.DataNotFoundException;
import com.raiffeisen.processor.service.PaymentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentsController.class)
class PaymentsControllerTest {
    private static final String KEY_ID = "key_id";
    private static RSAKey RSA_KEY;

    @Autowired
    private MockMvc mockMvc;

    @RegisterExtension
    final static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.jwk-set-uri", wireMockServer::baseUrl);
    }

    @MockBean
    private PaymentService paymentService;

    @BeforeAll
    static void beforeAll() throws JOSEException {
        RSA_KEY = new RSAKeyGenerator(2048)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(new Algorithm("RS256"))
                .keyID(KEY_ID)
                .generate();
    }

    @BeforeEach
    public void beforeEach() {
        var rsaPublicJWK = RSA_KEY.toPublicJWK();
        var jwkResponse = format("{\"keys\": [%s]}", rsaPublicJWK.toJSONString());

        wireMockServer.stubFor(get("/").willReturn(aResponse()
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(jwkResponse)));
    }

    @Test
    void testGetPayments() throws Exception {
        when(paymentService.getPayments(any(), any())).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/payments").header("Authorization", "Bearer " + getSignedJwt()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCreatePayment() throws Exception {
        doNothing().when(paymentService).createPayment(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/payments").header("Authorization", "Bearer " + getSignedJwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100.0,\"currency\":\"USD\",\"fromAccount\":\"12345678901234567890\",\"toAccount\":\"09876543210987654321\"}"))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @CsvSource({
            "{\"amount\":100.0,\"currency\":\"USD\",\"fromAccount\":\"1234567890123456\",\"toAccount\":\"09876543210987654321\"}",
            "{\"amount\":0.0,\"currency\":\"USD\",\"fromAccount\":\"12345678901234567890\",\"toAccount\":\"09876543210987654321\"}",
            "{\"amount\":-100.0,\"currency\":\"USD\",\"fromAccount\":\"12345678901234567890\",\"toAccount\":\"09876543210987654321\"}",
            "{\"amount\":100.0,\"currency\":\"USDS\",\"fromAccount\":\"12345678901234567890\",\"toAccount\":\"09876543210987654321\"}"
    })
    void testCreatePaymentWithIncompletePayloads(String payload) throws Exception {
        doNothing().when(paymentService).createPayment(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/payments")
                        .header("Authorization", "Bearer " + getSignedJwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePayment() throws Exception {
        doNothing().when(paymentService).deletePayment(any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/payments/{id}", 1L)
                .header("Authorization", "Bearer " + getSignedJwt()))
                .andExpect(status().isOk());
    }

    @Test
    void testDeletePaymentNotFound() throws Exception {
        doThrow(new DataNotFoundException("Payment not found")).when(paymentService).deletePayment(any());

        mockMvc.perform(MockMvcRequestBuilders.delete("/payments/{id}", 1L)
                .header("Authorization", "Bearer " + getSignedJwt()))
                .andExpect(status().isNotFound());
    }

    private String getSignedJwt() throws Exception {
        var signer = new RSASSASigner(RSA_KEY);
        var claimsSet = new JWTClaimsSet.Builder()
                .expirationTime(new Date(new Date().getTime() + 60 * 1000))
                .build();
        var signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(RSA_KEY.getKeyID()).build(), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }
}