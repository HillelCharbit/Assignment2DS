public class DataStructure implements DT {
	private  ContainerList xSorted; // a list of the points, sorted by the X values
	private  ContainerList ySorted; // a list of the points, sorted by the Y values
	private  int size;

	//////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
	public DataStructure() {
		this.xSorted = new ContainerList(true);
		this.ySorted = new ContainerList(false);
		this.size = 0;
	}

	public  DataStructure(DataStructure dt){

	}

	/**
	 * Add a point to the data structure.
	 * @param point a point to add.
	 */
	@Override
	public void addPoint(Point point) {
		// add the point to both lists and save the returned containers
		Container addedToX = this.xSorted.add(point);
		Container addedToY = this.ySorted.add(point);

		// link each container to its twin on the other list
		addedToX.setInOtherList(addedToY);
		addedToY.setInOtherList(addedToX);

		this.size++;
	}

	/**
	 * Get all the points in a specific range according to some axis X or Y.
	 * @param min the min. value of a point in the desired axis.
	 * @param max the max. value of a point in the desired axis.
	 * @param axis the axis to check, true for X, false for Y.
	 * @return an array of points that their X/Y value is in [min, max].
	 */
	@Override
	public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {
		int i = 0, currentVal;
		// create two array of points, one in the biggest size possible, other not initialized
		Point[] tmp = new Point[size], output;
		Container current = axis ? xSorted.head : ySorted.head;

		while (current != null) {
			currentVal = axis ? current.getData().getX() : current.getData().getY();

			// no need to check the elements after max, they're all out of range
			if (currentVal > max)
				break;

			// min <= x <= max therefore in range, add to the array
			if (min <= currentVal)
				tmp[i++] = current.getData();

			current = current.getNext();
		}
		output = deleteArrayTail(tmp,i);
		return output;
	}
	private Point[] deleteArrayTail(Point[] toDeleteFrom, int i){
		Point[] output = new Point[i];
		if (i != this.size) {
			// create an array of the correct size and copy the points to it
			while (i != 0) {
				output[i - 1] = toDeleteFrom[i - 1];
				i--;
			}
		}
		else output = toDeleteFrom;
		return output;
	}

	/**
	 * Get all the points in a specific range according to some axis X or Y.
	 * @param min the min. value of a point in the desired axis.
	 * @param max the max. value of a point in the desired axis.
	 * @param axis the axis NOT to check, true for X, false for Y.
	 * @return an array of points that their X/Y value is in [min, max].
	 */
	@Override
	public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {
		int i = 0, currentVal;
		// create two array of points, one in the biggest size possible, other not initialized
		Point[] tmp = new Point[size], output;

		//looking into the opposite Axis data
		Container current = !axis ? xSorted.head : ySorted.head;

		while (current != null) {
			currentVal = axis ? current.getData().getX() : current.getData().getY();

			// value is is range
			if (currentVal >= min && currentVal <= max)
				tmp[i++] = current.getData();

			current = current.getNext();
		}
		output = deleteArrayTail(tmp,i);
		return output;
	}

	@Override
	public double getDensity() {
		return (double)(this.size) / ((this.xSorted.getMax() - this.xSorted.getMin()) *
				(this.ySorted.getMax() - this.ySorted.getMin()));
	}

	/**
	 * Remove all the points out of a given range.
	 * @param min the min. value of a point that should stay.
	 * @param max the max. value of a point that should stay.
	 */
	@Override
	public void narrowRange(int min, int max, Boolean axis) {
		ContainerList sorted = axis ? this.xSorted : this.ySorted;
		ContainerList other = axis ? this.ySorted : this.xSorted;

		// start by removing all the elements smaller than min
		// begin at the head of the list, then move forward
		while (sorted.head != null){
			// if the value is smaller
			if ((axis ? sorted.head.getX() : sorted.head.getY()) < min){
				other.remove(sorted.head.getInOtherList());
				sorted.remove(sorted.head);
			} else break;
		}

		// continue by removing all the elements greater than min
		// begin at the tail of the list, then move backwards
		while (sorted.tail != null){
			// if the value is smaller
			if ((axis ? sorted.tail.getX() : sorted.tail.getY()) > max){
				other.remove(sorted.tail.getInOtherList());
				sorted.remove(sorted.tail);
			} else break;
		}

		this.size = sorted.getSize();
	}

	/** @return the wider/larger axis (true for X, false for Y).*/
	@Override
	public Boolean getLargestAxis() {
		if (size == 0)
			return false;

		return xSorted.getMax() - xSorted.getMin() > ySorted.getMax() - ySorted.getMin();
	}

//    /**
//     *  Get the median point in the structure according to the chosen axis.
//     * @param axis true for X, false for Y.
//     * @return the median point by the axis.
//     */
//    @Override
//    public Container getMedian(Boolean axis) {
//        if (size == 0)
//            return null;
//
//        Container current = axis ? xSorted.head : ySorted.head;
//
//        // using the fact that the list is always sorted, we know where the median value is.
//        for (int i = 0; i < size / 2; i++)
//            current = current.getNext();
//
//        return current;
//    }

	/**
	 * Get the median point in the structure according to the chosen axis.
	 * @param axis true for X, false for Y.
	 * @return the median point by the axis.
	 */
	public Container getMedian(Boolean axis){
		if (size == 0)
			return null;

		return axis ? xSorted.getMedian() : ySorted.getMedian();
	}

//	public ContainerList getStrip(Container point, double width, boolean axis){
//		int min = axis ? (int)((double)point.getX() - width) :  (int)((double)point.getY() - width);
//		int max = axis ? (int)((double)point.getX() + width) :  (int)((double)point.getY() + width);
//		ContainerList strip = new ContainerList(axis);
//		Container original = new Container(point);
//		int val = axis ? point.getX() : point.getY();
//
//		while (val <= max){
//			strip.addLast(point);
//			point = point.getNext();
//
//			if (point != null)
//				val = axis ? point.getX() : point.getY();
//			else break;
//		}
//
//		point = original.getPrev();
//
//		if (point != null) {
//			val = axis ? point.getX() : point.getY();
//
//			while (min <= val) {
//				strip.addFirst(point);
//				point = point.getPrev();
//
//				if (point != null)
//					val = axis ? point.getX() : point.getY();
//				else break;
//			}
//		}
//
//		return strip;
//	}

	@Override
	public Point[] nearestPairInStrip(Container container, double width,
									  Boolean axis) {
		int min = axis ? (int)((double)container.getX() - width) :  (int)((double)container.getY() - width);
		int max = axis ? (int)((double)container.getX() + width) :  (int)((double)container.getY() + width);

		return nearestPointInStripHelper(container, min, max, axis);
	}

	public Point[] nearestPointInStripHelper(Container point, int min, int max, Boolean axis){
		Point[] nearestPairOfPoint = new Point[2];

		int cntBefore = 0, cntAfter = 0;

		while ((cntBefore < 3) && (point.getPrev() != null) && (point.getPrev().getVal(axis) >= min)){
			cntBefore++;
			point = point.getPrev();
		}

		while ((cntAfter < 3) && (point.getNext() != null) && (point.getNext().getVal(axis) >= min)){
			cntAfter++;
			point = point.getNext();
		}

		return nearestPairOfPoint;
	}

	@Override
	public Point[] nearestPair() {
		// TODO Auto-generated method stub
		return null;
	}

	//TODO: add members, methods, etc.
	public ContainerList getxSorted() {
		return this.xSorted;
	}

	public ContainerList getySorted() {
		return this.ySorted;
	}

	public int getSize() {
		return this.size;
	}
}