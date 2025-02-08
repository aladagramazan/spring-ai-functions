package com.rem.springaifunctions.model;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

@JsonClassDescription("Inflation request")
public record InflationRequest(@JsonPropertyDescription("inflation of the country") String country) {
}
