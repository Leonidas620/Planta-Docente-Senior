package estructura;

import modelo.Profesor;

public class MultilistaUniversidad {
    private NodoDepartamento cabeza;

    public MultilistaUniversidad() {
        this.cabeza = null;
    }

    public void agregarDepartamento(String nombre) {
        String limpio = validarTexto(nombre, "El nombre del departamento");
        if (buscarDepartamento(cabeza, limpio) != null) {
            throw new IllegalArgumentException("Ya existe el departamento '" + limpio + "'.");
        }
        NodoDepartamento nuevo = new NodoDepartamento(limpio);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoDepartamento actual = cabeza;
            while (actual.getNext() != null) {
                actual = actual.getNext();
            }
            actual.setNext(nuevo);
        }
    }

    public void agregarProfesor(String nombreDepartamento, Profesor profesor) {
        if (profesor == null) {
            throw new IllegalArgumentException("El profesor no puede ser nulo.");
        }
        NodoDepartamento departamento = obtenerDepartamento(nombreDepartamento);
        NodoProfesor nuevo = new NodoProfesor(profesor);
        if (departamento.getPrimerProfesor() == null) {
            departamento.setPrimerProfesor(nuevo);
        } else {
            NodoProfesor actual = departamento.getPrimerProfesor();
            while (actual.getNext() != null) {
                actual = actual.getNext();
            }
            actual.setNext(nuevo);
        }
    }

    public int contarProfesoresMayores30(String nombreDepartamento) {
        NodoDepartamento departamento = obtenerDepartamento(nombreDepartamento);
        return contarMayores30(departamento.getPrimerProfesor());
    }

    private int contarMayores30(NodoProfesor actual) {
        if (actual == null) {
            return 0;
        }
        int esteNodo = actual.getInfo().getEdad() > 30 ? 1 : 0;
        return esteNodo + contarMayores30(actual.getNext());
    }

    private NodoDepartamento buscarDepartamento(NodoDepartamento actual, String nombre) {
        if (actual == null) {
            return null;
        }
        if (actual.getNombre().equalsIgnoreCase(nombre)) {
            return actual;
        }
        return buscarDepartamento(actual.getNext(), nombre);
    }

    private NodoDepartamento obtenerDepartamento(String nombre) {
        String limpio = validarTexto(nombre, "El nombre del departamento");
        NodoDepartamento encontrado = buscarDepartamento(cabeza, limpio);
        if (encontrado == null) {
            throw new IllegalArgumentException("El departamento '" + limpio + "' no existe en la universidad.");
        }
        return encontrado;
    }

    private static String validarTexto(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return texto.trim();
    }

    public void imprimir() {
        if (cabeza == null) {
            System.out.println("(universidad sin departamentos)");
            return;
        }
        NodoDepartamento dep = cabeza;
        while (dep != null) {
            System.out.print(dep.getNombre() + ": ");
            NodoProfesor p = dep.getPrimerProfesor();
            if (p == null) {
                System.out.print("(sin profesores)");
            }
            while (p != null) {
                System.out.print(p.getInfo().getNombre() + " [" + p.getInfo().getEdad() + "]");
                if (p.getNext() != null) {
                    System.out.print(" -> ");
                }
                p = p.getNext();
            }
            System.out.println();
            dep = dep.getNext();
        }
    }
}
