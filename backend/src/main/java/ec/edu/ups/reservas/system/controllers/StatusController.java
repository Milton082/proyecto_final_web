package ec.edu.ups.reservas.system.controllers;

import ec.edu.ups.reservas.system.dtos.StatusResponseDto;
import ec.edu.ups.reservas.system.services.StatusService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {
    private final StatusService service;

    public StatusController(StatusService service) {
        this.service = service;
    }

    @GetMapping
    public StatusResponseDto getStatus() {
        return service.getStatus();
    }
}
