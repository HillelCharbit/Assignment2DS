import java.util.Iterator;

/**
 * A class that functions like a doubly linked list of Containers.
 * The points in the list are always sorted in an increasing order.
 */
public class ContainerList {
    protected Container head;
    protected Container tail;

    private Container median;
    private int size;
    private final boolean axis;   // true means X axis while false means Y axis

    public ContainerList(boolean axis) {
        this.head = null;
        this.tail = null;
        this.axis = axis;
        this.size = 0;
    }

    /**
     * Add a point to the list, keeping the list sorted.
     * @param point a point to add to the list.
     * @return a new Container containing the point.
     */
    public Container add(Point point) {
        Container newNode = new Container(point), current = this.head, beforeCurrent;
        boolean isSmaller;
        int val, medVal;    // value of new node and the current median

        // if the list is empty
        if (size == 0) {
            // this point will be the head and the tail
            this.head = newNode;
            this.tail = newNode;
        } else {
            // compare each point's value to the value of the new point (by the provided axis)
            while (current != null) {
                // check if the current value is less than the given value
                isSmaller = axis ? current.getX() < point.getX() : current.getY() < point.getY();

                if (isSmaller)
                    current = current.getNext();
                else
                    break;
            }
            // if the loop ended with current being null, then isSmall is true -
            // the tail is smaller than the given value
            if (current == null) {
                // add the point in the end (make it the tail)
                this.tail.setNext(newNode);
                newNode.setPrev(this.tail);
                this.tail = newNode;
            }
            // insert the point before the last point that we checked
            else {
                beforeCurrent = current.getPrev();
                newNode.setNext(current);
                newNode.setPrev(beforeCurrent);

                if (beforeCurrent == null)
                    this.head = newNode;
                else
                    beforeCurrent.setNext(newNode);

                current.setPrev(newNode);
            }
        }

        // update the median
        if (this.size == 0)
            this.median = newNode;
        else {
            val = axis ? point.getX() : point.getY();
            medVal = axis ? this.median.getX() : this.median.getY();

            if (this.size % 2 == 0 && val < medVal)
                // the middle point is now a valid index, the index of the current median.
                // but the current median is pushed forward. e.g. 1 2 3 4, median = 3, a = 0 -> 0 1 2 3 4, median = 2.
                this.median = this.median.getPrev();
            else if (this.size % 2 == 1 && val > medVal)
                // the middle point is now not a valid index, the ceiling is the index of the current median + 1.
                // e.g. 1 2 3 4 5, median = 3, a = 6 -> 1 2 3 4 5 6, median = 4.
                this.median = this.median.getNext();
        }

        this.size += 1;
        return newNode;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public String toString() {
        String str = "{";
        Container current = head;

        if (current != null) {
            str += current.toString();

            while (current.getNext() != null) {
                str += ", " + current.getNext().toString();
                current = current.getNext();
            }
        }

        return str + "}";
    }

    public int getSize() {
        return this.size;
    }

    /** @return the max. value of a point in the list (by the given axis). */
    public int getMax() {
        if (this.axis)
            return this.tail.getX();
        return this.tail.getY();
    }

    /** @return the min. value of a point in the list (by the given axis). */
    public int getMin() {
        if (this.axis)
            return this.head.getX();
        return this.head.getY();
    }

    /**
     * Remove a point from the list.
     * @param toRemove the container to remove (A REFERENCE!)
     */
    public void remove(Container toRemove) {
        if (this.isEmpty())
            throw new IllegalArgumentException();

        int val, medVal;    // value of new node and the current median

        if (this.size == 1) {
            // reset
            this.head = null;
            this.tail = null;
            this.median = null;
            this.size = 0;
        }
        else if (toRemove != null) {    // and also the size is at least 2
            val = axis ? toRemove.getX() : toRemove.getY();
            medVal = axis ? this.median.getX() : this.median.getY();

            if (this.size % 2 == 0 && (val >= medVal))
                // for example 1 2 3 4, median = 3, removing 3 -> 1 2 4, median = 2
                // or 1 2 3 4, m = 3, removing 4 -> 1 2 3, m = 2
                this.median = this.median.getPrev();
            else if (this.size % 2 == 1 && (val <= medVal))
                // for example 1 2 3 4 5, median = 3, removing 3 -> 1 2 4 5, median = 4
                // or 1 2 3 4 5, median = 3, removing 2 -> 1 3 4 5, median = 4
                this.median = this.median.getNext();

            if (toRemove == head)
                head = toRemove.getNext();

            if (toRemove == tail)
                tail = toRemove.getPrev();

            if (toRemove.getNext() != null)
                toRemove.getNext().setPrev(toRemove.getPrev());

            if (toRemove.getPrev() != null)
                toRemove.getPrev().setNext(toRemove.getNext());

            this.size--;
        }
    }

    public Container getMedian(){
        return this.median;
    }

    public void addLast(Point point){
        this.addLast(new Container(point));
    }

    public void addLast(Container point){
        if (this.tail == null){
            this.tail = point;
            this.head = point;
            this.median = point;
        }
        else{
            this.tail.setNext(point);
            point.setPrev(this.tail);
            this.tail = point;

            if (this.size % 2 == 1)
                this.median = this.median.getNext();
        }

        this.size++;
    }

    public void addFirst(Point point){
        this.addFirst(new Container(point));
    }

    public void addFirst(Container point){
        if (this.head == null){
            this.head = point;
            this.tail = point;
            this.median = point;
        }
        else{
            this.head.setPrev(point);
            point.setNext(this.head);
            this.head = point;

            if (this.size % 2 == 0)
                this.median = this.median.getPrev();
        }

        this.size++;
    }
}