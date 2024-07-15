package btree;
import java.util.*;

public class NodeB<T extends Comparable <T>> {
    protected ArrayList<T> keys;
    protected ArrayList<NodeB<T>> childs;
    protected int count;
    
    public NodeB(int count) {
        this.keys = new ArrayList<T>(count - 1);
        this.childs = new ArrayList<NodeB<T>>(count);
        this.count = 0;
        for(int i=0; i<count; i++){
            this.keys.add(null);
            this.childs.add(null);
        }
    }

    /*Método que verifica la regla del máximo tomando en cuenta el orden del árbol al que
    pertenece */
    public boolean nodeFull(int orden) {
        return this.count == orden;
    }
    /*Método que verifica la regla del mínimo tomando en cuenta el orden del árbol al que
    Pertenece el nodo*/
    public boolean nodeEmpty(int orden) {
        return this.count < Math.ceil(orden / 2.0) - 1;
    }
    /*Método que busca de manera secuencial el elemento ‘cl’ en el conjunto de claves del nodo. Si no está,
    el método retorna False y en pos[0] la posición del desciende por donde se debe descender en el árbol.
    En caso contrario, retorna True y la posición en que la clave está en el nodo al que pertenece nodo*/
    public boolean searchNode(T cl, int posicion[]) {
        int i = 0;
        while(i < this.count && cl.compareTo(this.keys.get(i)) > 0)
            i++;
        if(cl.equals(keys.get(i))) {
            posicion[0] = i;
            return true;
        }
        posicion[0] = i;
        return false;
    }
    
    /*Método que retorna el conjunto de claves contenidas en el átbol*/
    public String toString() {
        String t = "";
        for(int i = 0; i < this.count; i++)
                t += "[" + this.keys.get(i) + "], ";
        return t;
    }
}