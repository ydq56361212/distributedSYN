import java.util.List;

public class SANDPrinter {
    List<Node> nodeList;

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public void printList(){
        for (Node node : nodeList){
            String filename = "node" + node.getID() + ".txt";
            SANDFileOutput.fileoutput(node,filename);
            SANDFileOutput.matlabFileoutput(node,filename);
        }
    }
}
