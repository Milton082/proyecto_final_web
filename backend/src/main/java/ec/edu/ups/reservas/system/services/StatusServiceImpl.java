package ec.edu.ups.reservas.system.services;

import ec.edu.ups.reservas.system.dtos.StatusResponseDto;
import ec.edu.ups.reservas.system.repositories.StatusRepository;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {
    private final StatusRepository repository;

    public StatusServiceImpl(StatusRepository repository) {
        this.repository = repository;
    }

    @Override
    public StatusResponseDto getStatus() {
        if (!repository.isInitialized()) {
            throw new DataRetrievalFailureException("Base técnica no inicializada");
        }
        return new StatusResponseDto("reservas-laboratorios", "UP", "UP");
    }
}
