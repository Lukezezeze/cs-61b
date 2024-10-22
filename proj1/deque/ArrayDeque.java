package deque;


import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T>,Deque<T> {
    private T[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    public  ArrayDeque(){
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
        // 检查队列是否为空，或索引无效
        if (size() == 0 || index < 0 || index >= size()) {
            return null;
        }

        // 计算目标元素的实际索引位置
        int targetIndex = (nextfirst + 1 + index) % items.length;

        // 返回目标位置的元素
        return items[targetIndex];
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

    private class ADiterator<T> implements Iterator<T>{
        private int pos;
        private int count;

        public ADiterator(){
            pos = (nextfirst +1) % items.length;
            count = 0;
        }

        @Override
        public boolean hasNext() {
            return count < size;
        }

        @Override
        public T next() {
            T value = (T) items[pos];
            pos = (pos+1)%items.length;
            count += 1;
            return value;

        }

    }

    @Override
    public Iterator<T> iterator(){
        return new ADiterator<T>();
    }

    @Override
    public boolean equals(Object o) {
        // If it's the same object, return true
        if (this == o) {
            return true;
        }

        // If the object is not an ArrayDeque or is null, return false
        if (!(o instanceof ArrayDeque<?>)) {
            return false;
        }

        // Cast the object to ArrayDeque
        ArrayDeque<?> other = (ArrayDeque<?>) o;

        // Check if sizes are different
        if (this.size() != other.size()) {
            return false;
        }

        // Compare elements in both deques
        for (int i = 0; i < this.size(); i++) {
            T thisItem = this.get(i);
            Object otherItem = other.get(i);
            if (!thisItem.equals(otherItem)) {  // Use equals() for object comparison
                return false;
            }
        }

        return true;
    }

}
