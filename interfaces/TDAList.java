package interfaces;

public interface TDAList<T> {
	void insert (T x);
	boolean search(T x);
	void remove(T x);
	boolean isEmpty();
}