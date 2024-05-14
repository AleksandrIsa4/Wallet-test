package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.WalletResponse;
import org.example.entity.Wallet;
import org.example.dto.WalletDto;
import org.example.mapper.WalletMapper;
import org.example.service.WalletService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    private final WalletMapper walletMapper;

    @Operation(summary = "Изменение баланса кошелька")
    @PostMapping(value = "/wallet")
    public WalletResponse post(@RequestBody @Valid @NotNull WalletDto walletDto) {
        Wallet wallet=walletService.post(walletDto);
        return walletMapper.toWalletResponseFromWallet(wallet);
    }

    @Operation(summary = "Получить баланс кошелька")
    @GetMapping(value = "wallets/{WALLET_UUID}")
    public WalletResponse get(@PathVariable Long WALLET_UUID) {
        Wallet wallet=walletService.get(WALLET_UUID);
        return walletMapper.toWalletResponseFromWallet(wallet);
    }

    @Operation(summary = "Создание кошелька")
    @PostMapping(value = "/wallet/create")
    public WalletResponse createWallet() {
        Wallet wallet=walletService.create();
        return walletMapper.toWalletResponseFromWallet(wallet);
    }
}
