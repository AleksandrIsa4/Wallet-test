package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Wallet;
import org.example.model.WalletDto;
import org.example.service.WalletService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
public class WalletController {

    private final WalletService walletService;

    @Operation(summary = "Изменение баланса кошелька")
    @PostMapping(value = "/wallet")
    @Async
    public Wallet post(@RequestBody @Valid @NotNull WalletDto walletDto) {
        return walletService.post(walletDto);
    }

    @Operation(summary = "Получить баланс кошелька")
    @GetMapping(value = "wallets/{WALLET_UUID}")
    @Async
    public Wallet get(@PathVariable Long WALLET_UUID) {
        return walletService.get(WALLET_UUID);
    }

    @Operation(summary = "Создание кошелька")
    @PostMapping(value = "/wallet/create")
    public Wallet createWallet() {
        return walletService.create();
    }
}
