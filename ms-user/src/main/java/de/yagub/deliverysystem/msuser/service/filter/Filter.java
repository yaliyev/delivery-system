package de.yagub.deliverysystem.msuser.service.filter;

import de.yagub.deliverysystem.msuser.model.FilterTarget;
import de.yagub.deliverysystem.msuser.model.enums.FilterId;

public interface Filter{
    void execute(FilterTarget filterTarget);
    FilterId id();

}
