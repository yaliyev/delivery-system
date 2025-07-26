package de.yagub.deliverysystem.msreportconsumer.listener;

import de.yagub.deliverysystem.msreportconsumer.events.ProcessManagerResponse;
import de.yagub.deliverysystem.msreportconsumer.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportEventConsumer {

    private final ReportService reportService;

    @KafkaListener(topics = "process.completion.events", groupId = "report-consumer-group")
    public void consume(ProcessManagerResponse event) {
        System.out.println("==> Received event for username: " + event.username());

        reportService.processReport();

    }
}
