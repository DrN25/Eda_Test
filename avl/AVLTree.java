package avl;

public class AVLTree<E extends Comparable<E>> {
    private AVLNode<E> root;
    private boolean creciente;

    public AVLTree(boolean creciente) {
        super();
        this.creciente = creciente;
    }

    public AVLTree() {
        this(true);
    }

    public AVLNode<E> getRoot() { return this.root; }

    public void insert(E x) {
        root = insertRec(root, x);
    }

    private AVLNode<E> insertRec(AVLNode<E> node, E x) {
        if (node == null) {
            return new AVLNode<>(x);
        }
        int compareResult;
        if(creciente) { compareResult = x.compareTo(node.getData()); }
        else { compareResult = node.getData().compareTo(x); }

        if (compareResult <= 0) {
            node.left = insertRec(node.left, x);
        } else {
            node.right = insertRec(node.right, x);
        }

        node.height = 1 + Math.max(height(node.left), height(node.right));

        int balance = getBalance(node);
        if(creciente) {
            if (balance > 1 && x.compareTo(node.left.getData()) < 0) {
                return rightRotate(node);
            }
            if (balance < -1 && x.compareTo(node.right.getData()) > 0) {
                return leftRotate(node);
            }
            if (balance > 1 && x.compareTo(node.left.getData()) > 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            if (balance < -1 && x.compareTo(node.right.getData()) < 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        } else { 
            if (balance > 1 && node.left.getData().compareTo(x) <= 0) {
                return rightRotate(node);
            }
            if (balance < -1 && node.right.getData().compareTo(x) > 0) {
                return leftRotate(node);
            }
            if (balance > 1 && node.left.getData().compareTo(x) > 0) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
            if (balance < -1 && node.right.getData().compareTo(x) <= 0) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        return node;
    }

    private int height(AVLNode<E> N) {
        return N == null ? 0 : N.height;
    }

    private int getBalance(AVLNode<E> N) {
        return (N == null) ? 0 : height(N.left) - height(N.right);
    }

    private AVLNode<E> rightRotate(AVLNode<E> y) {
        if (y == null || y.left == null) return y;

        AVLNode<E> x = y.left;
        AVLNode<E> T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private AVLNode<E> leftRotate(AVLNode<E> x) {
        if (x == null || x.right == null) return x;

        AVLNode<E> y = x.right;
        AVLNode<E> T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringInOrder(root, sb);
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    private void toStringInOrder(AVLNode<E> node, StringBuilder sb) {
        if (node != null) {
            toStringInOrder(node.left, sb);
            sb.append(node.getData()).append(", ");
            toStringInOrder(node.right, sb);
        }
    }
}
