import java.util.List;

public class Printer {
    List<Node> nodeList;


    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public void printList(){
        for (Node node : nodeList){
            node.setLocalTime(node.getLocalTime()+1);
            String filename = "node" + node.getID() + ".txt";
            FileOutput.fileoutput(node,filename);
            FileOutput.matlabFileoutput(node,filename);
        }
    }
}
