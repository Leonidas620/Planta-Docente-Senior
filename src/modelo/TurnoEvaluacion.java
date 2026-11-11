package modelo;

import java.time.LocalDate;

public class TurnoEvaluacion {
    private final Profesor profesor;
    private final LocalDate fechaSolicitud;
    private final int limiteDias;

    public TurnoEvaluacion(Profesor profesor, LocalDate fechaSolicitud, int limiteDias) {
        if (profesor == null || fechaSolicitud == null) {
            throw new IllegalArgumentException("Profesor y fecha de solicitud no pueden ser nulos.");
        }
        if (limiteDias < 1) {
            throw new IllegalArgumentException("El límite de días debe ser al menos 1: " + limiteDias);
        }
        this.profesor = profesor;
        this.fechaSolicitud = fechaSolicitud;
        this.limiteDias = limiteDias;
    }

    public Profesor getProfesor() { return profesor; }
    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public int getLimiteDias() { return limiteDias; }

    public LocalDate getFechaLimite() {
        return fechaSolicitud.plusDays(limiteDias);
    }

    public boolean estaAtrasado(LocalDate hoy) {
        return getFechaLimite().isBefore(hoy);
    }

    @Override
    public String toString() {
        return profesor.getNombre() + " | solicitud: " + fechaSolicitud
                + " | límite: " + limiteDias + " días (vence " + getFechaLimite() + ")";
    }
}
