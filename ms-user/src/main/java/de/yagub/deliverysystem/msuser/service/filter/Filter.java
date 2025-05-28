package de.yagub.deliverysystem.msuser.service.filter;

import de.yagub.deliverysystem.msuser.model.FilterTarget;
import de.yagub.deliverysystem.msuser.model.enums.FilterId;
import org.springframework.stereotype.Component;

@Component
public interface Filter{
    void execute(FilterTarget filterTarget);
    FilterId id();

}
