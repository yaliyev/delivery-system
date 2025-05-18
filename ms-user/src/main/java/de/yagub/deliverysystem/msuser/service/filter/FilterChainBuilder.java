package de.yagub.deliverysystem.msuser.service.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilterChainBuilder<T> {
    private final List<Filter<T>> filters;

    public FilterChain<T> build() {
        List<Filter<T>> sortedFilters = filters.stream()
                .sorted(Comparator.comparingInt(this::getOrder))
                .collect(Collectors.toList());

        return new FilterChain<>(sortedFilters);
    }

    private int getOrder(Filter<T> filter) {
        return filter.getClass().getAnnotation(Order.class).value();
    }

    public void logFilters() {
        System.out.println("Filters will execute in this order:");
        filters.forEach(filter ->
                System.out.println(filter.getClass().getSimpleName())
        );
    }

}
