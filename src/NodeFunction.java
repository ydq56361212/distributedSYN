


import java.lang.reflect.Array;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.*;
//定义一些与节点相关的功能
public class NodeFunction {
    static Printer printer = new Printer();


    static Clock clock = new Clock(new ClockListener() {
        @Override
        public void onMessage() {
            printer.printList();
        }
    });

    public static double getDistance(Node a, Node b){
        double distance;
        double temp = Math.pow(a.getLocation().getX_coordinates() - b.getLocation().getX_coordinates(),2.0) + Math.pow(a.getLocation().getY_coordinates() - b.getLocation().getY_coordinates(),2.0);
        distance = Math.sqrt(temp);
        return distance;
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
        int time =(int) (MyMap.BASEtIME + (Math.random()*600+2000));
        return time;
    }
    public static double getRandomClockRatio(){
        double ratio = 1+Math.random()*0.000005;
        return ratio;
    }
    public static long getRandomClockOffset(){
        long offset =(long) (Math.random()*2000 - 1000);
        return offset;
    }
    public static void showtheNode(Node node){
        DecimalFormat df = new DecimalFormat("0.00");
        System.out.println("节点" + node.getID() + "  位置：（" + df.format(node.getLocation().getX_coordinates()) + "," + df.format(node.getLocation().getY_coordinates()) + ")  本地时钟：" + node.getLocalTime());
    }
    //构建邻居表并更新时钟状态与决策状态
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
            }
        }
        node.setNeighborMap(neighbor);
        updateState(node);
    }
    //更新节点时钟状态
    public static void updateState(Node node){
        if (node.getNeighborMap().isEmpty()){
            return;
        }
        Collection<Node> neighbors = node.getNeighborMap().values();
        Iterator<Node> neighborsIterator = neighbors.iterator();
        //使用TreeSet自动排序
        Set<Integer> stateSet = new TreeSet<>();
        while (neighborsIterator.hasNext()){
            Node tmpNode = neighborsIterator.next();
            stateSet.add(tmpNode.getClockState());
        }
        Iterator<Integer> stateIterator = stateSet.iterator();
        int minState = stateIterator.next();
        if (node.getClockState() == 0){
            return;
        }else if(minState == MyMap.NO_GPS){
            return;
        }else
            node.setClockState(minState + 1);
    }



    //构建时钟偏差表
    public static void buildOffsetMap(Node node){
        //从邻居表中获取offset
        String filename = "node" + node.getID() + ".txt";
        HashMap<Integer,Node> neighborMap = node.getNeighborMap();
        if(neighborMap.isEmpty()){
            return;
        }
        HashMap<Node,Long> offsetMap = node.getOffsetMap();
        Collection<Node> neighbors = neighborMap.values();
        Iterator<Node> neighborsIterator = neighbors.iterator();
        while (neighborsIterator.hasNext()){
            Node temp = neighborsIterator.next();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t1 = node.getLocalTime();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t2 = MyMap.getPropagationDelay() + temp.getLocalTime();
            for (int i = 0;i < MyMap.getPropagationDelay();i++){
                clock.send();
            }
//            MyMap.changeBASECLOCK();
//            FileOutput.fileoutput(node,filename);
            FileOutput.matlabFileoutput(node,filename);
            long t3 = temp.getLocalTime();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t4 = MyMap.getPropagationDelay() + node.getLocalTime();
            //MyMap.changeBASECLOCK();
            for (int i = 0;i < MyMap.getPropagationDelay();i++){
                clock.send();
            }
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
//            MyMap.SOLT_NUMBER += 2;
            long offset = ((t2 - t1) - (t4 - t3))/2;
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            //给offset增加一个很小的随机值，代表同步过程中的不稳定因素
            offset += MyMap.getRandomOffset();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            offsetMap.put(temp,offset);
            //MyMap.changeBASECLOCK();
//            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
        }
        node.setOffsetMap(offsetMap);
        //MyMap.changeBASECLOCK();
//        clock.send();
//        clock.send();
//        FileOutput.fileoutput(node,filename);
//        FileOutput.matlabFileoutput(node,filename);
    }
    //增加邻居选择功能后的构建偏差表，参数N表示本轮选择的邻居数量
    public static void buildOffsetMap(Node node,int N){
        if (N <= 0 ){
            return;
        }
        HashMap<Integer,Node> neighborMap = node.getNeighborMap();
        if(neighborMap.isEmpty()){
            return;
        }
        HashMap<Node,Long> offsetMap = node.getOffsetMap();
        Integer[] neighbors = neighborMap.keySet().toArray(new Integer[0]);
        Random randomnode = new Random();
        Set<Node> neighborsSet = new HashSet<>();
        while (neighborsSet.size() < N){
            Integer randindex = neighbors[randomnode.nextInt(neighbors.length)];
            Node tmpNode = neighborMap.get(randindex);
            neighborsSet.add(tmpNode);
        }
        Iterator<Node> neigthorsIterator = neighborsSet.iterator();
        while (neigthorsIterator.hasNext()){
            Node temp = neigthorsIterator.next();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t1 = node.getLocalTime();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t2 = MyMap.getPropagationDelay() + temp.getLocalTime();
            for (int i = 0;i < MyMap.getPropagationDelay();i++){
                clock.send();
            }
//            MyMap.changeBASECLOCK();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t3 = temp.getLocalTime();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t4 = MyMap.getPropagationDelay() + node.getLocalTime();
            //MyMap.changeBASECLOCK();
            for (int i = 0;i < MyMap.getPropagationDelay();i++){
                clock.send();
            }
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
//            MyMap.SOLT_NUMBER += 2;
            long offset = ((t2 - t1) - (t4 - t3))/2;
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            //给offset增加一个很小的随机值，代表同步过程中的不稳定因素
            offset += MyMap.getRandomOffset();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            offsetMap.put(temp,offset);
            //MyMap.changeBASECLOCK();
//            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
        }
        node.setOffsetMap(offsetMap);
        //MyMap.changeBASECLOCK();
//        clock.send();
    }
    //设定通信邻居数在邻居表中的比例
    public static void buildOffsetMap(Node node,double a){
        HashMap<Integer,Node> neighborMap = node.getNeighborMap();
        if(neighborMap.isEmpty()){
            return;
        }
        HashMap<Node,Long> offsetMap = node.getOffsetMap();
        int sync_number = (int) ( neighborMap.size() * a);
        if (sync_number <= 0 ){
            sync_number = 1;
        }
        Collection<Node> neighbors = neighborMap.values();
        Iterator<Node> neighborsIterator = neighbors.iterator();
        List<Node> tempNodeList = new ArrayList<>();
        while (neighborsIterator.hasNext()){
            Node temp = neighborsIterator.next();
            tempNodeList.add(temp);
        }
        Collections.shuffle(tempNodeList);
        for (int i = 0; i < sync_number;i++){
            Node temp = tempNodeList.get(i);
            MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t1 = node.getLocalTime();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t2 = MyMap.getPropagationDelay() + temp.getLocalTime();
            for (int j = 0;j < MyMap.getPropagationDelay();j++){
                clock.send();
            }
//            MyMap.changeBASECLOCK();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t3 = temp.getLocalTime();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            long t4 = MyMap.getPropagationDelay() + node.getLocalTime();
            //MyMap.changeBASECLOCK();
            for (int j = 0;j < MyMap.getPropagationDelay();j++){
                clock.send();
            }
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
//            MyMap.SOLT_NUMBER += 2;
            long offset = ((t2 - t1) - (t4 - t3))/2;
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            //给offset增加一个很小的随机值，代表同步过程中的不稳定因素
            offset += MyMap.getRandomOffset();
            //MyMap.changeBASECLOCK();
            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
            offsetMap.put(temp,offset);
            //MyMap.changeBASECLOCK();
//            clock.send();
//            FileOutput.fileoutput(node,filename);
//            FileOutput.matlabFileoutput(node,filename);
        }
        node.setOffsetMap(offsetMap);
        //MyMap.changeBASECLOCK();
//        clock.send();
    }
