package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Wallet;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.DataNotFoundException;
import org.example.model.WalletDto;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    @Transactional
    public Wallet create() {
        return walletRepository.save(Wallet.builder()
                .total(0)
                .build());
    }

    @Transactional
    public Wallet post(WalletDto walletDto) {
        Wallet wallet = getWallet(walletDto.getValletId());
        long total = wallet.getTotal();
        long amount = walletDto.getAmount();
        switch (walletDto.getOperationType()) {
            case DEPOSIT -> total += amount;
            case WITHDRAW -> {
                total -= amount;
                if (total < 0) {
                    throw new BadRequestException("Баланс не может быть отрицательным");
                }
            }
        }
        wallet.setTotal(total);
        return walletRepository.save(wallet);
    }

    @Transactional(readOnly = true)
    public Wallet get(Long id) {
        return getWallet(id);
    }

    private Wallet getWallet(Long id) {
        return walletRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Нет записи с id=" + id));
    }
}
