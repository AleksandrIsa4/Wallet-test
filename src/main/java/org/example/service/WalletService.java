package org.example.service;

import org.example.entity.Wallet;
import org.example.dto.WalletDto;

public interface WalletService {

    Wallet post(WalletDto walletDto);

    Wallet create();

    Wallet get(Long id);
}