//------固定权值加权------------//
    public static final long SYNC_ERROR = -1000000000;
    //计算加权偏差值
//    public static long getWeightedOffset(Node node){
//        String filename = "node" + node.getID() + ".txt";
//        HashMap<Node,Long> offsetMap = node.getOffsetMap();
//        //如果偏差表为0，则认为错误返回一个不可能的偏差值
//        if(offsetMap.isEmpty())return SYNC_ERROR;
//        //如果自己本身有GPS则不调整时钟
//        if(node.getClockState() == 0)return 0;
//
//        //记录有gps的邻居个数
//        int gps_number = 0;
//        //记录有gps邻居的累计偏差
//        long gps_offset = 0;
//        //记录gps一跳邻居的个数
//        int neighgps_number = 0;
//        //记录gps一跳邻居的累计偏差
//        long neighgps_offset = 0;
//        //记录完全没有gps邻居的个数
//        int nogps_number = 0;
//        //记录完全没有gps邻居的累计偏差
//        long nogps_offset = 0;
//        //遍历偏差表
//        for(Map.Entry<Node,Long> offset : offsetMap.entrySet()){
//            Node tempnode = offset.getKey();
//            //MyMap.changeBASECLOCK();
//            clock.send();
////            FileOutput.fileoutput(node,filename);
////            FileOutput.matlabFileoutput(node,filename);
//            //取出邻居节点根据其状态进行判断，分别累计三种状态节点的状态和累计偏差
//            switch (tempnode.state){
//                case NO_GPS:
//                    nogps_number++;
//                    nogps_offset += offset.getValue();
//                    break;
//                case HAS_GPS:
//                    gps_number++;
//                    gps_offset += offset.getValue();
//                    break;
//                case NIGHBOR_GPS:
//                    neighgps_number++;
//                    neighgps_offset += offset.getValue();
//                    break;
//                default:
//                    break;
//            }
//            //MyMap.changeBASECLOCK();
//            clock.send();
////            FileOutput.fileoutput(node,filename);
////            FileOutput.matlabFileoutput(node,filename);
//
//        }
//        //如果邻居中有gps节点则向这些节点的算术平均值同步
//        if(gps_number != 0){
//            //MyMap.changeBASECLOCK();
//            clock.send();
////            FileOutput.fileoutput(node,filename);
////            FileOutput.matlabFileoutput(node,filename);
//            return gps_offset/(long)gps_number;
//        }
//        //如果邻居都是gps的一跳节点那么向邻居的算术平均值同步
//        else if(neighgps_number != 0 && nogps_number == 0){
//            //MyMap.changeBASECLOCK();
//            clock.send();
////            FileOutput.fileoutput(node,filename);
////            FileOutput.matlabFileoutput(node,filename);
//            return neighgps_offset/(long)neighgps_number;
//        }
//        //如果即有一跳节点又有非gps节点则两种偏差算术平均后在加权平均
//        else if(neighgps_number != 0 && nogps_number != 0){

