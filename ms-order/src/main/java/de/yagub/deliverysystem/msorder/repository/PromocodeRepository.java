package de.yagub.deliverysystem.msorder.repository;

import de.yagub.deliverysystem.msorder.model.Promocode;

import java.math.BigDecimal;
import java.util.Optional;

public interface PromocodeRepository {

    Optional<Promocode> findByCode(String promocode);


    boolean markAsUsed(String promocode);
}
