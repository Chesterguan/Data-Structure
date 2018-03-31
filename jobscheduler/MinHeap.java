
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/*
 * 
 * @Ziyuan Guan 94722121
 * 
 * */
public class MinHeap<T extends Comparable> {
 
    private ArrayList<T> arr = new ArrayList<>();
 
    public MinHeap() {
    }
 
    /**
     * get the original array
     */
    public MinHeap(Collection<T> arr) {
        this.arr.addAll(arr);
        for (int i = parentIndex(arr.size()-1); i >= 0; --i) {
            fixDown(i);
        }
    }
 
    /**
     * swap a and b
     */
    private void swap(int a, int b) {
        T tmp = arr.get(a);
        arr.set(a, arr.get(b));
        arr.set(b, tmp);
    }
 
    /**
     * get the index of the parent
     */
    private int parentIndex(int index) {
        return (index - 1) >> 1;
    }
 
    /**
     * get the index of the left son
     */
    private int leftSon(int index) {
        return (index << 1) + 1;
    }
 
    /**
     * get the index of right son
     */
    private int rightSon(int index) {
        return (index << 1) + 2;
    }
 
    /**
     * fix the heap up-down
     */
    private void fixUp(int i) {
        if (i <= 0 || i >= arr.size())
            return;
        // move the child up until reach the father
        for (int j = parentIndex(i);
                j >= 0 && arr.get(j).compareTo(arr.get(i)) > 0;
                i = j, j = parentIndex(j)) {
            swap(i, j);
        }
    }
 
    /**
     * adjust the heap up-down
     */
    private void fixDown(int i) {
        int son = leftSon(i);
        while (son < arr.size()) {
            // swap the smaller child
            if (son + 1 < arr.size() && arr.get(son + 1).compareTo(arr.get(son)) < 0)
                son += 1;
            if (arr.get(i).compareTo(arr.get(son)) <= 0)
                break;
            swap(i, son);
            i = son;
            son = leftSon(i);
        }
    }
 
    /**
     * push the new node
     */
    public void push(T val) throws Exception{
        arr.add(val);
        fixUp(arr.size()-1);
    }
 
    /**
     * pop out the smallest child pop the top
     */
    public T pop() {
        if (arr.isEmpty())
            return null;
        T res = arr.get(0);
        swap(0, arr.size()-1);
        arr.remove(arr.size()-1);
        fixDown(0);
        return res;
    }
 
    /**
     * find the top of the heap
     */
    public T top() {
        if (arr.isEmpty())
            return null;
        return arr.get(0);
    }
 
    public int size() {
        return arr.size();
    }
 
    @Override
    public String toString() {
        return "MinHeap: " + arr.toString();
    }
}