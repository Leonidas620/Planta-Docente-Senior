package modelo;

public class Profesor {
    private final String nombre;
    private final CategoriaDocente categoria;
    private final GradoCientifico grado;
    private final int edad;

    public Profesor(String nombre, CategoriaDocente categoria, GradoCientifico grado, int edad) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del profesor no puede estar vacío.");
        }
        if (categoria == null) {
            throw new IllegalArgumentException("La categoría docente no puede ser nula.");
        }
        if (grado == null) {
            throw new IllegalArgumentException("El grado científico no puede ser nulo.");
        }
        if (edad < 18 || edad > 100) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 100 años: " + edad);
        }
        this.nombre = nombre.trim();
        this.categoria = categoria;
        this.grado = grado;
        this.edad = edad;
    }

    public String getNombre() { return nombre; }
    public CategoriaDocente getCategoria() { return categoria; }
    public GradoCientifico getGrado() { return grado; }
    public int getEdad() { return edad; }

    @Override
    public String toString() {
        return nombre + " (" + categoria + ", " + grado + ", " + edad + " años)";
    }
}
