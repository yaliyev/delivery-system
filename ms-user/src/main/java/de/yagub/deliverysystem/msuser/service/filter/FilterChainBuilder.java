package de.yagub.deliverysystem.msuser.service.filter;

import de.yagub.deliverysystem.msuser.model.FilterTarget;
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
    private  List<FilterId> userServiceFilters;

    private final List<Filter> filters;


    public List<FilterId> proceedUserServiceFilters(FilterTarget filterTarget){

        userServiceFilters = Arrays.stream(FilterId.values()).toList();

        List<FilterId> sortedFilters = userServiceFilters.stream()
                .sorted(Comparator.comparingInt(FilterId::getId))
                .toList();


        sortedFilters.stream().forEach(id -> filters.stream()
                        .filter(filter -> id == filter.id())
                        .forEach(filter -> filter.execute(filterTarget)));

        return  sortedFilters;
    }






}
