import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.*;
//定义一些与节点相关的功能
public class NodeFunction {
    public static double getDistance(Node a,Node b){
        double distance;
        double temp = Math.pow(a.getLocation().getX_coordinates() - b.getLocation().getX_coordinates(),2.0) + Math.pow(a.getLocation().getY_coordinates() - b.getLocation().getY_coordinates(),2.0);
        distance = Math.sqrt(temp);
        return distance;
    }

    public static int getTimeOffset(Node a,Node b){
        int offset = a.getLocalTime() - b.getLocalTime();
        return offset;
    }
    public static double getRandomX_Coordinates(){
        double random_x = Math.random()* MyMap.BOUNDARY_X;
        return random_x;
    }
    public static double getRandomY_Coordinates(){
        double random_y = Math.random()* MyMap.BOUNDARY_Y;
        return random_y;
    }
    public static int getRandomLocalClock(){
        int time =(int) (MyMap.BASEtIME + (Math.random()*200+100));
        return time;
    }
    public static void showtheNode(Node node){
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("节点" + node.getID() + "  位置：（" + df.format(node.getLocation().getX_coordinates()) + "," + df.format(node.getLocation().getY_coordinates()) + ")  本地时钟：" + node.getLocalTime());
    }
    //构建邻居表
    public static void buildneighborMap(Node node,List<Node> nodeList) {
        HashMap<Integer, Node> neighbor = node.getNeighborMap();
        Node[] tmpnodes = nodeList.toArray(new Node[nodeList.size()]);
        for(int i = 0; i < tmpnodes.length;i++){
            //忽略自己本身
            if (node == tmpnodes[i]) continue;
            //忽略已经存在表中的邻居
             if((neighbor.containsValue(tmpnodes[i]))) continue;
            double distance = NodeFunction.getDistance(node, tmpnodes[i]);
            //判断邻居的标准根据波束能力修改
            if (distance < Node.DISCOVERABLE) {
                neighbor.put(tmpnodes[i].getID(), tmpnodes[i]);
                if(tmpnodes[i].state == ClockState.HAS_GPS){
                    node.state = ClockState.NIGHBOR_GPS;
                }
            }
        }
        node.setNeighborMap(neighbor);
    }

    //构建时钟偏差表
    public static void buildOffsetMap(Node node){
        //从邻居表中获取offset
        HashMap<Integer,Node> neighborMap = node.getNeighborMap();
        if(neighborMap.isEmpty()){
            return;
        }
        HashMap<Node,Integer> offsetMap = node.getOffsetMap();
        Collection<Node> neighbors = neighborMap.values();
        Iterator<Node> neighborsIterator = neighbors.iterator();
        while (neighborsIterator.hasNext()){
            Node temp = neighborsIterator.next();
            int t1 = node.getLocalTime();
            int t2 = MyMap.getPropagationDelay() + temp.getLocalTime();
            int t3 = temp.getLocalTime();
            int t4 = MyMap.getPropagationDelay() + node.getLocalTime();
            MyMap.SOLT_NUMBER += 2;
            int offset = ((t2 - t1) - (t4 - t3))/2;
            offsetMap.put(temp,offset);
        }
        node.setOffsetMap(offsetMap);
    }
    public static final int SYNC_ERROR = -1000000000;
    //计算加权偏差值
    public static int getWeightedOffset(Node node){
        HashMap<Node,Integer> offsetMap = node.getOffsetMap();

        //如果偏差表为0，则认为错误返回一个不可能的偏差值
        if(offsetMap.isEmpty())return SYNC_ERROR;
        //如果自己本身有GPS则不调整时钟
        if(node.state == ClockState.HAS_GPS)return 0;
        //记录有gps的邻居个数
        int gps_number = 0;
        //记录有gps邻居的累计偏差
        int gps_offset = 0;
        //记录gps一跳邻居的个数
        int neighgps_number = 0;
        //记录gps一跳邻居的累计偏差
        int neighgps_offset = 0;
        //记录完全没有gps邻居的个数
        int nogps_number = 0;
        //记录完全没有gps邻居的累计偏差
        int nogps_offset = 0;
        //遍历偏差表
        for(Map.Entry<Node,Integer> offset : offsetMap.entrySet()){
            Node tempnode = offset.getKey();
            //取出邻居节点根据其状态进行判断，分别累计三种状态节点的状态和累计偏差
            switch (tempnode.state){
                case NO_GPS:
                    nogps_number++;
                    nogps_offset += offset.getValue();
                    break;
                case HAS_GPS:
                    gps_number++;
                    gps_offset += offset.getValue();
                    break;
                case NIGHBOR_GPS:
                    neighgps_number++;
                    neighgps_offset += offset.getValue();
                    break;
                default:
                    break;
            }
        }
        //如果邻居中有gps节点则向这些节点的算术平均值同步
        if(gps_number != 0){
            return gps_offset/gps_number;
        }
        //如果邻居都是gps的一跳节点那么向邻居的算术平均值同步
        else if(neighgps_number != 0 && nogps_number == 0){
            return neighgps_offset/neighgps_number;
        }
        //如果即有一跳节点又有非gps节点则两种偏差算术平均后在加权平均
        else if(neighgps_number != 0 && nogps_number != 0){
            double a = (ClockState.NIGHBOR_GPS.getWeight()*(neighgps_offset/neighgps_number));
            double b = (ClockState.NO_GPS.getWeight()*(nogps_offset/nogps_number));
            return (int)(a+b);
        }
        else return nogps_offset/nogps_number;
    }
    //构造一个菱形的网络
    public static void buildNet(List<Node> nodeList,int laynumber){
        int number = 0;
        for(int i = 0; i < laynumber;i++){
            Node node = new Node(number,0.0+i*15,0.0-i*15,NodeFunction.getRandomLocalClock());
            nodeList.add(node);
            number++;
            for (int j = 1;j < laynumber;j++){
                Node node1 = new Node(number,0.0+i*15+j*15,0.0-i*15+j*15,NodeFunction.getRandomLocalClock());
                nodeList.add(node1);
                number++;
            }
        }
        System.out.println(number);
    }




}
