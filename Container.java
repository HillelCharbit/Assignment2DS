
//Don't change the class name
public class Container{
	private Point data;//Don't delete or change this field;
	
	//Don't delete or change this function
	public Point getData() { return data; }
	public Container getPrev() { return prev; }
	public Container getNext() { return next; }
	public Container getInOtherList() { return inOtherList; }

	public Container(Point point, Container prev, Container next){
		this.data = point;
		this.prev = prev;
		this.next = next;
		this.inOtherList = null;
	}

	public Container(Point point){
		this.data = point;
		this.prev = null;
		this.next = null;
		this.inOtherList = null;
	}

	public Container(Container other){
		this.data = other.data;
		this.prev = other.prev;
		this.next = other.next;
		this.inOtherList = other.inOtherList;
	}

	public void setInOtherList(Container me){ this.inOtherList = me; }
	public void setNext(Container next){ this.next = next; }
	public void setPrev(Container prev){ this.prev = prev; }

	public String toString(){
		if (data == null)
			return "()";
		return this.data.toString();
	}

	// test
}

