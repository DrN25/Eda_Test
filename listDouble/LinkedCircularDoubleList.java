package listDouble;
import interfaces.*;

public class LinkedCircularDoubleList<T extends Comparable<T>> implements TDAList<T> {
	private int count;		
    private NodeDouble<T> first; 

    public LinkedCircularDoubleList() {
        this.first = null;
        this.count = 0;
    }

    public NodeDouble<T> getFirst() {
        return this.first;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int length() {
        return count;
    }

    public boolean search(T item) {
        NodeDouble<T> aux = this.first;
        while(aux != null) {
            if(aux.getData().equals(item)) {
                return true;
            }
            aux = aux.getNext();
            if(aux == this.first) {
                break;
            }
        }
        return false;
    }

    public NodeDouble<T> find(T item) {
        NodeDouble<T> aux = this.first;
        while(aux != null) {
            if(aux.getData().equals(item)) {
                return aux;
            }
            aux = aux.getNext();
            if(aux == this.first) {
                break;
            }
        }
        return null;
    }

    public void insert(T item) {
        NodeDouble<T> node = new NodeDouble<>(item);
        if(isEmpty()) {
            node.setNext(node); // Primer nodo apunta a sí mismo
            node.setBack(node); // Primer nodo apunta a sí mismo
            this.first = node;
        } else {
            // Insertar al final de la lista
            NodeDouble<T> last = this.first.getBack();
            last.setNext(node);
            node.setBack(last);
            node.setNext(this.first);
            this.first.setBack(node);
        }
        this.count++;
    }

    public void remove(T item) {
        if(!isEmpty()) {
            if(this.first.getData().equals(item)) {
                if(this.count == 1) {
                    this.first = null; // Eliminar el único nodo
                } else {
                    NodeDouble<T> last = this.first.getBack();
                    this.first = this.first.getNext();
                    last.setNext(this.first);
                    this.first.setBack(last);
                }
                this.count--;
            } else {
                NodeDouble<T> aux = this.first;
                while(aux.getNext() != null && !aux.getNext().getData().equals(item)) {
                    aux = aux.getNext();
                    if(aux == this.first) {
                        break;
                    }
                }
                if(aux.getNext() != null && aux.getNext().getData().equals(item)) {
                    aux.setNext(aux.getNext().getNext());
                    if(aux.getNext() != null) {
                        aux.getNext().setBack(aux);
                    }
                    this.count--;
                }
            }
        }
    }

    public void cambiarOrden(int posicionActual, int nuevaPosicion) throws IndexOutOfBoundsException {
        if(posicionActual < 0 || posicionActual >= this.count || nuevaPosicion < 0 || nuevaPosicion >= this.count) {
            throw new IndexOutOfBoundsException("Invalid positions.");
        }
    
        if(posicionActual == nuevaPosicion) return;
    
        // Find the node at posicionActual
        NodeDouble<T> nodeActual = this.first;
        NodeDouble<T> nodePrevActual = null;
    
        for(int i = 0; i < posicionActual; i++) {
            nodePrevActual = nodeActual;
            nodeActual = nodeActual.getNext();
        }
    
        // Remove nodeActual from its current position
        if(nodePrevActual != null) {
            nodePrevActual.setNext(nodeActual.getNext());
            nodeActual.getNext().setBack(nodePrevActual);
        } else {
            this.first = nodeActual.getNext();
            this.first.setBack(nodeActual.getBack());
            nodeActual.getBack().setNext(this.first);
        }
    
        // Find the node at nuevaPosicion
        NodeDouble<T> nodeNuevaPos = this.first;
        NodeDouble<T> nodePrevNuevaPos = null;
    
        for(int i = 0; i < nuevaPosicion; i++) {
            nodePrevNuevaPos = nodeNuevaPos;
            nodeNuevaPos = nodeNuevaPos.getNext();
        }
    
        // Insert nodeActual at nuevaPosicion
        if(nodePrevNuevaPos != null) {
            nodePrevNuevaPos.setNext(nodeActual);
            nodeActual.setBack(nodePrevNuevaPos);
            nodeActual.setNext(nodeNuevaPos);
            nodeNuevaPos.setBack(nodeActual);
        } else {
            nodeActual.setNext(this.first);
            nodeActual.setBack(this.first.getBack());
            this.first.getBack().setNext(nodeActual);
            this.first.setBack(nodeActual);
            this.first = nodeActual;
        }
    }
    

    public String toString() {
        if(isEmpty()) {
            return "CircularLinkedDoubleList is empty";
        }
        String t = "";
        NodeDouble<T> current = this.first;
        boolean condition = true;
        while(condition) {
            t += current.toString() + ", ";
            current = current.getNext();
            if (current == this.first)
                condition = false;
        }
        return t;
    }
}