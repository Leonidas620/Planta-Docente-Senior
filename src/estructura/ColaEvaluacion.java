package estructura;

import modelo.Profesor;
import modelo.ResultadoEvaluacion;
import modelo.TurnoEvaluacion;
import java.time.LocalDate;

public class ColaEvaluacion {
    private Node<TurnoEvaluacion> first;
    private Node<TurnoEvaluacion> last;

    public ColaEvaluacion() {
        this.first = null;
        this.last = null;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public void add(TurnoEvaluacion turno) {
        if (turno == null) {
            throw new IllegalArgumentException("No se puede encolar un turno nulo.");
        }
        Node<TurnoEvaluacion> n = new Node<>(turno);
        if (isEmpty()) {
            first = n;
            last = n;
        } else {
            last.setNext(n);
            last = n;
        }
    }

    public TurnoEvaluacion poll() {
        if (isEmpty()) {
            return null;
        }
        TurnoEvaluacion item = first.getInfo();
        first = first.getNext();
        if (first == null) {
            last = null;
        }
        return item;
    }

    public TurnoEvaluacion peek() {
        if (isEmpty()) {
            return null;
        }
        return first.getInfo();
    }

    public ResultadoEvaluacion evaluarProfesor(int nuevoLimiteDias) {
        return evaluarProfesor(nuevoLimiteDias, LocalDate.now());
    }

    public ResultadoEvaluacion evaluarProfesor(int nuevoLimiteDias, LocalDate hoy) {
        if (isEmpty()) {
            System.out.println("    ERROR: no hay turnos de evaluación en la cola.");
            return ResultadoEvaluacion.COLA_VACIA;
        }
        if (nuevoLimiteDias < 1) {
            throw new IllegalArgumentException("El nuevo límite de días debe ser al menos 1: " + nuevoLimiteDias);
        }
        TurnoEvaluacion turno = poll();
        Profesor profesor = turno.getProfesor();
        ResultadoEvaluacion resultado;
        if (turno.estaAtrasado(hoy)) {
            System.out.println("    ¡ALERTA! La evaluación del profesor " + profesor.getNombre() + " está ATRASADA");
            resultado = ResultadoEvaluacion.ATRASADO;
        } else {
            System.out.println("    El profesor " + profesor.getNombre() + " ha sido EVALUADO exitosamente");
            resultado = ResultadoEvaluacion.EVALUADO;
        }
        add(new TurnoEvaluacion(profesor, hoy, nuevoLimiteDias));
        return resultado;
    }
}
