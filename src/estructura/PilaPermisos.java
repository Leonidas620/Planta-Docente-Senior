package estructura;

import modelo.PermisoLaboratorio;

public class PilaPermisos {
    private Node<PermisoLaboratorio> top;

    public PilaPermisos() {
        this.top = null;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void push(PermisoLaboratorio permiso) {
        if (permiso == null) {
            throw new IllegalArgumentException("No se puede apilar un permiso nulo.");
        }
        top = new Node<>(permiso, top);
    }

    public PermisoLaboratorio pop() {
        if (isEmpty()) {
            return null;
        }
        PermisoLaboratorio aux = top.getInfo();
        top = top.getNext();
        return aux;
    }

    public PermisoLaboratorio top() {
        if (isEmpty()) {
            return null;
        }
        return top.getInfo();
    }

    public int listarUltimosN(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n debe ser mayor que 0: " + n);
        }
        if (isEmpty()) {
            System.out.println("    La pila de permisos está vacía: no hay permisos para listar.");
            return 0;
        }
        Node<PermisoLaboratorio> aux = top;
        int mostrados = 0;
        while (aux != null && mostrados < n) {
            mostrados++;
            System.out.println("    " + mostrados + ". " + aux.getInfo());
            aux = aux.getNext();
        }
        if (mostrados < n) {
            System.out.println("    (se pidieron " + n + ", pero solo hay " + mostrados + ")");
        }
        return mostrados;
    }
}
