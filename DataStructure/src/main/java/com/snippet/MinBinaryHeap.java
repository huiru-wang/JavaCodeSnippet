package com.snippet;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 小顶堆: 数组存储元素(必须可比较)，构造成完全二叉树结构
 * ===================索引=====================
 * 如当前元素索引为index
 * 父节点为: (index-1)/2
 * 左子节点为: index*2 +1
 * 右子节点为: index*2 +2
 * <p>
 * ==================Sift Up===================
 * 当添加元素时，元素处于数组尾部，需要保证最小堆仍然成立
 * 则要与其父节点比较，如果小于，则进行交换，直到大于
 * <p>
 * =================Sift Down==================
 * 当取出堆顶元素，将尾部元素放入堆顶，进行下沉操作
 * 不断比较子节点，与较小的一个进行交换，直到满足最小堆
 * <p>
 * ===================API======================
 * size、isEmpty、push、pop、peek
 *
 * @param <E>
 */
public class MinBinaryHeap<E> {

    private Object[] queue;

    private int size;

    private final Comparator<? super E> comparator;

    public MinBinaryHeap(int capacity, Comparator<? super E> comparator) {
        this.queue = new Object[capacity];
        this.comparator = comparator;
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E peek() {
        if (size == 0) {
            throw new IllegalArgumentException("size is 0");
        }
        return (E) queue[0];
    }

    public void push(E element) {
        if (size >= queue.length) {
            grow();
        }
        queue[size++] = element;
        siftUp(size - 1);
    }

    public E pop() {
        if (size == 0) {
            throw new IllegalArgumentException("size is 0");
        }
        E minEle = peek();
        swap(0, size - 1);
        queue[--size] = null;
        siftDown(0);
        return minEle;
    }


    /**
     * queue扩容
     */
    private void grow() {
        int oldCapacity = queue.length;
        // 如果size < 64 则扩容2，否则扩容50%
        int newCapacity = oldCapacity;
        if (oldCapacity < 64) {
            // 防止空间浪费
            newCapacity += 2;
        } else if (oldCapacity + (oldCapacity << 1) > 0) {
            // 防溢出，如果扩容50%溢出，则抛异常，溢出必须用加，加之后变负数
            newCapacity >>= 1;
        } else {
            throw new OutOfMemoryError("new capacity is too large");
        }
        this.queue = Arrays.copyOf(queue, newCapacity);
    }

    private void siftUp(int index) {
        // 父节点 > 当前 则上浮，直到满足最小堆
        while (index > 0 && comparator.compare((E) queue[parent(index)], (E) queue[index]) > 0) {
            swap(parent(index), index);
            index = parent(index);
        }
    }

    private void siftDown(int index) {
        // 左节点存在，则继续
        while (left(index) < size) {
            // next 为要进行交换的索引
            int next = left(index);
            // 如果右边更小，则修改next
            if (next + 1 < size && comparator.compare((E) queue[next + 1], (E) queue[next]) < 0) {
                next = right(index);
            }
            // 如果index更小，则退出，完成siftDown
            if (comparator.compare((E) queue[index], (E) queue[next]) < 0) {
                break;
            }
            // index更小，继续下沉
            swap(index, next);
            index = next;
        }
    }

    private int parent(int index) {
        if (index == 0)
            throw new IllegalArgumentException("index 0 has no parent");
        return (index - 1) / 2;
    }

    private int left(int index) {
        return index * 2 + 1;
    }

    private int right(int index) {
        return index * 2 + 2;
    }

    private void swap(int i, int j) {
        Object e = queue[i];
        queue[i] = queue[j];
        queue[j] = e;
    }

}
