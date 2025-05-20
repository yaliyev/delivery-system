package de.yagub.deliverysystem.msuser.service.filter;

public interface Filter<T> {
    void execute(T context, FilterChain<T> chain);

}
