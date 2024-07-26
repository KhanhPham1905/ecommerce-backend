package com.ghtk.ecommercewebsite.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayRepeatDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("day_of_week")
    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    @JsonProperty("voucher_id")
    @NotNull(message = "Voucher ID is required")
    private Long voucherId;

    public enum DayOfWeek {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }
}
