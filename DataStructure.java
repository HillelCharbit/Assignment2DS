
public class DataStructure implements DT {

	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public DataStructure(){
		this.xSorted = new ContainerList(true);
		this.ySorted = new ContainerList(false);
		this.size = 0;
	}

	@Override
	public void addPoint(Point point) {
		Container addedToX = this.xSorted.add(point);
		Container addedToY = this.ySorted.add(point);
		addedToX.setInOtherList(addedToY);
		addedToY.setInOtherList(addedToX);
		this.size++;
	}

	@Override
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		int size = this.size;
		Point[] tmp = new Point[size];
		int i = 0;

		Container current = axis ? xSorted.head : ySorted.head;

		while(size != 0) {
			int currentVal = axis ?
					current.getData().getX() : current.getData().getY();

			if (currentVal > max)
				break;

			if (min <= currentVal)
				tmp[i++] = current.getData();

			size--;
			current = current.getNext();
		}

		Point[] output = new Point[i];

		while (i != 0) {
			output[i - 1] = tmp[i - 1];
			i--;
		}

		return output;
	}

	@Override
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDensity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void narrowRange(int min, int max, Boolean axis) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean getLargestAxis() {
		if(size == 0)
			return false;

		return xSorted.getMax()-xSorted.getMin()>ySorted.getMax()- ySorted.getMin();
	}

	@Override
	public Container getMedian(Boolean axis) {
		if(size == 0)
			return null;
		int medIndex = size/2;

		Container current = axis ? xSorted.head : ySorted.head;
		while(medIndex != 0) {
			current = current.getNext();
			medIndex--;
		}

		return current;
	}

	@Override
	public Point[] nearestPairInStrip(Container container, double width,
									  Boolean axis) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point[] nearestPair() {
		// TODO Auto-generated method stub
		return null;
	}


	//TODO: add members, methods, etc.
	public ContainerList getxSorted(){
		return this.xSorted;
	}

	public ContainerList getySorted(){
		return this.ySorted;
	}

	public int getSize(){
		return this.size;
	}
}

