package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Builder
public class WalletResponse {
    @Schema(description = "ID кошелька")
    long valletId;

    @Schema(description = "Количество всего средств")
    long total;
}
