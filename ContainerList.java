import java.util.Iterator;

//public class ContainerList implements Iterable<Container> {
public class ContainerList {
    protected Container head;
    protected Container tail;
    private int size;
    private boolean axis;

    public ContainerList(boolean axis) {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.axis = axis;   // true means x
    }

    public Container add(Point point) {
        Container newNode = new Container(point);

        if (size == 0) {
            // add the first (and last) point
            this.head = newNode;
            this.tail = newNode;
        } else {
            Container current = head;
            boolean inRange = true;

            int x = point.getX();
            int y = point.getY();

            while (current != null && inRange) {
                if (this.axis)
                    inRange = current.getData().getX() < x;
                else
                    inRange = current.getData().getY() < y;

                if (inRange)
                    current = current.getNext();
            }

            if (current == null) {
                if (inRange) {
                    // add at the end
                    this.tail.setNext(newNode);
                    newNode.setPrev(this.tail);
                    this.tail = newNode;
                } else {
                    // one before tail
                    Container beforeTail = this.tail.getPrev();
                    newNode.setNext(this.tail);
                    this.tail.setPrev(newNode);
                    newNode.setPrev(beforeTail);

                    if (beforeTail == null)
                        this.head = newNode;
                    else
                        beforeTail.setNext(newNode);
                }
            } else {
                // before current
                newNode.setNext(current);
                newNode.setPrev(current.getPrev());

                if (current.getPrev() == null)
                    head = newNode;
                else
                    current.getPrev().setNext(newNode);

                current.setPrev(newNode);
            }
        }
        this.size += 1;
        return newNode;
    }

    public boolean isEmpty(){ return this.size == 0; }

    public String toString(){
        String str = "{";
        Container current = head;

        if (current != null)
            str += current.toString();

        while (current.getNext() != null){
            str += ", " + current.getNext().toString();
            current = current.getNext();
        }

        return str + "}";
    }

    public int getSize(){
        return this.size;
    }

    public int getMax(){
        if (this.axis)
            return this.tail.getData().getX();
        return this.tail.getData().getY();
    }

    public int getMin(){
        if (this.axis)
            return this.head.getData().getX();
        return this.head.getData().getY();
    }

    public int removeOutOfRange(int min, int max, ContainerList other){
        int removes = 0;

        while (head != null){
            if ((axis ? head.getData().getX() : head.getData().getY()) < min) {
                this.size--;
                other.remove(head.getInOtherList());
                head = head.getNext();
                head.setPrev(null);
            }
            else
                break;
        }

        while (tail != null){
            if ((axis ? tail.getData().getX() : tail.getData().getY()) > max) {
                this.size--;
                other.remove(tail.getInOtherList());
                tail = tail.getPrev();
                tail.setNext(null);
                removes++;
            }
            else
                break;
        }

        return removes;
    }

    public void remove(Container toRemove){
        if(size == 1) {
            head = null;
            tail = null;
        }

        if (toRemove == head)
            head = toRemove.getNext();

        if (toRemove == tail)
            tail = toRemove.getPrev();

        if (toRemove.getNext() != null)
            toRemove.getNext().setPrev(toRemove.getPrev());

        if (toRemove.getPrev() != null)
            toRemove.getPrev().setNext(toRemove.getNext());

        size--;
    }
}
