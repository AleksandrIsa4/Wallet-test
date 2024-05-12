package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class WalletDto {

    @Schema(description = "ID кошелька", example = "1")
    long valletId;

    @Schema(description = "Тип операции", example = "DEPOSIT")
    OperationType operationType;

    @Schema(description = "Количество переводимых средств", example = "100")
    @Positive
    long amount;
}
