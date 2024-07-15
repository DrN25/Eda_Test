package avl;

public class EntryNode<K extends Comparable<K>, V> implements Comparable<EntryNode<K,V>> {
    private K clave;
    private V valor;

    public EntryNode(K clave, V valor) {
        this.clave = clave;
        this.valor = valor;
    }
    @Override
    public int compareTo(EntryNode<K,V> otro) {
        return this.clave.compareTo(otro.getClave());
    }

    public void setClave(K clave) { this.clave = clave; }
    public void setValor(V valor) { this.valor = valor; }
    public K getClave() { return this.clave; }
    public V getValor() { return this.valor; }
}