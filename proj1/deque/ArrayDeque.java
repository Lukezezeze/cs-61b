package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    public void ArrayDeque(){
        items = (T[]) new Object[8];
        size = 0;
        nextfirst = items.length-1;
        nextlast = 0;
    }

    public void addFirst(T item){
        if(items[nextfirst] == null){
            items[nextfirst] = item;
        }

        nextfirst = (nextfirst - 1 + items.length) % items.length;
        size += 1;
    }

    public void addLast(T item){
        if(items[nextlast] == null){
            items[nextlast] = item;
        }

        nextfirst = (nextfirst + 1) % items.length;
        size += 1;
    }

    public boolean isEmpty(){
        return size() == 0;
    }

    public int size(){
        return size;
    }

    public void printDeque() {
        if (size() == 0) {
            System.out.println("Deque is empty");
            return;
        }

        int index = (nextfirst + 1) % items.length;  // 从第一个有效元素开始
        while (index != nextlast) {                  // 直到达到 nextLast 为止
            System.out.print(items[index] + " ");
            index = (index + 1) % items.length;      // 循环移动 index
        }

        System.out.println();  // 打印完成后换行
    }

    public T removeFirst(){
        if(size()==0){
            return  null;
        }

        int firstindex = (nextfirst + 1)%items.length;
        T item = items[firstindex];
        items[firstindex] = null;

        nextfirst = firstindex;
        size -= 1;
        return item;
    }

    public T removeLast(){
        if(size() == 0){
            return null;
        }

        int lastindex = (nextlast - 1 + items.length) % items.length;
        T item = items[lastindex];
        items[lastindex] = null;

        nextlast = lastindex;
        size -= 1;
        return  item;
    }

    public T get(int index){
        if(size() == 0){
            return  null;
        }

        int firstindex = (nextfirst + 1) % items.length;
        while (index != 0){
            firstindex = (firstindex + 1) % items.length;
            index -= 1;
        }

        return items[firstindex];
    }

    public void resize(int capacity){
        capacity = items.length * 2;
        T[] newitems = (T[]) new Object[capacity];

        int index = 0;
        int firstindex = (nextfirst + 1)%items.length;
        while (firstindex != nextlast){
            newitems[index] = items[firstindex];
            index += 1;
            firstindex = (firstindex + 1)%items.length;
        }
        items = newitems;
    }

}
