package avl;

public class EntryNode implements Comparable<EntryNode> {
    private String clave;
    private String valor;

    public EntryNode(String clave, String valor) {
        this.clave = clave;
        this.valor = valor;
    }

    @Override
    public int compareTo(EntryNode otro) {
        // Convertir las claves a Double si es posible
        Double thisKey = tryParseDouble(this.clave);
        Double otherKey = tryParseDouble(otro.getClave());

        // Si ambas claves se pudieron convertir a Double, comparar como Double
        if (thisKey != null && otherKey != null) {
            return Double.compare(thisKey, otherKey);
        } else {
            // Si alguna de las claves no se pudo convertir a Double, comparar como String
            return this.clave.compareTo(otro.getClave());
        }
    }

    // Método para intentar convertir un String a Double
    private Double tryParseDouble(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null; // Si no se puede convertir, devolver null
        }
    }

    public void setClave(String clave) { this.clave = clave; }
    public void setValor(String valor) { this.valor = valor; }
    public String getClave() { return this.clave; }
    public String getValor() { return this.valor; }
}