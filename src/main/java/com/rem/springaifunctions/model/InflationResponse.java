package com.rem.springaifunctions.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Builder;

@Builder
public record InflationResponse(@JsonPropertyDescription("country") String country,
                                @JsonPropertyDescription("type") String type,
                                @JsonPropertyDescription("period") String period,
                                @JsonPropertyDescription("monthly_rate_pct") double monthly_rate_pct,
                                @JsonPropertyDescription("yearly_rate_pct") double yearly_rate_pct) {
}