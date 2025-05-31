package de.yagub.deliverysystem.msorder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Promocode {
    private String promocode;
    private Boolean isUsed;
}
