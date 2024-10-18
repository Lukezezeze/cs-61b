package deque;

public class LinkedListDeque<T> {
    public class Node{
        T value;
        Node next;
        Node prev;

        public Node(T value){
            this.value = value;
        }
    }

    Node sentinal;
    int size;

    public LinkedListDeque() {
        sentinal = new Node(null);  // 初始化 sentinel 节点，null 表示没有实际数据
        sentinal.next = sentinal;   // 初始时 sentinel 的 next 指向自己
        sentinal.prev = sentinal;   // sentinel 的 prev 也指向自己
        size = 0;                   // 初始化大小为 0
    }

    public void addFirst(T item){
        Node newnode = new Node(item);

        if(sentinal.next == sentinal){
            sentinal.next = newnode;
            sentinal.prev = newnode;
            newnode.next = sentinal;
            newnode.prev = sentinal;
        }else{
            Node ccurrent = sentinal.next;
            sentinal.next = newnode;
            newnode.next = ccurrent;
            ccurrent.prev = newnode;
            newnode.prev = sentinal;

        }

        size += 1;

    }

    public void addLast(T item){
        Node newnode = new Node(item);

        if(sentinal.next == sentinal){
            sentinal.next = newnode;
            sentinal.prev = newnode;
            newnode.prev = sentinal;
            newnode.next = sentinal;
        }else{
            Node current = sentinal.next;
            while(current.next != sentinal){
                current = current.next;
            }
            current.next = newnode;
            newnode.prev = current;
            newnode.next = sentinal;
            sentinal.prev = newnode;
        }
        size += 1;
    }

    public boolean isEmpty(){
        return sentinal.next == null;
    }

    public int size(){
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

    public T removeFirst(){
        if(sentinal.next == sentinal){
            return null;
        }else{
            Node firstnode = sentinal.next;
            Node newfirstnode = firstnode.next;
            sentinal.next = newfirstnode;
            newfirstnode.prev = sentinal;
            size -= 1;
            return firstnode.value;
        }
    }

    public T removeLast(){
        if(sentinal.next != sentinal){
            return null;
        }else {
            Node current = sentinal.next;
            while (current.next == sentinal){
                current = current.next;
            }

            Node newlastnode = current.prev;
            newlastnode.next = sentinal;
            sentinal.prev = newlastnode;
            size -= 1;
            return current.value;
        }
    }

    public T get(int index) {
        if (sentinal.next == sentinal) {
            return null;
        } else {
            Node current = sentinal.next;
            int k = 0;
            while (k != index) {
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


}
