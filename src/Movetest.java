import java.util.ArrayList;
import java.util.List;

public class Movetest {
    public static void main(String[] args) {
        int roundNumber = 10;
        int round = 0;
        //是否开启动态
        boolean moveable = false;
        List<Node> nodeList = new ArrayList<>();
        Node node0 = new Node(0,35.0,35.0,NodeFunction.getRandomClockRatio(),NodeFunction.getRandomClockOffset());
        nodeList.add(node0);
        NodeFunction.moveNodesinCircle(nodeList,5,8);
        while (round < 5){
            for (int i = 0;i < nodeList.size();i++){
                nodeList.get(i).move(round);
                System.out.println(nodeList.get(i).toString());
            }
            round++;
        }
    }
}
