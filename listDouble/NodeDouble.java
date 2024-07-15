package listDouble;

public class NodeDouble<T> extends Node<T> {
	private NodeDouble<T> next;
    private NodeDouble<T> back;

   	public NodeDouble(T data) {
		super(data);
		this.back = null;
        this.next = null;
    }

   	public NodeDouble(T data, NodeDouble<T> next, NodeDouble<T> back) {
		super(data);    
		this.back = back;
        this.next = next;    
    }

   	public NodeDouble<T> getBack() {
        return this.back;
    }

    public NodeDouble<T> getNext() {
        return this.next;
    }

   	public void setBack(NodeDouble<T> back){
		this.back = back;
	}
    public void setNext(NodeDouble<T> next){
		this.next = next;
	}
} 