//            //MyMap.changeBASECLOCK();
//            clock.send();
////            FileOutput.fileoutput(node,filename);
////            FileOutput.matlabFileoutput(node,filename);
//            return (long)(a+b);
//        }
//        else {
//            //MyMap.changeBASECLOCK();
//            clock.send();
////            FileOutput.fileoutput(node,filename);
////            FileOutput.matlabFileoutput(node,filename);
//            return nogps_offset/(long)nogps_number;
//        }
//    }
    //-------------动态权值分配
    //计算加权偏差值
    public static long getWeightedOffset(Node node){
        String filename = "node" + node.getID() + ".txt";
        HashMap<Node,Long> offsetMap = node.getOffsetMap();
        //如果偏差表为0，则认为错误返回一个不可能的偏差值
        if(offsetMap.isEmpty())return SYNC_ERROR;
        //如果自己本身有GPS则不调整时钟
        if(node.getClockState() == 0)return 0;
        //遍历偏差表记录每个ClockState下的offset
        Map<Integer,List<Long>> StateMap = new TreeMap<>();
        for(Map.Entry<Node,Long> offset : offsetMap.entrySet()){
            Node tempnode = offset.getKey();
            if (!StateMap.containsKey(tempnode.getClockState())){
                List<Long> offsetList = new ArrayList<>();
                offsetList.add(offset.getValue());
                StateMap.put(tempnode.getClockState(),offsetList);
            }else {
                List<Long> tmplist = StateMap.get(tempnode.getClockState());
                tmplist.add(offset.getValue());
                StateMap.put(tempnode.getClockState(),tmplist);
            }
        }
        //选择最小的状态分配最大权值
        Collection<Map.Entry<Integer,List<Long>>> tmpCollection = StateMap.entrySet();
        Iterator<Map.Entry<Integer,List<Long>>> tmpIterator = tmpCollection.iterator();
        int stateNumber = StateMap.size();
        Map.Entry<Integer,List<Long>> minStateEntry = tmpIterator.next();
        int minState = minStateEntry.getKey();
        if (minState == MyMap.NO_GPS){
            List<Long> offsetList = minStateEntry.getValue();
            int offsetNumber = offsetList.size();
            long sumOffset = 0;
            for (int i = 0; i < offsetNumber;i++){
                sumOffset += offsetList.get(i);
            }
            clock.send();
            return sumOffset/(long)offsetNumber;
        }
        double maxWeight =(double) 1- MyMap.WEIGHTING_1 * (double)minState;
        if(maxWeight < 0){
            maxWeight = 0;
        }
        double otherWeight = (1-maxWeight)/(stateNumber - 1);
        List<Long> maxWeightOffset = minStateEntry.getValue();
        int maxWeightOffseNumber = maxWeightOffset.size();
        long sumMaxWeightOffset = 0;
        double weithtOffset = 0;
        for (int i = 0;i < maxWeightOffseNumber;i++){
            sumMaxWeightOffset += maxWeightOffset.get(i);
        }
        weithtOffset += (sumMaxWeightOffset/maxWeightOffseNumber) * maxWeight;
        while (tmpIterator.hasNext()){
            Map.Entry<Integer,List<Long>> otherEntry = tmpIterator.next();
            List<Long> otherWeightOffsetList = otherEntry.getValue();
            int otherWeightNumber = otherWeightOffsetList.size();
            long otherWeightOffset = 0;
            for (int i = 0;i < otherWeightNumber;i++){
                otherWeightOffset += otherWeightOffsetList.get(i);
            }
            weithtOffset += (otherWeightOffset/otherWeightNumber) * otherWeight;
        }
        clock.send();
        return (long) weithtOffset;

    }
    //计算与邻居的时钟offset方差
    public static double caculatNeighborVariance(Node node){
        long sum = 0;
        double variance = 0.0;
        double average = 0.0;

        HashMap<Node,Long> tmpOffsetMap = node.getOffsetMap();
        Iterator<Long> offsetIterator1 = tmpOffsetMap.values().iterator();
        while (offsetIterator1.hasNext()){
            sum += offsetIterator1.next();
        }
        average = ((double)sum/(double) tmpOffsetMap.size());
        Iterator<Long> offsetIterator2 = tmpOffsetMap.values().iterator();
        while (offsetIterator2.hasNext()){
            variance += Math.pow(((double)offsetIterator2.next() - average),2);
        }
        return variance;
    }
    //按照当前策略获得下一个行动
    public static int getNextAction(Node node){
        int next_action;
        next_action = Egreedy.getNext_action(node.getState(),node.getQ_table());
        return next_action;
    }
    //根据当前选择的行动以及当前状态进行状态转移并更新offset方差
    public static void updataSelectionState(Node node){
        if (node.getSelectionState().getState_space() == null){
            return;
        }
        String[][] tmpStatePace = node.getSelectionState().getState_space();
        double tmpvariance = node.getNeighbor_variance();
        double currentvariance = NodeFunction.caculatNeighborVariance(node);
        if (currentvariance == 0){
            node.setState(tmpStatePace[node.getAction()][0]);
        }
        else if  (tmpvariance/currentvariance > 1.5){
            node.setState(tmpStatePace[node.getAction()][0]);

        }
        else if(tmpvariance/currentvariance < 0.7){
            node.setState(tmpStatePace[node.getAction()][1]);

        }
        else
            node.setState(tmpStatePace[node.getAction()][2]);
        node.setNeighbor_variance(currentvariance);
    }

    //构造一个菱形的网络
    public static void buildNet(List<Node> nodeList,int laynumber){
        int number = 0;
        for(int i = 0; i < laynumber;i++){
            Node node = new Node(number,0.0+i*15,0.0-i*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
            nodeList.add(node);
            number++;
            for (int j = 1;j < laynumber;j++){
                Node node1 = new Node(number,0.0+i*15+j*15,0.0-i*15+j*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
                nodeList.add(node1);
                number++;
            }
        }
        //System.out.println(number);
    }
    //构建线型拓扑
    public static void buildLineMap(List<Node> nodeList,int number){
        for(int i = 0;i < number;i++){
            Node node = new Node(i,0.0 + i*15,0.0 - i*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
            nodeList.add(node);
        }
    }
    //构建环形拓扑
    public static void buildCircMap(List<Node> nodeList,int number){
        double centerX = 50;//圆心坐标
        double centerY = 50;
        double radius = 40;//半径
        for (int i= 0; i<number; i++)
        {
            double x = centerX+ (double)(radius * Math.cos(Math.PI * 2 / number * i));
            double y = centerY+ (double)(radius * Math.sin(Math.PI * 2 / number * i));
            Node node = new Node(i,x,y,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
            nodeList.add(node);
        }
    }
    //构建邻居矩阵
    public static int[][] buildNeighborMatrix(List<Node> nodeList){
        int[][] graph = new int[nodeList.size()][nodeList.size()];
        for(int i = 0;i < graph.length;i++){
            Node node = nodeList.get(i);
            HashMap<Integer,Node> neighborMap = node.getNeighborMap();
            int row = node.getID();
            for (int j = 0; j < graph[0].length;j++){
                if(neighborMap.containsKey(j)){
                    graph[row][j] = 1;
                }else graph[row][j] = 0;
            }
        }
        return graph;
    }
    public static Graph buildNeighborGraph(List<Node> nodeList){
        Graph graph = new Graph(nodeList.size());
        for (Node node : nodeList){
            HashMap neigbermap = node.getNeighborMap();
            Collection<Node> neigbers = neigbermap.values();
            for(Node neighbernode : neigbers){
                graph.addEdge(node.getID(),neighbernode.getID());
            }
        }
        return graph;
    }

    public static double getOffsetVariance(List<Node> nodeList){
        long sum = 0;
        double variance = 0;
        double mean = 0;
        for (Node node : nodeList){
            sum += node.getLocalTime();
        }
        mean = (double) (sum/nodeList.size());
        for (Node node : nodeList){
            variance += Math.pow((double) (node.getLocalTime() - mean),2);
        }
        variance /= nodeList.size();
        return variance;
    }
    //构造一个异形的网络
    public static void buildNetNorules(List<Node> nodeList,int laynumber){
        int number = 0;
        for(int i = 0; i < laynumber;i++){
            Node node = new Node(number,0.0+i*15,0.0-i*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
            nodeList.add(node);
            number++;
            for (int j = 1;j < laynumber;j++){
                Node node1 = new Node(number,0.0+i*15+j*15,0.0-i*15+j*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
                nodeList.add(node1);
                number++;
            }
        }
        Node node2 = new Node(number,0.0+(laynumber-1)*15+(laynumber-1)*15 + 15,0.0-(laynumber-1)*15+(laynumber-1)*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
        nodeList.add(node2);
        number++;

        //System.out.println(number);
    }
    //构建哑铃型网络
    public static void buildDumbbellNet(List<Node> nodeList,int laynumber){
        int number = 0;
        for(int i = 0; i < laynumber;i++){
            Node node = new Node(number,0.0+i*15,0.0-i*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
            nodeList.add(node);
            number++;
            for (int j = 1;j < laynumber;j++){
                Node node1 = new Node(number,0.0+i*15+j*15,0.0-i*15+j*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
                nodeList.add(node1);
                number++;
            }
        }
        Node node2 = new Node(number,0.0+(laynumber-1)*15+(laynumber-1)*15 + 15,0.0-(laynumber-1)*15+(laynumber-1)*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
        nodeList.add(node2);
        number++;
        Node node3 = new Node(number,0.0+(laynumber)*15+(laynumber)*15 ,0.0-(laynumber-1)*15+(laynumber-1)*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
        nodeList.add(node3);
        number++;

        double location_2x = node3.getLocation().getX_coordinates() + 15;
        double location_2y = node3.getLocation().getY_coordinates();
        for(int i = 0; i < laynumber;i++){
            Node node = new Node(number,location_2x+i*15,location_2y-i*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
            nodeList.add(node);
            number++;
            for (int j = 1;j < laynumber;j++){
                Node node1 = new Node(number,location_2x+i*15+j*15,location_2y-i*15+j*15,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
                nodeList.add(node1);
                number++;
            }
        }
        //System.out.println(number);
    }
    //圆形内随机产生节点
    public static void buildRandomCircleNet(List<Node> nodeList,int number){
        Node centernode = new Node(0,35.0,35.0,getRandomClockRatio(),getRandomClockOffset());
        nodeList.add(centernode);
        Random random = new Random();
        for (int i = 0;i < number;i++){
            double x = 35 - Node.DISCOVERABLE + 2 * Node.DISCOVERABLE * random.nextDouble();
            double y = 35 - Node.DISCOVERABLE + 2 * Node.DISCOVERABLE * random.nextDouble();

            while(Math.sqrt(Math.pow(x - 35, 2) + Math.pow(y - 35, 2)) > Node.DISCOVERABLE){
                x = 35 - Node.DISCOVERABLE + 2 * Node.DISCOVERABLE * random.nextDouble();
                y = 35 - Node.DISCOVERABLE + 2 * Node.DISCOVERABLE * random.nextDouble();
            }
            Node node = new Node(i+1,x,y,getRandomClockRatio(),getRandomClockOffset());
            nodeList.add(node);
        }
    }

    public static void moveNodesinCircle(List<Node> nodeList,int turnNumber,int nodeNumber){
        Random random = new Random();
        double x_center = nodeList.get(0).getLocation().getX_coordinates();
        double y_cneter = nodeList.get(0).getLocation().getY_coordinates();

        for (int i = 1;i <nodeNumber;i++){
            double[][] location = tragComput(turnNumber,1,5,8,0,1,Math.random()*360,Math.random()*360,0,1,0,0,
                    0,1,1,(int)Math.random()*3 + 1);
            ArrayList<Double[]> locations = new ArrayList<>();
            for (int j = 0;j < turnNumber;j++){
                double x = location[j][0];
                double y = location[j][1];
                if (Math.sqrt(Math.pow(x - MyMap.CENTER_X,2) + Math.pow(y-MyMap.CENTER_Y,2)) <= Node.DISCOVERABLE){
                    Double[] track = new Double[]{x,y};
                    locations.add(track);
                }else continue;
            }
            Node node = new Node(i,locations.get(0)[0],locations.get(0)[1],getRandomClockRatio(),getRandomClockOffset());
            node.setMoveTrack(locations);
            nodeList.add(node);
        }
    }
    public static void refrashLocation(List<Node> nodeList,int round){
      for (Node node : nodeList){
         if (node.getMoveTrack().size() > round){
             node.setLocation(node.getMoveTrack().get(round)[0],node.getMoveTrack().get(round)[1]);
         }
      }

    }
    //创建动态列表
    public static void buildMoveNetwork(List<Node> nodeList,int number,int round){
        for (int i = 0;i < number;i++){
            final double startTime = System.currentTimeMillis();
            double map_Xlength = 865;
            double map_Ylength = 500;
            double startX = Math.random()*map_Xlength;
            double startY = Math.random()*map_Ylength;
            double speed = 500 ;
            double degree = Math.random() * 3.14;
            double map_boundary_X = 40;
            double map_boundary_Y = 40;
            double diameter = 0;
            ArrayList<Double[]> moveTrack = new ArrayList<>();
           // Calculate calculate = new Calculate(startX,startY,speed,degree,MyMap.BOUNDARY_X,MyMap.BOUNDARY_Y,map_boundary_X,map_boundary_Y,diameter);
            for (int j = 0;j < round;j++){
                double time = (System.currentTimeMillis() - startTime) / 1000;
                double X_helper = map_Xlength - map_boundary_X - diameter - map_boundary_X;
                double Y_helper = map_Ylength - map_boundary_Y - diameter - map_boundary_Y - diameter;
                double ballX = Math.abs((startX +  10000000 *map_Xlength +  time*speed * Math.cos(degree)) % (2 * X_helper) - X_helper) + map_boundary_X;
                double ballY = Math.abs((startY +  10000000 *map_Ylength +  time*speed * Math.sin(degree)) % (2 * Y_helper) - Y_helper) + diameter + map_boundary_Y;
                Double[] location = new Double[]{ballX,ballY};
                moveTrack.add(location);
                try {
                    Thread.sleep(40);//40ms,1s = 1000ms
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            Node node = new Node(i,moveTrack.get(0)[0],moveTrack.get(0)[1],getRandomClockRatio(),getRandomClockOffset());
            node.setMoveTrack(moveTrack);
            nodeList.add(node);
        }
    }
    //创建弹球轨迹
   /* public static void creatTrack(Node node,int round){
        double startX = Math.random()*MyMap.BOUNDARY_X;
        double startY = Math.random()*MyMap.BOUNDARY_Y;
        double speed = 500 + Math.random()*500;
        double degree = Math.random() * 3.14;
        double map_boundary_X = 20;
        double map_boundary_Y = 20;
        double diameter = 0;
        List<Double[]> moveTrack = new ArrayList<>();
        Calculate calculate = new Calculate(startX,startY,speed,degree,MyMap.BOUNDARY_X,MyMap.BOUNDARY_Y,map_boundary_X,map_boundary_Y,diameter);
        for (int i = 0;i < round ;i++){
            moveTrack.add(calculate.locationMethod());
        }
        node.setMoveTrack(moveTrack);
    }*/
    //===========轨迹产生函数===========
    /**
     *
     * @param time  飞行时间
     * @param slot  更新时间间隔
     * @param s0    初始速度
     * @param s_avg
     * @param s_mean
     * @param s_dev
     * @param d0
     * @param d_avg
     * @param d_mean
     * @param d_dev
     * @param p0
     * @param p_avg
     * @param p_mean
     * @param p_dev
     * @param alpha
     * @return
     */
    public static double[][] tragComput(double time, double slot,
                                        double s0, double s_avg, double s_mean, double s_dev,
                                        double d0, double d_avg, double d_mean, double d_dev,
                                        double p0, double p_avg, double p_mean, double p_dev,
                                        double alpha, int situation){
        int start_point_x = 0;
        int start_point_y = 0;
        int nums = (int)(time/slot);
        double[][] res = new double[nums][6];
        double[] v = vComput(s0, s_avg, s_mean,s_dev,d0,d_avg,d_mean,d_dev,p0,p_avg,p_mean,p_dev,alpha);

        //速度
        double vx = v[0] * Math.cos(v[1] * Math.PI / 180) * Math.cos(v[2] * Math.PI / 180);
        double vy = v[0] * Math.sin(v[1] * Math.PI / 180) * Math.cos(v[2] * Math.PI / 180);
        double vz = v[0] * Math.sin(v[1] * Math.PI / 180);
        switch (situation){
            case 1:
                start_point_x = 20;
                start_point_y = 20;
                break;
            case 2:
                start_point_x = 25;
                start_point_y = 20;
                break;
            case 3:
                start_point_x = 20;
                start_point_y = 25;
                break;
            case 4:
                start_point_x = 25;
                start_point_y = 25;
        }
        res[0][0] = vx * slot + start_point_x;
        res[0][1] = vy * slot + start_point_y;
        res[0][2] = vz * slot;
        res[0][3] = v[0];
        res[0][4] = v[1];
        res[0][5] = v[2];

        for (int i = 1; i < res.length; i++) {
            v = vComput(res[i-1][3], s_avg,s_mean,s_dev,res[i-1][4],d_avg,d_mean,d_dev,res[i-1][5],p_avg,p_mean,p_dev,alpha);
            vx = v[0] * Math.cos(v[1] * Math.PI / 180) * Math.cos(v[2] * Math.PI / 180);
            vy = v[0] * Math.sin(v[1] * Math.PI / 180) * Math.cos(v[2] * Math.PI / 180);
            vz = v[0] * Math.sin(v[1] * Math.PI / 180);
//            res[i][0] = vx * slot;
//            res[i][1] = vy * slot;
//            res[i][2] = vz * slot;
            res[i][0] = vx * slot + res[i-1][0];
            res[i][1] = vy * slot + res[i-1][1];
            res[i][2] = vz * slot + res[i-1][2];
            res[i][3] = v[0];
            res[i][4] = v[1];
            res[i][5] = v[2];
        }

        return res;
    }

    //生成速度: v:m/s
    public static double[] vComput(double s0, double s_avg, double s_mean, double s_dev,
                                   double d0, double d_avg, double d_mean, double d_dev,
                                   double p0, double p_avg, double p_mean, double p_dev,
                                   double alpha){

        double sn = alpha * s0 + (1 - alpha) * s_avg + Math.sqrt(1 - alpha * alpha) * gaussVar(s_mean,s_dev);
        double dn = alpha * d0 + (1 - alpha) * d_avg + Math.sqrt(1 - alpha * alpha) * gaussVar(d_mean,d_dev);
        double pn = alpha * p0 + (1 - alpha) * p_avg + Math.sqrt(1 - alpha * alpha) * gaussVar(p_mean,p_dev);

        double[] res = {sn, dn, pn};
        return res;
    }

    //高斯变量
    public static double gaussVar(double a, double b){
        Random r = new Random();
        double v = Math.sqrt(b) * r.nextGaussian() + a;
        return v;
    }
//=====================================================
}
