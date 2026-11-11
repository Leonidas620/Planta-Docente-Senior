package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PermisoLaboratorio {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Profesor profesor;
    private final LocalDateTime horaSolicitud;
    private final LocalDateTime horaSalida;

    public PermisoLaboratorio(Profesor profesor, LocalDateTime horaSolicitud, LocalDateTime horaSalida) {
        if (profesor == null || horaSolicitud == null || horaSalida == null) {
            throw new IllegalArgumentException("Profesor y horas del permiso no pueden ser nulos.");
        }
        if (horaSalida.isBefore(horaSolicitud)) {
            throw new IllegalArgumentException("La hora de salida no puede ser anterior a la de solicitud.");
        }
        this.profesor = profesor;
        this.horaSolicitud = horaSolicitud;
        this.horaSalida = horaSalida;
    }

    public Profesor getProfesor() { return profesor; }
    public LocalDateTime getHoraSolicitud() { return horaSolicitud; }
    public LocalDateTime getHoraSalida() { return horaSalida; }

    @Override
    public String toString() {
        return profesor.getNombre() + " | solicitó: " + horaSolicitud.format(FORMATO)
                + " | salida: " + horaSalida.format(FORMATO);
    }
}
