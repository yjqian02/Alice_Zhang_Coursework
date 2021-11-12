// Written by zhan6698 and bogir001
public class ArrayList<T extends Comparable<T>> implements List<T> {
    private T[] a;
    private boolean isSorted;
    private int size;

    public ArrayList() {
        a = (T[]) new Comparable[2];
        isSorted = true;
        size = 0;
    }
    public boolean check_isSorted(){
        for(int i = 1; i < size; i++){
            if(a[i-1].compareTo(a[i]) > 0){
                return false;
            }
        }
        return true;
    }
    public boolean add(T element) {
        if (element == null) {
            return false;
        } else if (size >= a.length) {
            T[] big = (T[]) new Comparable[a.length * 2];
            System.arraycopy(a, 0, big, 0, a.length);
            a = big;
        }
        a[size] = element;
        size++;
        isSorted = check_isSorted();
        return true;
    }
    public boolean add(int index, T element) {
        if (element == null || index < 0 || index >= size) {
            return false;
        }
        else if (size == a.length) { //if array is full
            T[] bigger = (T[]) new Comparable[size * 2]; //make the array bigger
            System.arraycopy(a, 0, bigger, 0, size); //copy elements before adding it to the position
            a = bigger;
        }
            //copy all elements after the position at which to add over 1
            for (int copy = size- 1; copy >= index; copy--) {
                a[copy + 1] = a[copy];
            }
        //add the new element
        a[index] = element;
        size++;
        isSorted = check_isSorted();
        return true;
    }
    public void clear() {
        a = (T[]) new Comparable[2];
        isSorted = true;
        size = 0;
    }
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return a[index];
    }
    public int indexOf(T element) {
        if(element == null) {
            return -1;
        } else {
            for (int i = 0; i < size; i++) {
                if (isSorted && a[i].compareTo(element) > 0){
                    return -1;
                }
                if (a[i] != null && a[i].compareTo(element) == 0) {
                    return i;
                }
            }
            return -1;
        }
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public void sort() {
        //ArrayList<T> a = new ArrayList<T>();
        if (!isSorted) {
            for (int i = 0; i < size - 1; i++) {
                int minimum_Index = i;
                for (int j = i + 1; j < size; j++) {
                    if (a[j].compareTo(a[minimum_Index]) < 0) {
                        minimum_Index = j;
                    }
                }
                T temp = a[i];
                a[i] = a[minimum_Index];
                a[minimum_Index] = temp;
            }
            isSorted = true;
        }
    }
    public T remove(int index) {
        T element = get(index);
        if (index < 0 || index >= size || size == 0) {
            return null;
        }
        for (int i = index; i < size - 1; i++) {
            a[i] = a[i + 1];
        }
        a[size - 1] = null;
        size--;
        isSorted = check_isSorted();
        return element;
    }
    public void equalTo(T element){
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (a[i].compareTo(element) == 0) {
                count++;
            }
            else if(isSorted && a[i].compareTo(element) >0){
                break;
            }
        }
        a = (T[]) new Comparable[a.length];
        for (int i = 0; i < count; i++) {
            a[i] = element;
        }
        isSorted = true;
        size = count;
    }
    public void reverse() {
        T temp;
        for (int i = 0; i < size / 2; i++) {
            temp = (T) a[i];
            a[i] = a[size - i - 1];
            a[size - i - 1] = temp;
        }
        isSorted = check_isSorted();
    }
    public void merge(List<T> otherList) {
        ArrayList<T> other = (ArrayList<T>) otherList;
        sort();
        other.sort();
        int sizeOfSmallerList = otherList.size() + size;
        T[] mergedList = (T[]) new Comparable[sizeOfSmallerList];
        int a1 = 0, b = 0, c = 0;
        while (a1 < size() && b < other.size()){
            if (get(a1).compareTo(otherList.get(b)) <= 0) {
                mergedList[c]= get(a1);
                a1++;
            } else {
                mergedList[c] = other.get(b);
                b++;
            }
            c++;
        }
        while(a1 < size()){
            mergedList[c]= get(a1);
            a1++;
            c++;
        }
        while(b < other.size()){
            mergedList[c] = other.get(b);
            b++;
            c++;
        }
        size+= other.size();
        a = mergedList;
    }
    public boolean rotate(int n) {
        if (n <= 0 || size <= 1 || n >= size) {
            return false;
        } else {
            for (int i = 0; i < n; i++) {
                T last = (T) a[size- 1]; //take out last element

                for (int j = a.length - 1; j > 0; j--) {
                    a[j] = a[j - 1]; //shifting element of array to the right by 1
                }
                a[0] = last; //last element added to the start of array
            }
        }
        isSorted = check_isSorted();
        return true;
    }
    public String toString() {
        if (size > 0) {
            String result = "";
            for (int i = 0; i < size; i++) {
                result = result + " " + a[i] + "\n";
            }
            return result;
        } else {
            return " "; //empty list
        }
    }
    public boolean isSorted() {
        return isSorted;
    }

}






