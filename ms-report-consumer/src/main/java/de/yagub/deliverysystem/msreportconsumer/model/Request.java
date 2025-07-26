package de.yagub.deliverysystem.msreportconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {
    private Long id;
    private LocalDateTime requestDate;
}
