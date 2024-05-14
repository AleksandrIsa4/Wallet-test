package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.Wallet;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.DataNotFoundException;
import org.example.dto.WalletDto;
import org.example.repository.WalletRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    private final JdbcTemplate jdbcTemplate;


    @Transactional
    public Wallet create() {
        return walletRepository.save(Wallet.builder()
                .total(0)
                .build());
    }

    @Transactional
    public Wallet post(WalletDto walletDto) {
        long id = walletDto.getValletId();
        Wallet wallet = getWallet(id);
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
        int answerUpdate = jdbcTemplate.update("UPDATE wallets SET total=? where vallet_id=?", total, id);
        if (answerUpdate != 1) {
            throw new BadRequestException("Ошибка с базой данных, изменение касалось "+answerUpdate+" строк, а не одной");
        }
        wallet.setTotal(total);
        return wallet;
    }

    @Transactional
    public Wallet get(Long id) {
        return getWallet(id);
    }

    private Wallet getWallet(Long id) {
        SqlRowSet cityRows = jdbcTemplate.queryForRowSet("Select * FROM wallets where vallet_id=? FOR UPDATE", id);
        Wallet wallet = new Wallet();
        if (cityRows.next()) {
            wallet.setTotal(cityRows.getLong("total"));
            wallet.setValletId(cityRows.getLong("vallet_id"));
        } else {
            throw new DataNotFoundException("Нет записи с id=" + id);
        }
        return wallet;
    }
}
