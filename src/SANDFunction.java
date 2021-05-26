

import java.util.*;

public class SANDFunction {
    static SANDPrinter printer = new SANDPrinter();
    static Clock clock = new Clock(new ClockListener() {
        @Override
        public void onMessage() {
            printer.printList();
        }
    });
    static Printer syncprinter = new Printer();
    static Clock syncclock = new Clock(new ClockListener() {
        @Override
        public void onMessage() {
            syncprinter.printList();
        }
    });

    //得到一个初始化的token键是节点的Id，值是布尔值证明这个节点是否持有过token
    public static HashMap getInitialToken(List<Node> nodeList){
        HashMap<Integer,Boolean> tokenmap = new HashMap<>();
        for (Node node : nodeList){
            tokenmap.put(node.getID(),false);
        }
        return tokenmap;
    }

    public static void buildNeighbormap(List<Node> nodeList,Node tokenNode,HashMap<Integer,Boolean> token){
        token.put(tokenNode.getID(),true);
        NodeFunction.buildneighborMap(tokenNode,nodeList);
        if (!token.containsValue(false)){
//            System.out.println("node:" + tokenNode.getID() + tokenNode.getLocalTime());
//            System.out.println("node:" + tokenNode.getID() + tokenNode.getClockOffset());
            return;
        }
        HashMap<Integer,Node> neighbormap = tokenNode.getNeighborMap();
        if (neighbormap.isEmpty()){
            System.out.println("节点" + tokenNode.getID() + "未入网");
            return;
        }
        Collection<Node> neighbors = neighbormap.values();
        Iterator<Node> neighborsIterator = neighbors.iterator();
        String filename = "node" + tokenNode.getID() + ".txt";

        //遍历邻居节点看有没有没持有过token的节点
        //这里采用的是深度遍历
        while (neighborsIterator.hasNext()){
            SANDFileOutput.fileoutput(tokenNode,filename);
            Node slavnode = neighborsIterator.next();
            //如果邻居节点没有token，让邻居持有token并且两个节点进行主从式同步然后向下递归
            if(!token.get(slavnode.getID())){
                sycnBySAND(tokenNode,slavnode,token);
                buildNeighbormap(nodeList,slavnode,token);
//                break;
            }
        }

//        SANDFileOutput.fileoutput(tokenNode,filename);
//        SANDFileOutput.matlabFileoutput(tokenNode,filename);
        //跳出循环后说明这个节点的所有邻居都持有了token，那就将这个节点的上一层继续递归发现其他节点，直到token列表全部为true
        buildNeighbormap(nodeList,tokenNode,token);
    }

    public static void sycnBySAND(Node master,Node slave,HashMap<Integer,Boolean> token){
        String masterfilename = "node" + master.getID() + ".txt";
        String slavefilename = "node" + slave.getID() + ".txt";
        SANDFileOutput.fileoutput(master,masterfilename);
        SANDFileOutput.matlabFileoutput(master,masterfilename);
        SANDFileOutput.fileoutput(slave,slavefilename);
        SANDFileOutput.matlabFileoutput(slave,slavefilename);
        //修改slave在token中的状态

        clock.send();
        syncclock.send();
        long t1 = slave.getLocalTime();
        clock.send();
        syncclock.send();
        long t2 = MyMap.getPropagationDelay() + master.getLocalTime();
        for (int i = 0;i < MyMap.getPropagationDelay();i++){
            clock.send();
            syncclock.send();
        }
        long t3 = master.getLocalTime();
        clock.send();
        syncclock.send();
        long t4 = MyMap.getPropagationDelay() + slave.getLocalTime();
        for (int i = 0;i < MyMap.getPropagationDelay();i++){
            clock.send();
            syncclock.send();
        }
//        MyMap.SOLT_NUMBER += 2;
        long offset = ((t2 - t1) - (t4 - t3))/2;
        clock.send();
        syncclock.send();
        //给offset增加一个很小的随机值，代表同步过程中的不稳定因素
        offset += MyMap.getRandomOffset();
        clock.send();
        syncclock.send();
        long localtime = slave.getLocalTime();
        long clockoffset = slave.getClockOffset();
        slave.setLocalTime(localtime + offset);
        slave.setClockOffset(clockoffset + offset);
        token.put(slave.getID(),true);
        clock.send();
        syncclock.send();
//        SANDFileOutput.fileoutput(master,masterfilename);
//        SANDFileOutput.matlabFileoutput(master,masterfilename);
//        SANDFileOutput.fileoutput(slave,slavefilename);
//        SANDFileOutput.matlabFileoutput(slave,slavefilename);
    }

}
