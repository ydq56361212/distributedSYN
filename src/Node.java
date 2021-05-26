

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Node implements Runnable{
    private int ID;
//    private double x_coordinates;
//    private double y_coordinates;
    private HashMap<Integer,Node> neighborMap = new HashMap<>();
    private HashMap<Node,Long> offsetMap = new HashMap<>();
    private volatile long localTime;
    //时钟频率
    private double clockRatio;
    //时钟偏差
    private long clockOffset;
    //下一次同步邻居数选择
    private int nextNumber;
    //时钟状态参数
    private int clockState;
    //运动轨迹
    private List<Double[]> moveTrack = new ArrayList<>();
    //选择同步的状态
    private SelectionState selectionState = new SelectionState();
    //记录与邻居的时钟方差
    private double neighbor_variance = Double.MAX_VALUE;
    //创建一个初试策略
    private InitialPolicy initialPolicy = new InitialPolicy();
    //节点的状态与行动
    private String state;
    private int action;
    private int next_action = 0;
    //测试sarsa
    private double test_Sarsa;

    public double getTest_Sarsa() {
        return test_Sarsa;
    }

    public void setTest_Sarsa(double test_Sarsa) {
        this.test_Sarsa = test_Sarsa;
    }

    //本次的回报
    private int reword = 0;

    public double[][][] getQ_table() {
        return Q_table;
    }

    public void setQ_table(double[][][] q_table) {
        Q_table = q_table;
    }

    //Q-table
    private double[][][] Q_table;
    //测试Greedy
    private int test_greedy;
    public Egreedy egreedy = new Egreedy();

    public int getReword() {
        return reword;
    }

    public void setReword(int reword) {
        this.reword = reword;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public InitialPolicy getInitialPolicy() {
        return initialPolicy;
    }

    public void setInitialPolicy(InitialPolicy initialPolicy) {
        this.initialPolicy = initialPolicy;
    }

    public double getNeighbor_variance() {
        return neighbor_variance;
    }

    public void setNeighbor_variance(double neighbor_variance) {
        this.neighbor_variance = neighbor_variance;
    }

    public SelectionState getSelectionState() {
        return selectionState;
    }

    public void setSelectionState(SelectionState selectionState) {
        this.selectionState = selectionState;
    }

    public List<Double[]> getMoveTrack() {
        return moveTrack;
    }

    public void setMoveTrack(List<Double[]> moveTrack) {
        this.moveTrack = moveTrack;
    }

    //=============================================//
    public volatile boolean exit = false;

    public double getClockRatio() {
        return clockRatio;
    }

    public void setClockRatio(double clockRatio) {
        this.clockRatio = clockRatio;
    }

    public long getClockOffset() {
        return clockOffset;
    }

    public void setClockOffset(long ClockOffset) {
        this.clockOffset = ClockOffset;
    }
    //每一个节点一个线程维护自己的时钟，
    @Override
    public void run() {
        while (!exit){
//            this.localTime = ((long)(clockRatio*System.currentTimeMillis()) + clockOffset);
            this.localTime = ((long)clockRatio*Clock.BASECLOCK + clockOffset);
        }
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



    public static final double DISCOVERABLE = 50.0;



    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

//    public Node(int ID, double x_coordinates, double y_coordinates, int localTime) {
//        this.ID = ID;
////        this.x_coordinates = x_coordinates;
////        this.y_coordinates = y_coordinates;
//        this.location.x_coordinates = x_coordinates;
//        this.location.y_coordinates = y_coordinates;
//        this.localTime = localTime;
//    }
    public Node(int ID, double x_coordinates, double y_coordinates, double clockRatio,long clockOffset) {
        this.ID = ID;
//        this.x_coordinates = x_coordinates;
//        this.y_coordinates = y_coordinates;
        this.location.x_coordinates = x_coordinates;
        this.location.y_coordinates = y_coordinates;
        this.clockOffset = clockOffset;
        this.clockRatio = clockRatio;
        this.localTime = ((long)(clockRatio*System.currentTimeMillis()) + clockOffset);
//        this.nextNumber = 2;
        this.clockState = MyMap.NO_GPS;
    }



    public Node(int ID, long localTime) {
        this.ID = ID;
        this.localTime = localTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(double x,double y) {
       this.location.x_coordinates = x;
       this.location.y_coordinates = y;
    }

    public int getNextNumber() {
        return nextNumber;
    }

    public void setNextNumber(int nextNumber) {
        this.nextNumber = nextNumber;
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

    public HashMap<Node, Long> getOffsetMap() {
        return offsetMap;
    }

    public void setOffsetMap(HashMap<Node, Long> offsetMap) {
        this.offsetMap = offsetMap;
    }

    public long getLocalTime() {
        return localTime;
    }

    public void setLocalTime(long localTime) {
        this.localTime = localTime;
    }

    public int getClockState() {
        return clockState;
    }

    public int getTest_greedy() {
        return test_greedy;
    }

    public void setClockState(int clockState) {
        this.clockState = clockState;
    }





    //与邻居进行加权同步
    public void synchronisation(){
        NodeFunction.updateState(this);
        if (next_action == 0){
            this.setAction(NodeFunction.getNextAction(this));
        }else
            this.setAction(next_action);
        String tmp_state = this.getState();
//        this.test_greedy = egreedy.getNext_action(this.state,this.Q_table);
//        NodeFunction.buildOffsetMap(this,action);
        NodeFunction.buildOffsetMap(this,MyMap.CHOINCE);
        this.setReword(this.selectionState.getReword(state));
        NodeFunction.updataSelectionState(this);
        next_action = Sarsa.updateQtable(tmp_state,this.state,this.action,this.getQ_table());
        test_Sarsa = Sarsa.getQvale(tmp_state,action,this.getQ_table());
        long offset = NodeFunction.getWeightedOffset(this);
        if(offset == NodeFunction.SYNC_ERROR){
            System.out.println("节点" + ID + "未入网");
        }else {
            localTime += offset;
            clockOffset += offset;
        }
    }
    public String toString(){
        String nodeinfo;
        DecimalFormat df = new DecimalFormat("0.00");
        nodeinfo = "节点" + ID + "\t位置：" + "(" + df.format(location.x_coordinates) + "," + df.format(location.y_coordinates) + ")\t" + "本地时钟：" + localTime + "\t" + "时钟状态：" + clockState;
        return nodeinfo;
    }
    public void outputtoFile(){
        String filename = "node" + ID + ".txt";
        FileOutput.fileoutput(this,filename);
    }
    public void move(int round){
        if (moveTrack.size() > round){
            this.location.x_coordinates = this.moveTrack.get(round)[0];
            this.location.y_coordinates = this.moveTrack.get(round)[1];
        }
    }

    public void initial_state(){
        int neighbor_number = this.getNeighborMap().size();
        if (neighbor_number == 0){
            return;
        }
        String [][] state_space = new String[neighbor_number + 1][3];
        int[] action_space = new int[neighbor_number + 1];
        this.Q_table = new double[neighbor_number + 1][3][neighbor_number + 1];

        for (int i = 1;i < neighbor_number + 1;i++){
            action_space[i] = i;
            state_space[i][0] = "S_" + i + "_down";
            state_space[i][1] = "S_" + i + "_up";
            state_space[i][2] = "S_" + i + "_equal";
        }
        this.selectionState.setAction_space(action_space);
        this.selectionState.setState_space(state_space);
        this.setState(state_space[1][0]);

    }
}
