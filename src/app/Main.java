package app;

import estructura.ColaEvaluacion;
import estructura.MultilistaUniversidad;
import estructura.PilaPermisos;
import modelo.CategoriaDocente;
import modelo.GradoCientifico;
import modelo.PermisoLaboratorio;
import modelo.Profesor;
import modelo.ResultadoEvaluacion;
import modelo.TurnoEvaluacion;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    private static final LocalDate HOY = LocalDate.of(2026, 11, 10);
    private static int total = 0;
    private static int superadas = 0;

    public static void main(String[] args) {
        System.out.println("=== EQUIPO 4: ESTADÍSTICA Y CONTROL DE PLANTA DOCENTE SÉNIOR ===");
        pruebasMultilista();
        pruebasRecursividad();
        pruebasPila();
        pruebasCola();
        System.out.println("\nRESULTADO: " + superadas + "/" + total + " pruebas superadas");
        if (superadas != total) {
            System.exit(1);
        }
    }

    private static void verificar(String id, String descripcion, boolean condicion) {
        total++;
        if (condicion) {
            superadas++;
        }
        System.out.println("[" + (condicion ? "OK" : "FALLO") + "] " + id + " - " + descripcion);
    }

    private static boolean lanza(Runnable accion) {
        try {
            accion.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static void titulo(String texto) {
        System.out.println("\n--- " + texto + " ---");
    }

    private static Profesor prof(String nombre, int edad) {
        return new Profesor(nombre, CategoriaDocente.ASISTENTE, GradoCientifico.MASTER, edad);
    }

    private static PermisoLaboratorio permiso(String nombre, int hora) {
        LocalDateTime t = HOY.atTime(hora, 0);
        return new PermisoLaboratorio(prof(nombre, 40), t, t.plusHours(1));
    }

    private static MultilistaUniversidad universidadBase() {
        MultilistaUniversidad u = new MultilistaUniversidad();
        u.agregarDepartamento("Informática");
        u.agregarDepartamento("Matemática");
        u.agregarDepartamento("Física");
        u.agregarProfesor("Informática", new Profesor("Ana Pérez", CategoriaDocente.TITULAR, GradoCientifico.DOCTOR, 52));
        u.agregarProfesor("Informática", new Profesor("Luis Gómez", CategoriaDocente.ASISTENTE, GradoCientifico.MASTER, 29));
        u.agregarProfesor("Informática", new Profesor("Sofía Rivas", CategoriaDocente.AUXILIAR, GradoCientifico.MASTER, 35));
        u.agregarProfesor("Matemática", new Profesor("Carlos Ruiz", CategoriaDocente.INSTRUCTOR, GradoCientifico.LICENCIADO, 30));
        return u;
    }

    private static void pruebasMultilista() {
        titulo("A) MULTILISTA (Universidad -> Departamentos -> Profesores)");

        MultilistaUniversidad u = new MultilistaUniversidad();

        // M-01 (positiva): flujo normal, un departamento con un profesor de 40 años.
        u.agregarDepartamento("Informática");
        u.agregarProfesor("Informática", prof("Ana", 40));
        verificar("M-01", "agregar departamento y profesor válidos", u.contarProfesoresMayores30("Informática") == 1);

        // M-02 (negativa): el mismo departamento con otras mayúsculas debe rechazarse.
        verificar("M-02", "departamento duplicado lanza excepción", lanza(() -> u.agregarDepartamento("informática")));

        // M-03 (negativa): nombre vacío o nulo.
        verificar("M-03", "nombre de departamento vacío o nulo lanza excepción",
                lanza(() -> u.agregarDepartamento("   ")) && lanza(() -> u.agregarDepartamento(null)));

        // M-04 (negativa): el departamento destino no existe.
        verificar("M-04", "profesor en departamento inexistente lanza excepción",
                lanza(() -> u.agregarProfesor("Nada", prof("X", 40))));

        // M-05 (negativa): profesor nulo o con edad fuera del rango 18-100.
        verificar("M-05", "profesor nulo o con edad inválida lanza excepción",
                lanza(() -> u.agregarProfesor("Informática", null))
                        && lanza(() -> prof("Joven", 10))
                        && lanza(() -> prof("Viejo", 150)));

        System.out.println("  Estructura de la universidad de prueba:");
        universidadBase().imprimir();
    }

    private static void pruebasRecursividad() {
        titulo("A.1) MÉTODO RECURSIVO contarProfesoresMayores30");

        MultilistaUniversidad u = universidadBase();

        // R-01 (positiva): Informática tiene edades 52, 29 y 35 -> 2 mayores de 30.
        verificar("R-01", "Informática (52, 29, 35) devuelve 2", u.contarProfesoresMayores30("Informática") == 2);

        // R-02 (positiva, límite): 30 años NO es mayor que 30.
        verificar("R-02", "profesor con exactamente 30 años no se cuenta", u.contarProfesoresMayores30("Matemática") == 0);

        // R-03 (negativa): departamento sin profesores.
        verificar("R-03", "departamento vacío devuelve 0", u.contarProfesoresMayores30("Física") == 0);

        // R-04 (negativa): departamento que no existe.
        verificar("R-04", "departamento inexistente lanza excepción", lanza(() -> u.contarProfesoresMayores30("Química")));

        // R-05 (positiva): la búsqueda ignora mayúsculas y espacios sobrantes.
        verificar("R-05", "búsqueda ignora mayúsculas y espacios", u.contarProfesoresMayores30("  informática ") == 2);

        // R-06 (positiva): sublista larga para comprobar que la recursión no desborda la pila.
        u.agregarDepartamento("Historia");
        for (int i = 0; i < 3000; i++) {
            u.agregarProfesor("Historia", prof("P" + i, 31));
        }
        verificar("R-06", "sublista de 3000 profesores devuelve 3000", u.contarProfesoresMayores30("Historia") == 3000);
    }

    private static void pruebasPila() {
        titulo("B) PILA DE PERMISOS DE LABORATORIO (LIFO)");

        PilaPermisos pila = new PilaPermisos();

        // P-01 (positiva): una pila recién creada está vacía.
        verificar("P-01", "pila nueva está vacía", pila.isEmpty());

        pila.push(permiso("A", 8));
        pila.push(permiso("B", 9));
        pila.push(permiso("C", 10));

        // P-02 (positiva): top consulta el último permiso sin quitarlo.
        verificar("P-02", "top devuelve C sin eliminarlo",
                pila.top().getProfesor().getNombre().equals("C") && pila.top().getProfesor().getNombre().equals("C"));

        // P-03 (positiva): se piden 2 de 3 permisos.
        System.out.println("  listarUltimosN(2):");
        verificar("P-03", "listarUltimosN(2) muestra 2 permisos", pila.listarUltimosN(2) == 2);

        // P-04 (positiva): se piden más permisos de los que hay.
        System.out.println("  listarUltimosN(10):");
        verificar("P-04", "listarUltimosN(10) muestra los 3 existentes", pila.listarUltimosN(10) == 3);

        // P-05 (positiva): listar no destruyó la pila, los pop salen en orden LIFO.
        verificar("P-05", "tras listar, pop devuelve C, B, A",
                pila.pop().getProfesor().getNombre().equals("C")
                        && pila.pop().getProfesor().getNombre().equals("B")
                        && pila.pop().getProfesor().getNombre().equals("A")
                        && pila.isEmpty());

        // P-06 (negativa): pop y top sobre la pila ya vacía devuelven null.
        verificar("P-06", "pop y top en pila vacía devuelven null", pila.pop() == null && pila.top() == null);

        // P-07 (negativa): listar sobre pila vacía imprime mensaje y devuelve 0.
        System.out.println("  listarUltimosN(3) en pila vacía:");
        verificar("P-07", "listarUltimosN en pila vacía devuelve 0", pila.listarUltimosN(3) == 0);

        // P-08 (negativa): n debe ser positivo.
        verificar("P-08", "listarUltimosN con n <= 0 lanza excepción",
                lanza(() -> pila.listarUltimosN(0)) && lanza(() -> pila.listarUltimosN(-1)));

        // P-09 (negativa): no se puede apilar null.
        verificar("P-09", "push de permiso nulo lanza excepción", lanza(() -> pila.push(null)));

        // P-10 (negativa): la hora de salida no puede ser anterior a la de solicitud.
        LocalDateTime t = HOY.atTime(8, 0);
        verificar("P-10", "permiso con salida anterior a la solicitud lanza excepción",
                lanza(() -> new PermisoLaboratorio(prof("X", 40), t, t.minusHours(1))));
    }

    private static void pruebasCola() {
        titulo("C) COLA DE TURNOS DE EVALUACIÓN (FIFO)");

        ColaEvaluacion cola = new ColaEvaluacion();

        // C-01 (positiva): una cola recién creada está vacía.
        verificar("C-01", "cola nueva está vacía", cola.isEmpty());

        // C-02 (positiva): el primero en entrar es el primero en salir.
        cola.add(new TurnoEvaluacion(prof("A", 40), HOY, 5));
        cola.add(new TurnoEvaluacion(prof("B", 40), HOY, 5));
        verificar("C-02", "poll respeta el orden FIFO (A, B)",
                cola.poll().getProfesor().getNombre().equals("A")
                        && cola.poll().getProfesor().getNombre().equals("B")
                        && cola.isEmpty());

        // C-03 (positiva): peek consulta el frente sin quitarlo.
        cola.add(new TurnoEvaluacion(prof("A", 40), HOY, 5));
        verificar("C-03", "peek devuelve A sin eliminarlo",
                cola.peek().getProfesor().getNombre().equals("A") && !cola.isEmpty());

        // C-04 (negativa): poll y peek sobre cola vacía devuelven null.
        cola.poll();
        verificar("C-04", "poll y peek en cola vacía devuelven null", cola.poll() == null && cola.peek() == null);

        // C-05 (positiva): la cola vuelve a funcionar tras vaciarse (last se reinicia).
        cola.add(new TurnoEvaluacion(prof("B", 40), HOY, 5));
        cola.add(new TurnoEvaluacion(prof("C", 40), HOY, 5));
        verificar("C-05", "la cola funciona tras vaciarse",
                cola.poll().getProfesor().getNombre().equals("B")
                        && cola.poll().getProfesor().getNombre().equals("C"));

        // C-06 (negativa): no se puede encolar null.
        verificar("C-06", "add de turno nulo lanza excepción", lanza(() -> cola.add(null)));

        // C-07 (negativa): turno solicitado hace 20 días con límite de 10 -> atrasado.
        ColaEvaluacion c7 = new ColaEvaluacion();
        c7.add(new TurnoEvaluacion(prof("Ana", 40), HOY.minusDays(20), 10));
        verificar("C-07", "turno vencido devuelve ATRASADO", c7.evaluarProfesor(15, HOY) == ResultadoEvaluacion.ATRASADO);

        // C-08 (positiva): turno solicitado hoy con límite de 5 -> vigente.
        ColaEvaluacion c8 = new ColaEvaluacion();
        c8.add(new TurnoEvaluacion(prof("Luis", 40), HOY, 5));
        verificar("C-08", "turno vigente devuelve EVALUADO", c8.evaluarProfesor(15, HOY) == ResultadoEvaluacion.EVALUADO);

        // C-09 (positiva, límite): vence exactamente hoy, no está atrasado.
        ColaEvaluacion c9 = new ColaEvaluacion();
        c9.add(new TurnoEvaluacion(prof("Ana", 40), HOY.minusDays(5), 5));
        verificar("C-09", "turno que vence hoy devuelve EVALUADO", c9.evaluarProfesor(15, HOY) == ResultadoEvaluacion.EVALUADO);

        // C-10 (positiva): el profesor evaluado se reencola al final con fecha de hoy y el nuevo límite.
        ColaEvaluacion c10 = new ColaEvaluacion();
        c10.add(new TurnoEvaluacion(prof("A", 40), HOY, 5));
        c10.add(new TurnoEvaluacion(prof("B", 40), HOY, 5));
        c10.evaluarProfesor(15, HOY);
        TurnoEvaluacion b = c10.poll();
        TurnoEvaluacion reencolado = c10.poll();
        verificar("C-10", "tras evaluar, A se reencola al final con fecha de hoy y límite 15",
                b.getProfesor().getNombre().equals("B")
                        && reencolado.getProfesor().getNombre().equals("A")
                        && reencolado.getFechaSolicitud().equals(HOY)
                        && reencolado.getLimiteDias() == 15);

        // C-11 (negativa): evaluar con la cola vacía muestra error.
        verificar("C-11", "evaluarProfesor en cola vacía devuelve COLA_VACIA",
                new ColaEvaluacion().evaluarProfesor(15, HOY) == ResultadoEvaluacion.COLA_VACIA);

        // C-12 (negativa): límite inválido lanza excepción y no altera la cola.
        ColaEvaluacion c12 = new ColaEvaluacion();
        c12.add(new TurnoEvaluacion(prof("A", 40), HOY, 5));
        verificar("C-12", "nuevo límite < 1 lanza excepción y la cola queda intacta",
                lanza(() -> c12.evaluarProfesor(0, HOY)) && c12.peek().getProfesor().getNombre().equals("A"));

        // C-13 (escenario del examen): un turno vencido y uno de hoy, cuatro evaluaciones seguidas.
        // Esperado: ATRASADO (Ana), EVALUADO (Luis), EVALUADO (Ana reencolada), EVALUADO (Luis reencolado).
        System.out.println("  Escenario del examen (fecha fija " + HOY + "):");
        ColaEvaluacion c13 = new ColaEvaluacion();
        c13.add(new TurnoEvaluacion(prof("Ana", 52), HOY.minusDays(20), 10));
        c13.add(new TurnoEvaluacion(prof("Luis", 29), HOY, 5));
        ResultadoEvaluacion r1 = c13.evaluarProfesor(15, HOY);
        ResultadoEvaluacion r2 = c13.evaluarProfesor(15, HOY);
        ResultadoEvaluacion r3 = c13.evaluarProfesor(15, HOY);
        ResultadoEvaluacion r4 = c13.evaluarProfesor(15, HOY);
        verificar("C-13", "secuencia ATRASADO, EVALUADO, EVALUADO, EVALUADO",
                r1 == ResultadoEvaluacion.ATRASADO && r2 == ResultadoEvaluacion.EVALUADO
                        && r3 == ResultadoEvaluacion.EVALUADO && r4 == ResultadoEvaluacion.EVALUADO);

        // C-14 (positiva): misma lógica usando la fecha real del sistema (LocalDate.now()).
        System.out.println("  Con la fecha real del sistema:");
        LocalDate ahora = LocalDate.now();
        ColaEvaluacion c14 = new ColaEvaluacion();
        c14.add(new TurnoEvaluacion(prof("Ana", 52), ahora.minusDays(20), 10));
        c14.add(new TurnoEvaluacion(prof("Luis", 29), ahora, 5));
        verificar("C-14", "evaluarProfesor(15) con la fecha del sistema: ATRASADO y luego EVALUADO",
                c14.evaluarProfesor(15) == ResultadoEvaluacion.ATRASADO
                        && c14.evaluarProfesor(15) == ResultadoEvaluacion.EVALUADO);
    }
}
