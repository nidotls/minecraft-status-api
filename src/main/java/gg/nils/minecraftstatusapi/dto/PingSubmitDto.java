package gg.nils.minecraftstatusapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PingSubmitDto {

    @NotNull
    @NotBlank
    private String token;

    @NotNull
    @NotBlank
    private String serverId;

    @NotNull
    @Min(0)
    private Long count;
}
