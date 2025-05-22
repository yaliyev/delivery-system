package de.yagub.deliverysystem.msuser.service.filter;

import de.yagub.deliverysystem.msuser.model.enums.FilterId;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilterChainBuilder {
    private  FilterId[] userServiceFilters;


    public List<FilterId> getUserServiceFilters(){

        userServiceFilters = FilterId.values();

        List<FilterId> sortedFilters = Arrays.stream(userServiceFilters)
                .sorted(Comparator.comparingInt(FilterId::getId))
                .toList();

        return  sortedFilters;
    }


}
