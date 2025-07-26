package de.yagub.deliverysystem.msreportconsumer.repository;

import de.yagub.deliverysystem.msreportconsumer.model.Request;

import java.util.Optional;

public interface RequestRepository {
    Request save(Request request);
    Optional<Request> findById(Long id);
}
