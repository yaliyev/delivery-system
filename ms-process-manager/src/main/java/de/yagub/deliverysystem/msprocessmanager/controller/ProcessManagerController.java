package de.yagub.deliverysystem.msprocessmanager.controller;

import de.yagub.deliverysystem.msprocessmanager.dto.request.ProcessManagerRequest;
import de.yagub.deliverysystem.msprocessmanager.dto.response.ProcessManagerResponse;
import de.yagub.deliverysystem.msprocessmanager.service.ProcessManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pm")
@RequiredArgsConstructor
public class ProcessManagerController {

    private final ProcessManagerService processManagerService;

    @PostMapping("/start")
    public ProcessManagerResponse start(@RequestBody ProcessManagerRequest processManagerRequest){
        return  processManagerService.start(processManagerRequest);
    }

}
