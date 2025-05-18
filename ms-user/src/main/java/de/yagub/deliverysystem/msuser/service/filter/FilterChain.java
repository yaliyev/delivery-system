package de.yagub.deliverysystem.msuser.service.filter;

import java.util.Iterator;
import java.util.List;

public class FilterChain<T> {
    private final Iterator<Filter<T>> iterator;

    public FilterChain(List<Filter<T>> filters) {
        this.iterator = filters.iterator();
    }

    public void proceed(T context) {
        if (iterator.hasNext()) {
            Filter<T> nextFilter = iterator.next();
            nextFilter.execute(context, this);
        }
    }
}
