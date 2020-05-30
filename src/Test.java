import java.util.*;

public class Test {

    public static void main(String[] args) {
        List<Node> nodeList = new ArrayList<>();
//      测试初始化
//        for(int i = 0; i < MyMap.NODESNUMBER;i++){
//            nodeList.add(new Node(i, NodeFunction.getRandomX_Coordinates(), NodeFunction.getRandomY_Coordinates(), NodeFunction.getRandomLocalClock())) ;
//        }
        //网络构建
        NodeFunction.buildNet(nodeList,4);
//        nodeList.get(6).state = ClockState.HAS_GPS;
//        nodeList.get(9).state = ClockState.HAS_GPS;
//        nodes[0].state = ClockState.HAS_GPS;
//        nodes[1].state = ClockState.HAS_GPS;
//      测试文件输出
//      for(Node node : nodeList){
//            String filename = "node" + node.getID() + ".txt";
//            NodeFunction.showtheNode(node);
//            //FileOutput.clearFile(node,filename);
//            FileOutput.fileoutput(node,filename);
//        }
//

        //建立邻居表
        for (int i = 0;i<nodeList.size();i++){
            NodeFunction.buildneighborMap(nodeList.get(i),nodeList);
        }
        //打印邻居表
        for(Node node : nodeList){
            HashMap<Integer,Node> neighbor = node.getNeighborMap();
            for(Map.Entry<Integer,Node> entry : neighbor.entrySet()) {
                System.out.println("节点" + node.getID() + "的邻居有： "+entry.getValue().getID());
            }
        }
        //画出拓扑图
        new GrapNode(nodeList).setVisible(true);
        //测试时间同步,功能正确但还有细节要修改
        //FileOutput.clearFile(node,filename);
        //开始同步前清理输出文件
        for(Node node : nodeList){
            String filename = "node" + node.getID() + ".txt";
           //NodeFunction.showtheNode(node);
            FileOutput.clearFile(node,filename);
            FileOutput.matlabClearFile(node,filename);
            //FileOutput.fileoutput(node,filename);
        }
        //开始同步
        //打乱队列
       // List<Node> tempList = nodeList;
        Collections.shuffle(nodeList);
        while (MyMap.SOLT_NUMBER < 5000){
            for(Node node : nodeList){
                String filename = "node" + node.getID() + ".txt";
                node.synchronisation();
                FileOutput.fileoutput(node,filename);
                FileOutput.matlabFileoutput(node,filename);
            }
        }


    }


}
