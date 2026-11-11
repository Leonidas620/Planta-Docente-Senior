package estructura;

public class NodoDepartamento {
    private String nombre;
    private NodoProfesor primerProfesor;
    private NodoDepartamento next;

    public NodoDepartamento(String nombre) {
        this.nombre = nombre;
        this.primerProfesor = null;
        this.next = null;
    }

    public String getNombre() { return nombre; }
    public NodoProfesor getPrimerProfesor() { return primerProfesor; }
    public void setPrimerProfesor(NodoProfesor primerProfesor) { this.primerProfesor = primerProfesor; }
    public NodoDepartamento getNext() { return next; }
    public void setNext(NodoDepartamento next) { this.next = next; }
}
