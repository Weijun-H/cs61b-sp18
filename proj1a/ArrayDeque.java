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
        _capability = 6;
    }

    /** Resize the underlying array to the target capacity. */
    private void resize(int capacity) {
        if ( capacity == 0 ) {
            capacity = 3;
        }
        T[] a = (T[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            a[i+1] = this.get(i);
        }
        nextFirst = 0;
        nextLast = size + 1;
        items = a;
        _capability = capacity - 2;
    }

    /** Insert X into the back of list*/
    public void addLast(T x) {
        if (size + 1 > _capability) {
            resize(size * 3);
        }
        items[nextLast] = x;
        nextLast = (nextLast + 1) % (_capability + 2);
        size++;
    }

    /** Insert X into the front of the list*/
    public void addFirst(T x) {
        if (size + 1 > _capability) {
            resize(size * 3);
        }
        int index = nextFirst;
        items[nextFirst] = x;
        nextFirst = (nextFirst - 1 + _capability + 2) % (_capability + 2);
        size++;
    }

    /** Remove the item in the front of list*/
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int firstIndex = (nextFirst + 1) % (_capability + 2);
        T first = items[firstIndex];
        items[firstIndex] = null;
        nextFirst = firstIndex;
        size--;
        if (Double.valueOf(size) / Double.valueOf(_capability) < 0.25) {
            resize(size * 3);
        }

        return first;
    }

    /** Remove the item in the back of list */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        int lastIndex = (nextLast - 1 + _capability + 2) % (_capability + 2);
        T last = items[lastIndex];
        items[nextLast] = null;
        nextLast = lastIndex;

        size--;
        if (Double.valueOf(size) / Double.valueOf(_capability) < 0.25) {
            resize(size * 3);
        }

        return last;
    }

    /** Gets the ith item in the list (0 is the front). */
    public T get(int index) {
        if (index+1 > size) {
            return null;
        }
        return items[(nextFirst + 1 + index)%(_capability+2)];
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
    public void printDeque() {
        int start = nextFirst;
        int end = nextLast;
        int index = (start + 1) % (_capability + 2);
        while (index != end) {
            System.out.print(items[index] + " ");
            index = (index + 1) % (_capability + 2);
        }
    }
}
