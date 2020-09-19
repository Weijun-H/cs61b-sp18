public class LinkedListDeque<T> {
    private int size;
    private Node sentFront, sentBack;
    private class Node {
        T item;
        Node next;
        Node prev;
        Node(T i, Node n, Node p) {
            item = i;
            next = n;
            prev = p;
        }
        Node() {
            item = null;
            next = prev = null;
        }
    }

    public LinkedListDeque() {
        sentBack = new Node();
        sentFront = new Node();
        sentFront.next = sentFront.prev = sentBack;
        sentBack.next = sentBack.prev = sentFront;
        size = 0;
    }

    public void addFirst(T item) {
        Node newFirst = new Node(item, sentFront, sentFront.next);
        sentFront.next.prev = newFirst;
        sentFront.next = newFirst;
        size++;
    }

    public void addLast(T item) {
        Node newLast = new Node(item, sentBack.prev, sentBack);
        sentBack.prev.next = newLast;
        sentBack.prev = newLast;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
    public void printDeque() {
        Node cnt = sentFront.next;
        for (int i = 0; i < size; i++) {
            System.out.print(cnt.item + " ");
            cnt = cnt.next;
        }
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T temp = sentFront.next.item;
        sentFront.next = sentFront.next.next;
        sentFront.next.prev = sentBack;
        size--;
        return temp;
    }
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T temp = sentBack.prev.item;
        sentBack.prev = sentBack.prev.prev;
        sentBack.prev.next = sentFront;
        size--;
        return temp;
    }
    public T get(int index) {
        Node temp = sentBack.next;
        if (index > size - 1 || index < 0) {
            return null;
        }
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }
    private T getrecursive(Node p, int index, int start) {
        if (start == index + 1) {
            return p.item;
        }
        return getrecursive(p.next, index, start + 1);
    }

    public T getRecursive(int index) {
        return getrecursive(sentFront, index,  0);
    }
}
