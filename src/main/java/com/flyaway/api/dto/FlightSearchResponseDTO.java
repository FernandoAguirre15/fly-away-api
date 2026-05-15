package com.flyaway.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class FlightSearchResponseDTO {
    public List<FlightResponseDTO> items;
}