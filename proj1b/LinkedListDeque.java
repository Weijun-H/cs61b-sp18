public class LinkedListDeque<Item> implements Deque <Item>{
    private int size;
    private Node sentFront, sentBack;

    private class Node {
        Item item;
        Node next;
        Node prev;

        Node(Item i, Node p, Node n) {
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

    @Override
    public void addFirst(Item item) {
        Node newFirst = new Node(item, sentFront, sentFront.next);
        sentFront.next.prev = newFirst;
        sentFront.next = newFirst;
        size++;
    }

    @Override
    public void addLast(Item item) {
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

    @Override
    public void printDeque() {
        Node cnt = sentFront.next;
        for (int i = 0; i < size; i++) {
            System.out.print(cnt.item + " ");
            cnt = cnt.next;
        }
    }

    @Override
    public Item removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Item temp = sentFront.next.item;
        sentFront.next = sentFront.next.next;
        sentFront.next.prev = sentFront;
        size--;
        return temp;
    }

    @Override
    public Item removeLast() {
        if (isEmpty()) {
            return null;
        }
        Item temp = sentBack.prev.item;
        sentBack.prev = sentBack.prev.prev;
        sentBack.prev.next = sentBack;
        size--;
        return temp;
    }

    @Override
    public Item get(int index) {
        Node temp = sentFront.next;
        if (index > size - 1 || index < 0 || isEmpty()) {
            return null;
        }
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp.item;
    }

    private Item getrecursive(Node p, int index, int start) {
        if (start == index + 1) {
            return p.item;
        }
        return getrecursive(p.next, index, start + 1);
    }

    public Item getRecursive(int index) {
        return getrecursive(sentFront, index, 0);
    }
}
