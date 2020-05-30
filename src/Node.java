import java.text.DecimalFormat;
import java.util.HashMap;

public class Node implements Runnable{
    private int ID;
//    private double x_coordinates;
//    private double y_coordinates;
    private HashMap<Integer,Node> neighborMap = new HashMap<>();
    private HashMap<Node,Integer> offsetMap = new HashMap<>();
    private int localTime;
    

    @Override
    public void run() {

    }

    class Location{
        private double x_coordinates;
        private double y_coordinates;

        public Location(double x_coordinates, double y_coordinates) {
            this.x_coordinates = x_coordinates;
            this.y_coordinates = y_coordinates;
        }

        public double getX_coordinates() {
            return x_coordinates;
        }

        public void setX_coordinates(double x_coordinates) {
            this.x_coordinates = x_coordinates;
        }

        public double getY_coordinates() {
            return y_coordinates;
        }

        public void setY_coordinates(double y_coordinates) {
            this.y_coordinates = y_coordinates;
        }

    }
    private Location location = new Location(0,0);


    public ClockState state = ClockState.NO_GPS;
    public static final double DISCOVERABLE = 30.0;



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Node(int ID, double x_coordinates, double y_coordinates, int localTime) {
        this.ID = ID;
//        this.x_coordinates = x_coordinates;
//        this.y_coordinates = y_coordinates;
        this.location.x_coordinates = x_coordinates;
        this.location.y_coordinates = y_coordinates;
        this.localTime = localTime;
    }

    public Node(int ID, int localTime, ClockState state) {
        this.ID = ID;
        this.localTime = localTime;
        this.state = state;
    }

    public Node(int ID, int localTime) {
        this.ID = ID;
        this.localTime = localTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(double x,double y) {
       location.x_coordinates = x;
       location.y_coordinates = y;
    }
    //    public double getX_coordinates() {
//        return x_coordinates;
//    }
//
//    public void setX_coordinates(double x_coordinates) {
//        this.x_coordinates = x_coordinates;
//    }
//
//    public double getY_coordinates() {
//        return y_coordinates;
//    }
//
//    public void setY_coordinates(double y_coordinates) {
//        this.y_coordinates = y_coordinates;
//    }

    public HashMap<Integer, Node> getNeighborMap() {
        return neighborMap;
    }

    public void setNeighborMap(HashMap<Integer, Node> neighborMap) {
        this.neighborMap = neighborMap;
    }

    public HashMap<Node, Integer> getOffsetMap() {
        return offsetMap;
    }

    public void setOffsetMap(HashMap<Node, Integer> offsetMap) {
        this.offsetMap = offsetMap;
    }

    public int getLocalTime() {
        return localTime;
    }

    public void setLocalTime(int localTime) {
        this.localTime = localTime;
    }

    public ClockState getState() {
        return state;
    }

    public void setState(ClockState state) {
        this.state = state;
    }

    //与邻居进行加权同步
    public void synchronisation(){
        NodeFunction.buildOffsetMap(this);
        int offset = NodeFunction.getWeightedOffset(this);
        if(offset == NodeFunction.SYNC_ERROR){
            System.out.println("节点" + ID + "未入网");
        }else localTime += offset;
    }
    //得到一个随机的位置
    public void getRandomLocation(){
        location.x_coordinates = NodeFunction.getRandomX_Coordinates();
        location.y_coordinates = NodeFunction.getRandomX_Coordinates();
    }
    public String toString(){
        String nodeinfo;
        DecimalFormat df = new DecimalFormat("0.00");
        nodeinfo = "节点" + ID + "\t位置：" + "(" + df.format(location.x_coordinates) + "," + df.format(location.y_coordinates) + ")\t" + "本地时钟：" + localTime + "\t" + state.getState_name();
        return nodeinfo;
    }
    public void outputtoFile(){
        String filename = "node" + ID + ".txt";
        FileOutput.fileoutput(this,filename);
    }
}
