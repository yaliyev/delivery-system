package de.yagub.deliverysystem.msorder.service;

import de.yagub.deliverysystem.msorder.dto.request.OrderRequest;
import de.yagub.deliverysystem.msorder.mapper.OrderMapper;
import de.yagub.deliverysystem.msorder.model.Order;
import de.yagub.deliverysystem.msorder.model.Promocode;
import de.yagub.deliverysystem.msorder.repository.OrderRepository;
import de.yagub.deliverysystem.msorder.repository.PromocodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PromocodePurchasePricing implements PricingStrategy{

    private final  StandartPurchasePricing standartPurchasePricing;

    private final PromocodeRepository promocodeRepository;

    @Override
    public BigDecimal calculatePrice(OrderRequest orderRequest) {
        Promocode promocode = promocodeRepository.findByCode(orderRequest.promocode().getPromocode()).get();
        BigDecimal standartAmount = standartPurchasePricing.calculatePrice(orderRequest);
        BigDecimal totalAmount = standartAmount.subtract(promocode.getAmount());
        return totalAmount;
    }

    @Override
    public boolean isSuitable(OrderRequest orderRequest) {
        boolean promocodeWorks = false;
        if(orderRequest.promocode()!=null){
            if(!orderRequest.promocode().getPromocode().isBlank()){
                Optional<Promocode> promocodeOptional = promocodeRepository.findByCode(orderRequest.promocode().getPromocode());
                if(promocodeOptional.isPresent()){
                    Promocode promocode = promocodeOptional.get();
                    if(!promocode.getIsUsed()){
                        promocodeRepository.markAsUsed(promocode.getPromocode());
                        promocodeWorks = true;
                    }
                }
            }
        }
        return promocodeWorks;
    }

    @Override
    public int filterId() {
        return 2;
    }
}
