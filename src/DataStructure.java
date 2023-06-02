
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
		int currentVal;
		Container current = axis ? xSorted.head : ySorted.head;
		ContainerList inRange = new ContainerList(axis);

		while (current != null) {
			currentVal = current.getVal(axis);

			// no need to check the elements after max, they're all out of range
			if (currentVal > max)
				break;

			// min <= x <= max therefore in range, add to the array
			if (min <= currentVal)
				inRange.addLast(current);

			current = current.getNext();
		}

		return inRange.toArray();
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
		int currentVal;
		ContainerList inRange = new ContainerList(!axis);

		// looking into the opposite Axis data
		Container current = !axis ? xSorted.head : ySorted.head;

		while (current != null) {
			currentVal = current.getVal(axis);

			// value is is range
			if (currentVal >= min && currentVal <= max)
				inRange.addLast(current);

			current = current.getNext();
		}

		return inRange.toArray();
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
			if (sorted.head.getVal(axis) < min){
				other.remove(sorted.head.getInOtherList());
				sorted.remove(sorted.head);
			} else break;
		}

		// continue by removing all the elements greater than min
		// begin at the tail of the list, then move backwards
		while (sorted.tail != null){
			// if the value is smaller
			if (sorted.tail.getVal(axis) > max){
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

	/**
	 * The method returns the two nearest points in a strip centered in container with width width by the axis axis.
	 * @param container a middle point in the strip by the axis axis.
	 * @param width the width of the strip by axis axis.
	 * @param axis the axis that defines the strip.
	 * @return the nearest pair in the strip.
	 */
	@Override
	public Point[] nearestPairInStrip(Container container, double width,
									  Boolean axis) {
		// min value - so nearestPairInStripBounded would ignore this arguments,
		// max value - the same.
		return nearestPairInStripBounded(container, width, axis, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * @return the two closest points in the data structure.
	 */
	@Override
	public Point[] nearestPair() {
		Point[] pair = new Point[2];

		if (this.size < 2)
			return pair;

		if (this.size == 2){
			pair[0] = this.xSorted.head.getData();
			pair[1] = this.xSorted.tail.getData();
			return pair;
		}

		// there are at least 3 points in the segment

		boolean axis = getLargestAxis();
		Container[] allPoints = axis ? this.xSorted.toArrayOfContainers() : this.ySorted.toArrayOfContainers();

		return nearestPair(allPoints, axis, 0, this.size - 1);
	}

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

	/**
	 * Get a sorted array of all the points in a strip.
	 * @param container the middle of the strip, by the given axis.
	 * @param width the width of the strip.
	 * @param axis the axis to sort by and to determine the size of the strip.
	 * @return all the points in a strip.
	 */
	public Point[] pointsInStripBounded(Container container, double width, Boolean axis, int minVal, int maxVal) {
		// wasteful, should be organized as a containerList. rearranged with mergesort
		ContainerList strip = new ContainerList(axis);
		Container current = container;
		int containerVal = current.getVal(axis), currentVal = containerVal;
		int min = Math.max((int)(containerVal - width / 2), minVal);
		int max = Math.min((int)(containerVal + width / 2), maxVal);

		// while the current value is not less than the minimum value
		while ((double) currentVal >= min) {
			// put the point in the array, then proceed to check the prev. value
			strip.addFirst(current);
			current = current.getPrev();

			if(current == null)
				break;

			currentVal = current.getVal(axis);
		}

		// reset, now the other direction
		current = container.getNext();
		currentVal = containerVal;

		while ((double) currentVal <= max) {
			strip.addLast(current);
			current = current.getNext();

			if(current == null)
				break;

			currentVal = current.getVal(axis);
		}

		return strip.toArray();
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

	/** @return the Euclidean distance between two points, if gets null - the max. value of double.*/
	private double distance(Point p1, Point p2){
		if (p1 == null || p2 == null)
			return Double.MAX_VALUE;

		double dx = p1.getX() - p2.getX();
		double dy = p1.getY() - p2.getY();

		return Math.sqrt(dx*dx+dy*dy);
	}

	/**
	 * The method returns the two nearest points in a strip centered in container with width width by the axis axis.
	 * @param container a middle point in the strip by the axis axis.
	 * @param width the width of the strip by axis axis.
	 * @param axis the axis that defines the strip.
	 * @param minVal a specific minimal value of a relevant point in the strip.
	 * @param maxVal a specific maximal value of a relevant point in the strip.
	 * @return the nearest pair in the strip.
	 */
	public Point[] nearestPairInStripBounded(Container container, double width,
											 Boolean axis, int minVal, int maxVal) {
		Point[] strip = pointsInStripBounded(container, width, axis, minVal, maxVal);
		Point[] pair = new Point[2];
		double minDist = Double.MAX_VALUE, currentDist;

		if(strip.length*Math.log(strip.length)<size)
			// sort the points by the opposite axis
			mergeSort(strip, !axis);
		else{
			int min = Math.max((int)(container.getVal(axis) - (width/2)), minVal);
			int max = Math.min((int)(container.getVal(axis) + (width/2)), maxVal);

			strip = getPointsInRangeOppAxis(min,max,axis);
		}

		// for each point, calculate the distance to the next 5 points
		// find the minimum
		// TODO: explain why 5?

		// fot the first n-5 points, check 5 points
		for (int i = 0; i < strip.length - 5; i++)
			for (int j = 1; j < 6; j++){
				currentDist = distance(strip[i], strip[i + j]);
//				System.out.println("D(" + strip[i] + ", " + strip[i + j] + ") = " + currentDist);

				if (currentDist > minDist){
					minDist = distance(strip[i], strip[i + j]);
					pair[0] = strip[i];
					pair[1] = strip[i + j];
				}
			}

		// for the last 5 points check all the points after them
		for (int i = strip.length - 5; i < strip.length && i >= 0; i++)
			for (int j = strip.length - i - 1; j > 0; j--){
				currentDist = distance(strip[i], strip[i + j]);
//				System.out.println("D(" + strip[i] + ", " + strip[i + j] + ") = " + currentDist);

				if (currentDist < minDist){
					minDist = currentDist;
					pair[0] = strip[i];
					pair[1] = strip[i + j];
				}
			}

		return pair;
	}

	/**
	 * Calculate the nearest pair in a set of points.
	 * @param points the points to check.
	 * @param axis some axis X or Y.
	 * @param beginning the index of the first relevant point.
	 * @param end the index of the last relevant point.
	 * @return the nearest pair in [beginning, end].
	 */
	public Point[] nearestPair(Container[] points, Boolean axis, int beginning, int end){
		Point[] pair = new Point[2];
		Point[] firstHalfPair;
		Point[] secondHalfPair;
		Point[] stripPair;

		int segmentSize = end - beginning + 1;

		// note that when both even or both odd, the middle is the actual middle (same distance from both ends),
		// otherwise, eg. 2 and 9, the middle is 5, which is in the first half.
		int middle = (int)(Math.ceil((double) (end + beginning) / 2));

		double firstHalfMinDist, secondHalfMinDist, minDist;
		
		if (segmentSize < 2)
			return pair;

		if (segmentSize == 2){
			pair[0] = points[beginning].getData();
			pair[1] = points[end].getData();
			return pair;
		}

		// there are at least 3 points in the segment

		firstHalfPair = nearestPair(points, axis, beginning, middle - 1);
		secondHalfPair = nearestPair(points, axis, middle + 1, end);

		firstHalfMinDist = distance(firstHalfPair[0], firstHalfPair[1]);
		secondHalfMinDist = distance(secondHalfPair[0], secondHalfPair[1]);
		minDist = Math.min(firstHalfMinDist, secondHalfMinDist);

		stripPair = nearestPairInStripBounded(points[middle], 2 * minDist, axis,
				points[beginning].getVal(axis), points[end].getVal(axis));

		if (distance(stripPair[0], stripPair[1]) < minDist)
			return stripPair;
		else if (minDist == firstHalfMinDist)
			return firstHalfPair;
		else return secondHalfPair;
	}
}
