package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comparator;

    // 构造函数，接受一个 Comparator 对象
    public MaxArrayDeque(Comparator<T> c) {
        super();  // 调用父类的构造函数
        this.comparator = c;  // 存储传入的 Comparator 对象
    }

    // 返回队列中的最大元素，使用默认的 Comparator
    public T max() {
        if (isEmpty()) {
            return null;  // 如果队列为空，返回 null
        }
        T maxElement = get(0);  // 假设第一个元素是最大值
        for (int i = 1; i < size(); i++) {
            T currentElement = get(i);
            if (comparator.compare(currentElement, maxElement) > 0) {
                maxElement = currentElement;  // 找到更大的元素
            }
        }
        return maxElement;
    }

    public T max(Comparator<T> c) {
        if (isEmpty()) {
            return null;  // 如果队列为空，返回 null
        }
        T maxElement = get(0);  // 假设第一个元素是最大值
        for (int i = 1; i < size(); i++) {
            T currentElement = get(i);
            if (c.compare(currentElement, maxElement) > 0) {
                maxElement = currentElement;  // 找到更大的元素
            }
        }
        return maxElement;
    }

}
