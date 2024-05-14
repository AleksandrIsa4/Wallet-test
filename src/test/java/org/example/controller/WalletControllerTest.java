package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dto.WalletResponse;
import org.example.entity.Wallet;
import org.example.model.OperationType;
import org.example.dto.WalletDto;
import org.example.service.WalletService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class WalletControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WalletService walletService;

    Wallet wallet1, wallet2;

    WalletDto walletDto1, walletDto2;

    WalletDto walletDtoNotValidAmount, walletDtoNotValidValletId, walletDtoMoreAmount;

    @BeforeEach
    void init() {
        //wallet1 = new Wallet(1, 100);
        // wallet2 = new Wallet(2, 100);
        wallet1 = walletService.create();
        wallet2 = walletService.create();
        walletDto1 = new WalletDto(wallet1.getValletId(), OperationType.DEPOSIT, 200);
        walletDto2 = new WalletDto(wallet1.getValletId(), OperationType.WITHDRAW, 100);
        walletDtoMoreAmount = new WalletDto(wallet2.getValletId(), OperationType.WITHDRAW, 1000);
        walletDtoNotValidAmount = new WalletDto(wallet1.getValletId(), OperationType.WITHDRAW, -1000);
        walletDtoNotValidValletId = new WalletDto(44, OperationType.DEPOSIT, 200);
    }

    @Test
    @SneakyThrows
    void postWalletDone() {
        MvcResult resultStatusDone1 = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto1)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        MvcResult resultStatusDone2 = mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto2)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        WalletResponse resultWallet1 = objectMapper.readValue(
                resultStatusDone1.getResponse().getContentAsString(StandardCharsets.UTF_8),
                WalletResponse.class);
        WalletResponse resultWallet2 = objectMapper.readValue(
                resultStatusDone2.getResponse().getContentAsString(StandardCharsets.UTF_8),
                WalletResponse.class);
        Assertions.assertAll(
                () -> Assertions.assertEquals(resultWallet1.getTotal(), 200),
                () -> Assertions.assertEquals(resultWallet2.getTotal(), 100),
                () -> Assertions.assertEquals(resultWallet1.getValletId(), wallet1.getValletId())
        );
    }

    @Test
    @SneakyThrows
    void postWalletNotValidDate() {
        Assertions.assertAll(
                () -> mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(walletDtoNotValidAmount)))
                        .andExpect(status().isBadRequest()),
                () -> mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(walletDtoNotValidValletId)))
                        .andExpect(status().isNotFound()),
                () -> mockMvc.perform(MockMvcRequestBuilders
                                .post("/api/v1/wallet")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(walletDtoMoreAmount)))
                        .andExpect(status().isBadRequest())
        );
    }

    @Test
    @SneakyThrows
    void getWalletDone() {
        Wallet walletGet = walletService.create();
        WalletDto walletDto = new WalletDto(walletGet.getValletId(), OperationType.DEPOSIT, 777);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/wallet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletDto)))
                .andExpect(status().is2xxSuccessful());
        String urlGet = "/api/v1/wallets/" + walletGet.getValletId();
        MvcResult resultStatusDone = mockMvc.perform(MockMvcRequestBuilders
                        .get(urlGet))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        WalletResponse resultWallet = objectMapper.readValue(
                resultStatusDone.getResponse().getContentAsString(StandardCharsets.UTF_8),
                WalletResponse.class);
        Assertions.assertAll(
                () -> Assertions.assertEquals(resultWallet.getTotal(), 777),
                () -> Assertions.assertEquals(resultWallet.getValletId(), walletGet.getValletId())
        );
    }

    @Test
    @SneakyThrows
    void getWalletNotFoundId() {
        Assertions.assertAll(
                () -> mockMvc.perform(MockMvcRequestBuilders
                                .get("/api/v1/wallets/123123"))
                        .andExpect(status().isNotFound())
        );
    }
}