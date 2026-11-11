package estructura;

import modelo.Profesor;

public class NodoProfesor {
    private Profesor info;
    private NodoProfesor next;

    public NodoProfesor(Profesor info) {
        this.info = info;
        this.next = null;
    }

    public Profesor getInfo() { return info; }
    public void setInfo(Profesor info) { this.info = info; }
    public NodoProfesor getNext() { return next; }
    public void setNext(NodoProfesor next) { this.next = next; }
}
