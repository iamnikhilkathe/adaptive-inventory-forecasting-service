package com.nikhil.inventory.adaptiveinventoryforecastingservice.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ParseRequest {
    @NotBlank
    private String data;
}