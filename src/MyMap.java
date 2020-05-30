//这个类用来规定一些全局变量
public class MyMap {
    //地图的大小
    public static final int BOUNDARY_X = 200;
    public static final int BOUNDARY_Y = 200;
    //所有节点生成时钟的基准
    public static final int BASEtIME =(int)System.currentTimeMillis();
    //节点个数
    public static final int NODESNUMBER = 16;
    //得到一个5-10ms的传播时延
    public static int getPropagationDelay(){
        return 6;
    }
    //记录时隙数
    public static int SOLT_NUMBER = 0;
}
