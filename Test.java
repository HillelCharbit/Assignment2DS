import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Test {
    public static void main(String[] args) {
        DataStructure dt = new DataStructure();
        addPoints(dt, getTestPoints());
        System.out.println(dt.getxSorted());
        Point[] pair = dt.nearestPair();
        printArray(pair);
    }

    public static Point[] getTestPoints(){
        Point[] arr = {
                new Point(-15, 7), new Point(5, -1), new Point(3, 6),
                new Point(6, 4), new Point(0, 2), new Point(1, 5),
                new Point(4, 3), new Point(-2, -4), new Point(9, 1)
        };

        return arr;
    }

    public static void addRandomTestPoints(DataStructure dt, int min, int max, int num){
        Random rnd = new Random();
        int x, y;
        Set<Integer> xs = new HashSet<>(), ys = new HashSet<>();

        // first
        x = min + rnd.nextInt(max - min + 1);
        y = min + rnd.nextInt(max - min + 1);

        if (num > 0){
            dt.addPoint(new Point(x, y));
            xs.add(x);
            ys.add(y);
        }

        for (int i = 1; i < num; i++){
            do {
                x = min + rnd.nextInt(max - min + 1);
            } while (xs.contains(x));

            do {
                y = min + rnd.nextInt(max - min + 1);
            } while (ys.contains(y));

            dt.addPoint(new Point(x, y));
            xs.add(x);
            ys.add(y);
        }
    }

    public static void addPoints(DataStructure dt, Point[] points){
        if (dt == null || points == null)
            throw new NullPointerException();

        for (Point p : points) {
            dt.addPoint(p);
//            System.out.println("Adding " + p);
//            System.out.println("\tBy X: " + dt.getxSorted());
//            System.out.println("\tBy Y: " + dt.getySorted());
        }
    }

    public static void printArray(Object[] arr){
        System.out.print("{");

        if (arr != null && arr.length != 0) {
            System.out.print(arr[0]);

            for (int i = 1; i < arr.length; i++)
                System.out.print(", " + arr[i]);
        }

        System.out.println("}");
    }
}
