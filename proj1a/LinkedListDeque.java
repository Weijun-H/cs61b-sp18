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
        Node newFirst = new Node(item, sentFront, sentBack);
        sentFront.prev = newFirst;
        sentFront = newFirst;
        size++;
    }

    public void addLast(T item) {
        Node newLast = new Node(item, sentFront, sentBack);
        sentBack.next = newLast;
        sentBack = newLast;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
    public void printDeque() {
        Node cnt = sentFront;
        for (int i = 0; i < size; i++) {
            System.out.print(cnt.item + " ");
            cnt = cnt.next;
        }
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T temp = sentFront.item;
        sentFront = sentFront.next;
        sentFront.prev = sentBack;
        size--;
        return temp;
    }
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T temp = sentBack.item;
        sentBack = sentBack.prev;
        sentBack.next = sentFront;
        size--;
        return temp;
    }
    public T get(int index) {
        Node temp = sentBack;
        if (index > size - 1 || index < 0) {
            return null;
        }
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }
    private T getRecursive(Node p, int index, int start) {
        if (start == index + 1) {
            return p.item;
        }
        return getRecursive(p.next, index, start + 1);
    }

    public T getRecursive(int index) {
        return getRecursive(sentFront, index,  0);
    }
}
