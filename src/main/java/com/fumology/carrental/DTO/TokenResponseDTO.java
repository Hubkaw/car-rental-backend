package com.fumology.carrental.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponseDTO {

    private String token;
    private String tokenType;

}
