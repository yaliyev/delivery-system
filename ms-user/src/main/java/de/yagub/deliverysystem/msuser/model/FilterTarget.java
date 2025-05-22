package de.yagub.deliverysystem.msuser.model;

import de.yagub.deliverysystem.msuser.dto.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterTarget {
    private RegistrationRequest registrationRequest;

}
