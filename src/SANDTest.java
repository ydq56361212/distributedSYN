import java.text.DecimalFormat;
import java.util.*;

public class SANDTest {
    public static void main(String[] args) {
        List<Node> nodeList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入希望的网络拓扑标号：1、16个节点随机位置  2、16个节点组成菱形网络  3、10个节点线型网络  4、10个节点环形网络");
        int topology = scanner.nextInt();
        switch (topology){
            case 1:
                //构建随机拓扑
                for(int i = 0; i < MyMap.NODESNUMBER;i++){
                    nodeList.add(new Node(i, NodeFunction.getRandomX_Coordinates(), NodeFunction.getRandomY_Coordinates(),NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset()));
                }
                break;
            case 2:
                //菱形网络构建
                NodeFunction.buildNet(nodeList,3);
                break;
            case 3:
                //线性网络构建
                NodeFunction.buildLineMap(nodeList,10);
                break;
            case 4:
                //环形网络构建
                NodeFunction.buildCircMap(nodeList,10);
                break;
            default:
                break;
        }
        //启动所有node本地时钟
        for(int i = 0;i < nodeList.size();i++){
            Thread thread = new Thread(nodeList.get(i));
            thread.start();
        }
        SANDFunction.printer.setNodeList(nodeList);
        SANDFunction.syncprinter.setNodeList(nodeList);
        //开始同步前删除之前的文件，因为拓扑大小不同会影响文件的个数，影响图像质量
        SANDFileOutput.deleteFile();
        SANDFileOutput.deleteMatlabFile();
        //测试SAND的邻居发现
        DecimalFormat df = new DecimalFormat("0.00");
        double varience = NodeFunction.getOffsetVariance(nodeList);
        System.out.println("offset方差值: " + df.format(varience));
        //初始化token列表
        HashMap<Integer,Boolean> token = SANDFunction.getInitialToken(nodeList);

        nodeList.get(0).setClockOffset(0);
        SANDFunction.buildNeighbormap(nodeList,nodeList.get(0),token);
        varience = NodeFunction.getOffsetVariance(nodeList);
        System.out.println("offset方差值: " + df.format(varience));
        //打印邻居表
        for(Node node : nodeList){
            HashMap<Integer,Node> neighbor = node.getNeighborMap();
            for(Map.Entry<Integer,Node> entry : neighbor.entrySet()) {
                System.out.println("节点" + node.getID() + "的邻居有： "+entry.getValue().getID());
            }
        }
        //画出拓扑图
        new GrapNode(nodeList).setVisible(true);
        //结束node线程
        for(int i = 0;i < nodeList.size();i++){
            nodeList.get(i).exit = true;
        }
    }
}
