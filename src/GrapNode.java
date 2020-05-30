import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class GrapNode extends JFrame{
    List<Node> nodeList;
    public GrapNode(List<Node> nodeList){
        setSize(MyMap.BOUNDARY_X+100,MyMap.BOUNDARY_Y+100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.nodeList = nodeList;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.blue);
        for(Node node : nodeList){
            int x =(int) node.getLocation().getX_coordinates() + 100;
            int y =(int) node.getLocation().getY_coordinates() + 100;
            int id = node.getID();
            String s = String.valueOf(id);
            Collection<Node> neighbors = node.getNeighborMap().values();
            g.drawOval(x,y,8,8);
            Font font=new Font("宋体",Font.BOLD,10);
            g.setFont(font);
            g.drawString(s,x,y+15);
            for(Node neighbor : neighbors){
                int nx = (int) neighbor.getLocation().getX_coordinates() + 100;
                int ny = (int) neighbor.getLocation().getY_coordinates() + 100;
                g.setColor(Color.BLACK);
                g.drawLine(x,y,nx,ny);
            }
        }
    }

}



