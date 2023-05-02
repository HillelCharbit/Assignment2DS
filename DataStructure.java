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
		output = deleteArrayTail(tmp, 0, i);
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
		output = deleteArrayTail(tmp, 0, i);
		return output;
	}

	/**
	 * Calculate the density of the DT, meaning the ratio between the number of points and the area covered by them.
	 * @return the density of points in the structure.
	 */
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

	@Override
	public Point[] nearestPairInStrip(Container container, double width,
									  Boolean axis) {
		Point[] PointsInStrip = pointsInStrip(container,width,axis);
		Point[] Pair = new Point[2];

		return Pair;
	}

	@Override
	public Point[] nearestPair() {
		// TODO Auto-generated method stub
		return null;
	}

	//TODO: add members, methods, etc.

	/************** HELPERS ********************/

	public ContainerList getxSorted() {
		return this.xSorted;
	}

	public ContainerList getySorted() {
		return this.ySorted;
	}

	public int getSize() {
		return this.size;
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

	/**
	 * Get a sorted array of all the points in a strip.
	 * @param container the middle of the strip, by the given axis.
	 * @param width the width of the strip.
	 * @param axis the axis to sort by and to determine the size of the strip.
	 * @return all the points in a strip.
	 */
	public Point[] pointsInStrip(Container container, double width, Boolean axis) {
		// wasteful, should be organized as a containerList. rearranged with mergesort
		Point[] points = new Point[size];
		Container current = container;
		int index = size / 2, containerVal = current.getVal(axis), currentVal = containerVal, count = 0;

		// while the current value is not less than the minimum value
		while ((double) currentVal >= (double) containerVal - width / 2) {
			// put the point in the array, then proceed to check the prev. value
			points[index--] = current.getData();
			count++;
			current = current.getPrev();

			if(current == null)
				break;

			currentVal = current.getVal(axis);
		}

		// reset, now the other direction
		current = container;
		currentVal = containerVal;
		index = size / 2;

		while ((double) currentVal <= (double) containerVal + width / 2) {
			points[index++] = current.getData();
			current = current.getNext();
			count++;

			if(current == null)
				break;

			currentVal = current.getVal(axis);
		}

		// minus one, because we counter the middle twice
		return deleteArrayTail(points, index - count + 1, count - 1);
	}

	/**
	 * Cut a given array.
	 * @param toDeleteFrom an array to extract elements from.
	 * @param beginning the index of the first element to copy.
	 * @param len the length of the relevant data.
	 * @return a new array of the first len elements of toDeleteFrom.
	 */
	private Point[] deleteArrayTail(Point[] toDeleteFrom, int beginning, int len){
		Point[] output = new Point[len];

		if (len != this.size)
			// create an array of the correct size and copy the points to it
			for (int i = len; i != 0; i--)
				output[i - 1] = toDeleteFrom[beginning + i - 1];
		else
			output = toDeleteFrom;

		return output;
	}

	/**
	 * Sort an array of points by a given axis using merge sort.
	 * @param array the array to sort.
	 * @param axis the axis to sort by.
	 */
	public void mergeSort(Point[] array, Boolean axis){
		mergeSortSort(array, axis, 0, array.length - 1);
	}

	/**
	 * The actual merge sort.
	 * @param array an array to sort.
	 * @param axis an axis to sort by.
	 * @param left beginning of the section to sort.
	 * @param right end of the section to sort.
	 */
	private void mergeSortSort(Point[] array, Boolean axis, int left, int right){
		if (left < right){
			mergeSortSort(array, axis, left, (right + left) / 2);
			mergeSortSort(array, axis, (right + left) / 2 + 1, right);
			mergeSortMerge(array, axis, left, right);
		}
	}

	/**
	 * The merging part of the merge sort - merge two halves of a section of the array.
	 * @param array the array to sort.
	 * @param axis the axis to sort by.
	 * @param left beginning of the section to sort.
	 * @param right end of the section to sort.
	 */
	private void mergeSortMerge(Point[] array, Boolean axis, int left, int right){
		int middle = (right + left) / 2, leftHalfSize = middle - left + 1, rightHalfSize = right - middle;
		int takenFromLeft = 0, takenFromRight = 0;
		Point[] leftHalf = new Point[leftHalfSize], rightHalf = new Point[rightHalfSize];

		// copy the points to two arrays, left and right

		for (int i = 0; i < rightHalfSize; i++){
			leftHalf[i] = array[left + i];
			rightHalf[i] = array[middle + 1 + i];
		}

		// if one half is bigger, it is the left half )contains the middle)
		if (leftHalfSize > rightHalfSize)
			leftHalf[rightHalfSize] = array[middle];

		// assuming left and right array are sorted!

		// while not over with both halves
		while (takenFromLeft < leftHalfSize && takenFromRight < rightHalfSize){
			// check where is the next element, then put it in the right place

			if (getPointVal(rightHalf[takenFromRight], axis) < getPointVal(leftHalf[takenFromLeft], axis))
				array[left + takenFromLeft + takenFromRight] = rightHalf[takenFromRight++];
			else
				array[left + takenFromLeft + takenFromRight] = leftHalf[takenFromLeft++];
		}

		// add the remaining points from the half that we did not take all of its elements

		while (takenFromLeft < leftHalfSize)
			array[left + takenFromLeft + takenFromRight] = leftHalf[takenFromLeft++];

		while (takenFromRight < rightHalfSize)
			array[left + takenFromLeft + takenFromRight] = rightHalf[takenFromRight++];
	}

	/**
	 * Get the relevant value of a point.
	 * @param p the point.
	 * @param axis the axis.
	 * @return the axis value of p.
	 */
	private int getPointVal(Point p, Boolean axis){
		return axis ? p.getX() : p.getY();
	}
}