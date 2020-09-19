public class ArrayDeque<T> {
    private int size;
    private T[] items;
    private int nextFirst, nextLast;
    private int _capability;

    /** Create an empty list. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 3;
        nextLast = 4;
        _capability = 8;
    }

    /** Resize the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
        _capability = capacity;
    }

    /** Insert X into the back of list*/
    public void addLast(T x) {
        if (nextLast + 1 >= size) {
            resize(size * 2);
        }
        items[nextLast] = x;
        nextLast = (nextLast + 1) % size;
        size++;
    }

    /** Insert X into the front of the list*/
    public void addFirst(T x) {
        if (nextFirst - 1 < 0) {
            resize(size * 2);
        }
        items[nextFirst] = x;
        if (nextFirst - 1 < 0) {
            nextFirst = size - 1;
        } else {
            nextFirst--;
        }
        size++;
    }

    /** Remove the item in the front of list*/
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        int firstIndex = (nextFirst + 1) % size;
        T first = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = firstIndex;
        if (Double.valueOf(size) / Double.valueOf(_capability) < 0.25) {
            resize(size * 2);
        }

        size--;
        return first;
    }

    /** Remove the item in the back of list */
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        int lastIndex;
        if (nextLast - 1 < 0) {
            lastIndex = size - 1;
        } else {
            lastIndex = nextLast - 1;
        }
        T last = items[lastIndex];

        if (Double.valueOf(size) / Double.valueOf(_capability) < 0.25) {
            resize(size * 2);
        }

        nextLast = lastIndex;
        items[nextLast] = null;
        size--;
        return last;
    }

    /** Gets the ith item in the list (0 is the front). */
    public T get(int index) {
        if (index < 0 || index > size - 1) {
            return null;
        } else {
            return items[index];
        }
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Return whether the number of items is 0 */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Print Deque */
    public void printDeque(){
        int start = nextFirst;
        int end = nextLast;
        int index = (start + 1) % size;
        while (index != end) {
            System.out.print(items[index] + " ");
            index = (index + 1) % size;
        }
    }

}
