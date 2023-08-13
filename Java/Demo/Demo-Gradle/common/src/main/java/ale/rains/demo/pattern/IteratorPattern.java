package ale.rains.demo.pattern;

/**
 * 迭代器模式
 * Java迭代器模式是一种行为设计模式，它提供了一种访问集合对象元素的方法，而不需要暴露该对象的内部表示。
 * 该模式适用于需要遍历集合对象的场景，例如数组、列表、树等。
 */
public class IteratorPattern {
    /*
    interface Iterator<T> {
        boolean hasNext();
        T next();
    }
    // 具体迭代器实现类
    class ArrayIterator<T> implements Iterator<T> {
        private T[] array;
        private int currentIndex;
        public ArrayIterator(T[] array) {
            this.array = array;
            this.currentIndex = 0;
        }
        public boolean hasNext() {
            return currentIndex < array.length;
        }
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = array[currentIndex];
            currentIndex++;
            return element;
        }
    }
    // 使用迭代器遍历数组
    public class Main {
        public static void main(String[] args) {
            Integer[] array = {1, 2, 3, 4, 5};
            Iterator<Integer> iterator = new ArrayIterator<>(array);
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        }
    }
    */
}
