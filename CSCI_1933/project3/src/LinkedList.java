// Written by zhan6698 and bogir001
public class LinkedList<T extends Comparable<T>> implements List<T>{
    private boolean isSorted;
    private int size = 0;
    private Node<T> head = null;

    public LinkedList(){
        isSorted = true;
    }
    public boolean check_isSorted(){
        Node<T> current = head;
        if (head == null){
            return true;
        }
        while (current.getNext() != null){
            if (current.getNext().getData().compareTo(current.getData()) < 0){
                return false;
            }
            current = current.getNext();
        }
        return true;
    }
    public boolean add(T element){
        if (element == null){
            return false;
        }
        else if (size == 0){
            head = new Node<T>(element);
            size ++;
            isSorted = check_isSorted();
            return true;
        }
        Node current = head;
        while (current.getNext() != null){
            current = current.getNext();
        }
        Node newNode = new Node<T>(element);
        current.setNext(newNode);
        size ++;
        isSorted = check_isSorted();
        return true;
    }
    public boolean add (int index, T element){
        if (element == null || index < 0 || index >= size){
            return false;
        }
        else if (index == size|| size == 0){ // adding to end of list or if list is empty
            return add(element);
        }
        else if (index == 0){ // adding to beginning of list
            Node<T> newHead = new Node<T>(element);
            newHead.setNext(head);
            head = newHead;
            size ++;
            isSorted = check_isSorted();
            return true;
        }
        Node<T> current = head;
        int count = 0;
        while (current.getNext() != null && count < index - 1){
            current = current.getNext();
            count ++;
        }
        Node<T> next = current.getNext();
        Node<T> addNode = new Node<T>(element);
        addNode.setNext(next);
        current.setNext(addNode);
        isSorted = check_isSorted();
        size ++;
        return true;
    }
    public void clear(){
        size = 0;
        isSorted = true;
        head = null;
    }
    public T get(int index){
        if (index < 0 || index > size){
            return null;
        }
        Node<T> current = head;
        int elementNum = 0;
        while (current != null){
            if (elementNum == index) {
                return current.getData();
            }
            current = current.getNext();
            elementNum ++;
        }
        return null;
    }
    public int indexOf(T element) {
        if (element == null) {
            return -1;
        }
        Node<T> current = head;
        int elementNum = 0; // keeps track of current index
        while (current != null) {
            if (isSorted) { // increasing efficiency
                if (current.getData().compareTo(element) == 0){
                    return elementNum;
                }
                else if (current.getData().compareTo(element) > 0){
                    return -1;
                }
            }
            else if (current.getData().compareTo(element) == 0) {
                return elementNum;
            }
            current = current.getNext();
            elementNum++;
        }
        return -1;
    }
    public boolean isEmpty () {
        if (head == null){
            return true;
        }
        return false;
    }
    public int size () {
        return size;
    }
    public void sort () {
        if (!(isSorted)) { // increasing efficiency
            for (Node<T> i = head; i != null; i = i.getNext()) {
                Node<T> index = i;
                for (Node<T> j = i.getNext(); j != null; j = j.getNext()) {
                    if (j.getData().compareTo(index.getData()) < 0) {
                        index = j;
                    }
                }
                T temp = index.getData();
                index.setData(i.getData());
                i.setData(temp);
            }
            isSorted = true;
        }
    }
    public T remove(int index){
        T temp = null;
        if (index < 0 || index >= size || size == 0){
            return null;
        }
        else if (index == 0){
            temp = head.getData();
            head = head.getNext();
        }
        else {
            Node<T> prev = head;
            Node<T> current = prev.getNext();
            int count = 0;
            while (count != index - 1) { // search for index to be deleted, keep from of prev node
                prev = prev.getNext();
                current = current.getNext();
                count++;
            }
            temp = prev.getNext().getData();
            prev.setNext(current.getNext());
        }
        isSorted = check_isSorted();
        size --;
        return temp; // index not present in list
    }
    public void equalTo(T element){
        Node<T> prev = head;
        int count = 0;
        if (isSorted) {        //if element is < head you delete the whole list
            if (head.getData().compareTo(element) < 0){
                clear();
            }
            else {
                int newSize = 0;
                while (prev.getNext() != null && prev.getNext().getData().compareTo(element) == 0) {
                    head = prev;
                    prev = head;
                    prev = prev.getNext();
                    newSize ++;
                }
                prev.setNext(null);
                size = newSize;
            }
        }
        else {
            while (prev != null) {
                if (prev.getData().compareTo(element) != 0) {
                    remove(count);
                    count--;
                }
                prev = prev.getNext();
                count++;
            }
        }
        isSorted = true;
    }
    public void reverse(){
        add(head.getData());
        size --;
        head = head.getNext();
        for (int i=1; i < size; i++) {
            add(size - i, head.getData());
            size --;
            head = head.getNext();
        }
        isSorted = check_isSorted();
    }
    public void merge(List<T> otherList) {
        LinkedList<T> other = (LinkedList<T>) otherList;
        sort();
        otherList.sort();
        if (other.head == null || this.head == null){
            return;
        }
        else {
            Node<T> ptrThis = this.head;
            Node<T> ptrOther = other.head;
            Node<T> next; // temp placeholder
            Node<T> addNode; // temp node being added
            int count = 0; // keeps track of index of element
            while (ptrThis.getNext() != null && ptrOther != null){
                if (ptrOther.getData().compareTo(ptrThis.getData()) >= 0){ // add other Node to the right of this Node
                    if (ptrOther.getData().compareTo(ptrThis.getNext().getData()) > 0){
                        ptrThis = ptrThis.getNext();
                        size --;
                    }
                    else {
                        next = ptrThis.getNext();
                        addNode = new Node<T>(other.head.getData());
                        addNode.setNext(next);
                        ptrThis.setNext(addNode);
                        other.head = other.head.getNext();
                        ptrThis = ptrThis.getNext();
                        ptrOther = other.head;
                        other.size--;
                    }
                }
                else{ // add other node to the left of this Node
                    if (ptrThis == this.head){
                        next = this.head;
                        Node<T> newHead = new Node<T>(other.head.getData());
                        newHead.setNext(next);
                        this.head = newHead;
                        ptrThis = this.head;
                    }
                    else{
                        next = ptrThis.getNext();
                        addNode = new Node<T>(other.head.getData());
                        addNode.setNext(next);
                        ptrThis.setNext(addNode);
                    }
                    other.head = other.head.getNext();
                    ptrThis = ptrThis.getNext();
                    ptrOther = other.head;
                    other.size--;
                }
                size ++;
                count ++;
            }
            if (otherList.size() > 0){
                ptrThis.setNext(other.head);
                size += other.size;
            }
        }
        isSorted = true;
    }
    public boolean rotate(int n){
        if(n <= 0 || size <= 1){
            return false;
        }
        else {
            for (int i = 0; i < n; i++) {
                add(0, get(size - 1));
                remove(size -1);
            }
        }
        isSorted = check_isSorted();
        return true;
    }
    public String toString(){
        Node current = head;
        String list = "";
        int count = 0;
        while (current != null){
            list += current.getData() + " " + "\n";
            current =current.getNext();
            count ++;
        }
        return list;
    }
    public boolean isSorted(){
        return isSorted;
    }
}
