package avl;

import java.lang.classfile.components.ClassPrinter;

public class AVLNode<E extends Comparable<E>> {
    E song;
    AVLNode<E> left, right;
    int height;
    int fb;

    public AVLNode(E song) {
        this.song = song;
        this.height = 1;
        this.fb = 0;
        this.left = null;
        this.right = null;
    }

    public E getData() { return song; }
    public AVLNode<E> getRight() { return this.right; }
    public AVLNode<E> getLeft() { return this.left; }

    @Override
    public String toString() {
        return song.toString();
    }
}