package btree;

public class BTree <E extends Comparable<E>> {
    private NodeB<E> root;
    private final int orden;
    private boolean up;
    private NodeB<E> nDes;

    public BTree(int orden) {
        this.orden = orden;
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public boolean search(E cl) {
        return search(this.root, cl);
    }

    private boolean search(NodeB<E> actual, E cl) {
        int pos[] = new int[1];
        boolean fl;

        if(actual == null)
            return false;
        else {
            fl = actual.searchNode(cl, pos);
            if(fl) {
                return true;
            }
            else
                return search(actual.childs.get(pos[0]), cl);
        }

    }

    public E find(E cl) {
        return find(this.root, cl);
    }

    private E find(NodeB<E> actual, E cl) {
        int pos[] = new int[1];
        boolean fl;

        if(actual == null)
            return null;
        else {
            fl = actual.searchNode(cl, pos);
            if(fl) {
                return actual.keys.get(pos[0]);
            }
            else
                return find(actual.childs.get(pos[0]), cl);
        }

    }

    public void insert(E cl) {
        this.up = false;
        E mediana;
        NodeB<E> pnew;
        mediana = push(this.root, cl);
        if(this.up) {
            pnew = new NodeB<>(this.orden);
            pnew.count = 1;
            pnew.keys.set(0, mediana);
            pnew.childs.set(0, this.root);
            pnew.childs.set(1, this.nDes);
            this.root = pnew;
        }
    }

    private E push(NodeB<E> actual, E cl) {
        int pos[] = new int[1];
        E mediana;
        if(actual == null) {
            this.up = true;
            this.nDes = null;
            return cl;
        } else {
            boolean fl;
            fl = actual.searchNode(cl, pos);
            if(fl) {
                System.out.println("Item duplicado\n");
                this.up = false;
                return null;
            }
            mediana = push(actual.childs.get(pos[0]), cl);
            if(this.up) {
                if(actual.nodeFull(this.orden - 1))
                    mediana = dividedNode(actual, mediana, pos[0]);
                else {
                    this.up = false;
                    putNode(actual, mediana, this.nDes, pos[0]);
                }
            }
            return mediana;
        }
    }

    private void putNode(NodeB<E> actual, E cl, NodeB<E> rd, int k) {
        int i;

        for(i = actual.count - 1; i >= k; i--) {
            actual.keys.set(i + 1, actual.keys.get(i));
            actual.childs.set(i + 2, actual.childs.get(i + 1));
        }
        actual.keys.set(k, cl);
        actual.childs.set(k + 1, rd);
        actual.count++;
    }

    private E dividedNode(NodeB<E> actual, E cl, int k) {
        NodeB<E> rd = this.nDes;
        int i, posMdna;
        posMdna = (k <= this.orden/2) ? this.orden/2 : this.orden/2 + 1;

        this.nDes = new NodeB<> (this.orden);
        for(i = posMdna; i < this.orden - 1; i++) {
            nDes.keys.set(i - posMdna, actual.keys.get(i));
            nDes.childs.set(i - posMdna + 1, actual.childs.get(i + 1));
        }
        nDes.count = (this.orden - 1) - posMdna;
        actual.count = posMdna;

        if(k <= this.orden/2)
            putNode(actual, cl, rd, k);
        else
            putNode(this.nDes, cl, rd, k - posMdna);
        E median = actual.keys.get(actual.count - 1);
        nDes.childs.set(0, actual.childs.get(actual.count));
        actual.count--;
        return median;
    }
    @Override
    public String toString() {
        String s = "";
        if(isEmpty())
            s += "BTree is empty...";
        else
            s = writeTree(this.root);
        return s;
    }

    public String writeTree(NodeB<E> actual) {
        int i;
        String s = "";
        if(actual != null) {
            s += actual.toString() + "\n";
            for(i= 0; i <= actual.count; i++)
                s += writeTree(actual.childs.get(i)); 
        }
        return s;
    }

    public void remove(E cl) {
        //caso arbol vacio
        this.up = false;
        if(isEmpty()) {
            System.out.println("BTree is empty...");
            return;
        }
        delete(cl, this.root);
    }

    public void delete(E cl, NodeB<E> actual) {
        int pos[] = new int[1];
        boolean fl = false;
        if(actual == null)      //No se encontro el elemento a buscar
            System.out.println("El elemento no se encuentra en el arbol");
        else if(actual.searchNode(cl, pos)) {    //Se encontro el nodo
            if(actual.childs.get(0) == null) {  //El encontrado es hoja
                for(int i = pos[0] + 1; i < this.orden; i++)
                    actual.keys.set(i - 1, actual.keys.get(i));
                actual.keys.set(actual.count - 1, null);
                actual.count--;
                if(actual.nodeEmpty(this.orden)) {
                    this.up = true;
                    fl = true;
                }
            } else {    //El encontrado no es hoja
                E temp = getMin(actual.childs.get(pos[0] + 1)); //Se obtiene el minimo del hijo siguiente
                actual.keys.set(pos[0], temp);
                pos[0] = pos[0] + 1;
                delete(temp, actual.childs.get(pos[0]));
            }
        } else {
            delete(cl, actual.childs.get(pos[0]));
        }
        if(!fl && this.up) {    //Se hace reestructuracion al padre del actual
            //EVALUANDO CASO 1: Si el hermano izq puede prestar
            if(pos[0] - 1 >= 0 && !actual.childs.get(pos[0] - 1).nodeEmpty(this.orden + 2)) {    //Si hermano izq puede compartir
                System.out.println("se llego al caso 1");
                NodeB<E> izq = actual.childs.get(pos[0] - 1);
                NodeB<E> node = actual.childs.get(pos[0]);
                //actual esta en la posicion pos[0] - 1
                for(int i = node.count; i > 0; i --)    //Esto no deberia dar error, ya que node no cumple minimo
                    node.keys.set(i, node.keys.get(i - 1));     //Se ordenan las posiciones de las keys de nodo
                node.keys.set(0, actual.keys.get(pos[0] - 1));
                node.count++;
                for(int i = node.count; i > 0; i--)
                    node.childs.set(i, node.childs.get(i - 1));     //Se ordenan las posiciones de childs de nodo
                node.childs.set(0, izq.childs.get(izq.count));
                actual.keys.set(pos[0] - 1, izq.keys.get(izq.count - 1));   //Actualiza el valor de la raiz
                izq.childs.set(izq.count, null);    //Eliminando la clave e hijos pasados al otro nodo
                izq.keys.set(izq.count - 1, null);
                izq.count--;
                this.up = false;
            } else if(pos[0] + 1 < actual.count + 1 && !actual.childs.get(pos[0] + 1).nodeEmpty(this.orden + 2)) {    //Si hermano izq puede compartir
                //EVALUANDO CASO 2: Si el hermano der puede prestar
                System.out.println("se llego al caso 2");
                NodeB<E> der = actual.childs.get(pos[0] + 1);
                NodeB<E> node = actual.childs.get(pos[0]);
                //actual esta en la posicion pos[0]
                node.keys.set(node.count, actual.keys.get(pos[0])); //Se agrega a node la key de actual
                node.count++;
                node.childs.set(node.count, der.childs.get(0));   //Se agrega el hijo correspondiente de der
                actual.keys.set(pos[0], der.keys.get(0));   //Actualizando la key de actual
                for(int i = 1; i < der.count; i++)
                    der.childs.set(i - 1, node.childs.get(i));     //Se ordenan las posiciones de childs de der
                der.childs.set(der.count, null);    //Eliminando el hijo redundante en der
                for(int i = 1; i < der.count; i++)
                    der.keys.set(i - 1, der.keys.get(i));     //Se ordenan las posiciones de las keys de der
                der.keys.set(der.count - 1, null);      //Eliminando la clave redundante de der
                der.count--;
                this.up = false;
            } else if(pos[0] - 1 >= 0) {    //Si hermano izq se debe fusionar y se deduce que no cumple la regla del minimo al eliminar
                System.out.println("se llego al caso 3");
                NodeB<E> izq = actual.childs.get(pos[0] - 1);
                NodeB<E> node = actual.childs.get(pos[0]);
                //actual esta en la posicion pos[0] - 1
                NodeB<E> temp = new NodeB<>(this.orden);
                //AGREGANDO CLAVES A TEMP
                for(int i = 0; i < izq.count; i++) {    //Se agregan las claves de izq a temp y se aumenta su contador
                    temp.keys.set(i, izq.keys.get(i));
                    temp.count++;
                }
                temp.keys.set(temp.count, actual.keys.get(pos[0] - 1));     //Se agrega a temp la clave actual
                temp.count++;
                for(int i = 0; i < node.count; i++) {   //Se agregan las claves de node a temp y se aumenta su contador
                    temp.keys.set(i + izq.count + 1, node.keys.get(i));
                    temp.count++;
                }
                //AGREGANDO HIJOS A TEMP
                for(int i = 0; i < izq.count + 1; i++)    //Se agregan los hijos de izq a temp
                    temp.childs.set(i, izq.childs.get(i));
                for(int i = 0; i < node.count + 1; i++)  //Se agregan los hijos de node a temp
                    temp.childs.set(i + izq.count + 1, node.childs.get(i));
                //REORDENANDO ACTUAL
                for(int i = pos[0]; i < actual.count; i++)
                    actual.keys.set(i - 1, actual.keys.get(i));    //Reordenando claves
                actual.keys.set(actual.count - 1, null);    //Convertir el redundante en null
                actual.count--;
                actual.childs.set(pos[0] - 1, temp);    //Colocando temp como el nodo fusion
                for(int i = pos[0] + 1; i < actual.count + 2; i++)
                    actual.childs.set(i - 1, actual.childs.get(i));     //Reordenar hijos
                actual.childs.set(actual.count + 1, null);  //Convertir el redundante en null
                if(actual.nodeEmpty(this.orden)) {
                    this.up = true;
                }
                else {
                    this.up = false;
                }
                if(this.root.keys.get(0) == null) //SI EL ARBOL BAJO 1 NIVEL, LA RAIZ SERA TEMP
                    this.root = temp;
            } else {
                System.out.println("se llego al caso restante 4");
                NodeB<E> der = actual.childs.get(pos[0] + 1);
                NodeB<E> node = actual.childs.get(pos[0]);
                //actual esta en la posicion pos[0]
                NodeB<E> temp = new NodeB<>(this.orden);
                //AGREGANDO CLAVES A TEMP
                for(int i = 0; i < node.count; i++) {    //Se agregan las claves de node a temp y se aumenta su contador
                    temp.keys.set(i, node.keys.get(i));
                    temp.count++;
                }
                temp.keys.set(temp.count, actual.keys.get(pos[0]));     //Se agrega a temp la clave actual
                temp.count++;
                for(int i = 0; i < der.count; i++) {   //Se agregan las claves de der a temp y se aumenta su contador
                    temp.keys.set(i + node.count + 1, der.keys.get(i));
                    temp.count++;
                }
                //AGREGANDO HIJOS A TEMP
                for(int i = 0; i < node.count + 1; i++)    //Se agregan los hijos de izq a temp
                    temp.childs.set(i, node.childs.get(i));
                for(int i = 0; i < der.count + 1; i++)  //Se agregan los hijos de node a temp
                    temp.childs.set(i + node.count + 1, der.childs.get(i));
                //REORDENANDO ACTUAL
                for(int i = pos[0] + 1; i < actual.count; i++)
                    actual.keys.set(i - 1, actual.keys.get(i));    //Reordenando claves
                actual.keys.set(actual.count - 1, null);    //Convertir el redundante en null
                actual.count--;
                actual.childs.set(pos[0], temp);    //Colocando temp como el nodo fusion
                for(int i = pos[0] + 2; i < actual.count + 2; i++)
                    actual.childs.set(i - 1, actual.childs.get(i));     //Reordenar hijos
                actual.childs.set(actual.count + 1, null);  //Convertir el redundante en null
                if(actual.nodeEmpty(this.orden)) {
                    this.up = true;
                } else {
                    this.up = false;
                }
                if(this.root.keys.get(0) == null) //SI EL ARBOL BAJO 1 NIVEL, LA RAIZ SERA TEMP
                    this.root = temp;
            }            
        }
    }

    public E getMin(NodeB<E> actual) {
        while(actual.childs.get(0) != null)
            actual = actual.childs.get(0);
        return actual.keys.get(0); 
    }
}