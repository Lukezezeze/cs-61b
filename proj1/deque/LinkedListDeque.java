package deque;


import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    public class Node {
        private T value;
        private Node next;
        private Node prev;

        public Node(T value) {
            this.value = value;
        }
    }

    private Node sentinal;
    private int size;

    public LinkedListDeque() {
        sentinal = new Node(null);  // 初始化 sentinel 节点，null 表示没有实际数据
        sentinal.next = sentinal;   // 初始时 sentinel 的 next 指向自己
        sentinal.prev = sentinal;   // sentinel 的 prev 也指向自己
        size = 0;                   // 初始化大小为 0
    }

    public void addFirst(T item) {
        Node newnode = new Node(item);

        if (sentinal.next == sentinal) {
            sentinal.next = newnode;
            sentinal.prev = newnode;
            newnode.next = sentinal;
            newnode.prev = sentinal;
        } else {
            Node ccurrent = sentinal.next;
            sentinal.next = newnode;
            newnode.next = ccurrent;
            ccurrent.prev = newnode;
            newnode.prev = sentinal;

        }

        size += 1;

    }

    public void addLast(T item) {
        Node newnode = new Node(item);

        if (sentinal.next == sentinal) {
            sentinal.next = newnode;
            sentinal.prev = newnode;
            newnode.prev = sentinal;
            newnode.next = sentinal;
        } else {

            Node newNode = new Node(item);
            newNode.prev = sentinal.prev;
            newNode.next = sentinal;
            sentinal.prev.next = newNode;
            sentinal.prev = newNode;
        }
        size += 1;
    }




    public int size() {
        return size;

    }

    public void printDeque() {
        // 判断链表是否为空
        if (sentinal.next == sentinal) {
            System.out.println("The list is empty");
            return;
        }

        // 从第一个节点开始遍历
        Node current = sentinal.next;

        // 循环直到回到 sentinel,对比语句while(current.next ！= sentinal)
        while (current != sentinal) {
            // 打印当前节点的值
            System.out.print(current.value + " ");
            // 移动到下一个节点
            current = current.next;
        }

        // 换行
        System.out.println();
    }

    public T removeFirst() {
        if (sentinal.next == sentinal) {
            return null;
        } else {
            Node firstnode = sentinal.next;
            Node newfirstnode = firstnode.next;
            sentinal.next = newfirstnode;
            newfirstnode.prev = sentinal;
            size -= 1;
            return firstnode.value;
        }

    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node last = sentinal.prev;
        sentinal.prev = last.prev;
        last.prev.next = sentinal;
        size--;
        return last.value;
    }

    public T get(int index) {
        if (sentinal.next == sentinal) {
            return null;
        } else {
            if (index < 0 || index >= size) {
                return null;
            }
            Node current = sentinal.next; // 假设有一个哨兵节点
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current.value;
        }
    }

    public T getRecursive(int index) {
        // 判断链表是否为空
        if (sentinal.next == sentinal) {
            return null; // 空链表，返回 null
        }
        // 调用辅助递归方法，从第一个实际节点开始查找
        return getRecursiveHelper(sentinal.next, index);
    }

    // 私有的辅助递归方法
    private T getRecursiveHelper(Node current, int index) {
        // 递归终止条件：如果 index 为 0，返回当前节点的值
        if (index == 0) {
            return current.value;
        }
        // 否则继续递归查找下一个节点，index 减 1
        return getRecursiveHelper(current.next, index - 1);
    }

    private class LinkedListDequeIterator implements Iterator<T> {
        private Node current;

        // 构造函数，初始化从sentinel的下一个节点开始迭代
        LinkedListDequeIterator() {
            current = sentinal.next;
        }

        // hasNext() 方法，判断是否还有下一个元素
        @Override
        public boolean hasNext() {
            return current != sentinal; // 当current不是sentinel时，还有下一个元素
        }

        // next() 方法，返回当前节点的值，并将游标移动到下一个节点
        @Override
        public T next() {
            T value = current.value; // 获取当前节点的值
            current = current.next;  // 将current指向下一个节点
            return value;            // 返回当前节点的值
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        // If it's the same object, return true
        if (this == o) {
            return true;
        }

        // If the object is not a LinkedListDeque or is null, return false
        if (!(o instanceof LinkedListDeque<?>)) {
            return false;
        }

        // Cast the object to LinkedListDeque
        LinkedListDeque<?> other = (LinkedListDeque<?>) o;

        // Check if sizes are different
        if (this.size() != other.size()) {
            return false;
        }

        // Compare elements in both deques
        Node thiscurrent = this.sentinal.next;
        Node ocurrent = (Node) other.sentinal.next;

        while (thiscurrent != this.sentinal) {
            if (!thiscurrent.value.equals(ocurrent.value)) {  // Use equals() for object comparison
                return false;
            }
            thiscurrent = thiscurrent.next;
            ocurrent = ocurrent.next;
        }

        return true;
    }
}
