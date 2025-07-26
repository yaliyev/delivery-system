package de.yagub.deliverysystem.msreportconsumer.service;

import de.yagub.deliverysystem.msreportconsumer.model.Request;
import de.yagub.deliverysystem.msreportconsumer.model.Response;
import de.yagub.deliverysystem.msreportconsumer.repository.RequestRepository;
import de.yagub.deliverysystem.msreportconsumer.repository.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService{
    private final RequestRepository requestRepository;
    private final ResponseRepository responseRepository;

    @Transactional
    public Response processReport() {
        Request request = new Request();
        request.setRequestDate(LocalDateTime.now());
        Request savedRequest = requestRepository.save(request);


        Response response = new Response();
        response.setRequestId(savedRequest.getId());
        responseRepository.save(response);

        return response;
    }
}
