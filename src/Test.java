import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.FileHandler;

public class Test {


    public static void main(String[] args) {
        //25个节点需要20轮以上
        int roundNumber = 20;
        int round = 0;
        //是否开启训练
        boolean training = true;
        //是否开启动态
        boolean moveable = true;
        List<Node> nodeList = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入希望的网络拓扑标号：" + "\n" + "1、16个节点随机位置  2、16个节点组成菱形网络  3、10个节点线型网络  " +  "\n" +  "4、10个节点环形网络 5、异形网络 6、哑铃型网络 " + "\n" + "7、动态节点");
        int topology = scanner.nextInt();
        switch (topology) {
            case 1:
                //构建随机拓扑
                for (int i = 0; i < MyMap.NODESNUMBER; i++) {
                    nodeList.add(new Node(i, NodeFunction.getRandomX_Coordinates(), NodeFunction.getRandomY_Coordinates(), NodeFunction.getRandomClockRatio(), NodeFunction.getRandomClockOffset()));
                }
                break;
            case 2:
                //菱形网络构建
                NodeFunction.buildNet(nodeList, 4);
                break;
            case 3:
                //线性网络构建
                NodeFunction.buildLineMap(nodeList, 16);
                break;
            case 4:
                //环形网络构建
                NodeFunction.buildCircMap(nodeList, 16);
                break;
            case 5:
                //异性网络构建
                NodeFunction.buildNetNorules(nodeList,3);
                break;
            case 6:
                //哑铃型网络构建
                NodeFunction.buildDumbbellNet(nodeList,3);
                break;
            case 7:
                //弹球轨迹网络构建
                NodeFunction.buildMoveNetwork(nodeList,8,roundNumber);
                break;
            default:
                break;
        }

        //添加GPS节点

//        nodeList.get(1).setClockState(0);
//        nodeList.get(1).setClockOffset(0);

//        nodeList.get(5).setClockState(0);
//        nodeList.get(5).setClockOffset(0);
//        nodeList.get(5).setClockState(0);
//        nodeList.get(5).setClockOffset(0);
        //启动所有node本地时钟
        for (int i = 0; i < nodeList.size(); i++) {
            Thread thread = new Thread(nodeList.get(i));
            thread.start();
        }


        //将节点列表传给打印控制器
        NodeFunction.printer.setNodeList(nodeList);


        //将节点列表传给SADN打印控制器
//        SANDFunction.printer.setNodeList(nodeList);
//        SANDFunction.syncprinter.setNodeList(nodeList);

        //开始同步前删除之前的文件，因为拓扑大小不同会影响文件的个数，影响图像质量
            FileOutput.deleteFile();
            FileOutput.deleteMatlabFile();
            FileOutput.deletevarianceFile();
            FileOutput.deleteTimeFile();
            FileOutput.deleteTrackFile();


//        SANDFileOutput.deleteFile();
//        SANDFileOutput.deleteMatlabFile();
//        //测试SAND的邻居发现
        DecimalFormat df = new DecimalFormat("0.00");
        double varience = NodeFunction.getOffsetVariance(nodeList);
        System.out.println("offset方差值: " + df.format(varience));
        //初始化token列表
//        HashMap<Integer, Boolean> token = SANDFunction.getInitialToken(nodeList);
//        for(Node node : nodeList){
//            String filename = "node" + node.getID() + ".txt";
           //NodeFunction.showtheNode(node);
//            SANDFileOutput.clearFile(node,filename);
//            SANDFileOutput.matlabClearFile(node,filename);
            //FileOutput.fileoutput(node,filename);
//              }
//        nodeList.get(0).setClockOffset(0);
//        SANDFunction.buildNeighbormap(nodeList, nodeList.get(0), token);
//        varience = NodeFunction.getOffsetVariance(nodeList);
//        System.out.println("offset方差值: " + df.format(varience));

        //建立邻居表并初始化决策状态表
        for (int i = 0;i<nodeList.size();i++){
            NodeFunction.buildneighborMap(nodeList.get(i),nodeList);
            nodeList.get(i).initial_state();

        }
//        //打印邻居表
        for (Node node : nodeList) {
            HashMap<Integer, Node> neighbor = node.getNeighborMap();
            for (Map.Entry<Integer, Node> entry : neighbor.entrySet()) {
                System.out.println("节点" + node.getID() + "的邻居有： " + entry.getValue().getID());
            }
        }
        if (moveable){
            for (int i = 0;i<nodeList.size();i++){
                String filename = "node" + i + ".txt";
                FileOutput.locationFile(nodeList.get(i),roundNumber,filename);
            }
        }

//        //画出拓扑图
//        new GrapNode(nodeList).setVisible(true);

        //开始同步前删除之前的文件，因为拓扑大小不同会影响文件的个数，影响图像质量


        //构建邻居矩阵
//        Graph neighborGraph = NodeFunction.buildNeighborGraph(nodeList);
//        neighborGraph.connectedComponents();

        System.out.println("开始同步");
        varience = NodeFunction.getOffsetVariance(nodeList);
        System.out.println("offset方差值: " + df.format(varience));
        //开始同步
        List<Node> tempList = nodeList;
        //移动开关
//        moveable = true;
        //如果不需要训练可以直接读取qtable
      /*  if (!training){
            for (Node node:nodeList){
                String filename ="node" +  node.getID() + ".txt";
                FileOutput.loadQtable(node,filename);
            }
        }*/

        while (round < roundNumber) {
            int start = Clock.BASECLOCK;
            if (!moveable) {
                //打乱队列
                Collections.shuffle(nodeList);
                System.out.println("当前队列的顺序为：");
                for (Node node : nodeList) {
                    System.out.print(node.getID() + "\t");
                }
                System.out.println();
                for (Node node : nodeList) {
                    node.synchronisation();
                    //测试下一动作功能与下一状态
//                    System.out.println("节点" + node.getID() + "动作：" + node.getAction() + "下一状态" + node.getState());
                    //测试与邻居offset方差
//                    System.out.println("邻居offset方差：" + df.format(node.getNeighbor_variance()));
                    //测试回报函数
//                    System.out.println("回报" + node.getReword());
                    //测试Egreedy
//                    System.out.println("Egreedy_action:" + node.getTest_greedy() );
                    //测试Sarsa
//                    System.out.println("Q_value:" + node.getTest_Sarsa());
                }
                int end = Clock.BASECLOCK;
                int time = end - start;
                FileOutput.endtimefileoutput(round,end);
                FileOutput.timefileoutput(round,time);
                System.out.println("本轮时间：" + (end - start));
                varience = NodeFunction.getOffsetVariance(nodeList);
                System.out.println("offset方差值: " + df.format(varience));
                round++;
                System.out.println("同步轮次： " + round);
                FileOutput.variancefileoutput(round, varience);


//        }
//        for(int i = 0;i < 10;i++){
//            for(Node node : nodeList){
//
//                NodeFunction.showtheNode(node);
//                try {
//                    Thread.currentThread().sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
            }else {
                //改变节点位置
               for (int i = 0;i < nodeList.size();i++){
                   nodeList.get(i).move(round);
                   System.out.println(nodeList.get(i).toString());
               }
//                NodeFunction.refrashLocation(nodeList,round);
                //再一次构建邻居表
                for (int i = 0;i<nodeList.size();i++){
                    NodeFunction.buildneighborMap(nodeList.get(i),nodeList);
                }
                //打印邻居表
                for (Node node : nodeList) {
                    HashMap<Integer, Node> neighbor = node.getNeighborMap();
                    for (Map.Entry<Integer, Node> entry : neighbor.entrySet()) {
                        System.out.println("节点" + node.getID() + "的邻居有： " + entry.getValue().getID());
                    }
                }
                //打乱队列
                Collections.shuffle(nodeList);
                System.out.println("当前队列的顺序为：");
                for (Node node : nodeList) {
                    System.out.print(node.getID() + "\t");
                }
                System.out.println();
                //开始同步
                for (Node node : nodeList) {
                    node.synchronisation();
                }
                varience = NodeFunction.getOffsetVariance(nodeList);
                System.out.println("offset方差值: " + df.format(varience));
                round++;
                System.out.println("同步轮次： " + round);
                FileOutput.variancefileoutput(round, varience);

            }
        }
        //如果需要开启训练过程
      /*  if (training){
            FileOutput.deletQtable();
            for (int i = 0;i <nodeList.size();i++){
                String filename ="node" +  nodeList.get(i).getID() + ".txt";
                FileOutput.makeQtablefile(nodeList.get(i),filename);
            }
        }*/

        //结束node线程
        for (int i = 0; i < nodeList.size(); i++) {
            nodeList.get(i).exit = true;
        }
    }
}
