package ec.edu.ups.reservas.core.exceptions.response;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String error, String message, String path) {}
