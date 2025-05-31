package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.mapper.OrderMapper;
import de.yagub.deliverysystem.msorder.model.Order;
import de.yagub.deliverysystem.msorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class StandartPurchasePricing implements PricingStrategy{


    @Override
    public BigDecimal calculatePrice(OrderRequest orderRequest) {
        BigDecimal total = orderRequest.items().stream()
                .map(i -> i.pricePerUnit().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return total;
    }

    @Override
    public boolean isSuitable(OrderRequest orderRequest) {
        System.out.println(getClass());
        return true;
    }

    @Override
    public int filterId() {
        return 3;
    }
}
