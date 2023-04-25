public class Test {
    public static void main(String[] args){
        DataStructure dt = new DataStructure();
        dt.addPoint(new Point(1,3));
        dt.addPoint(new Point(4,8));
        dt.addPoint(new Point(6,10));
        dt.addPoint(new Point(-5,9));
        dt.addPoint(new Point(8,2));
        dt.addPoint(new Point(0,1));
        System.out.println(dt.getxSorted());
        System.out.println(dt.getDensity());

        dt.narrowRange(4, 8,true);
        System.out.println(dt.getxSorted());
        System.out.println(dt.getDensity());
    }
}
