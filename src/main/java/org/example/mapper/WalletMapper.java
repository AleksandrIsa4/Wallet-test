package org.example.mapper;

import org.example.dto.WalletResponse;
import org.example.entity.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    WalletResponse toWalletResponseFromWallet(Wallet wallet);
}
