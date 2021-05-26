//这个类用来规定一些全局变量
public class MyMap {
    //地图的大小
    public static final int BOUNDARY_X = 300;
    public static final int BOUNDARY_Y = 300;
    //所有节点生成时钟的基准
    public static final int BASEtIME =(int)System.currentTimeMillis();
    //节点个数
    public static final int NODESNUMBER = 10;
    //得到一个5-10ms的传播时延
    public static int getPropagationDelay(){
        return 5;
    }
    //记录时隙数
    public static volatile int SOLT_NUMBER = 0;
    //给Offset添加一个随机的偏差
    public static long getRandomOffset(){
        return (long)Math.random()*5;
    }
    public static volatile long BASECLOCK = 0;
    public static void changeBASECLOCK(){
        Object lock = new Object();
        synchronized (lock) {
            BASECLOCK++;
        }
    }
    //无gps标记
    public static  final int NO_GPS = 10000000;
    //最小状态值参数
    public static final double WEIGHTING_1 = 0.1;
    //圆心坐标
    public static final double CENTER_X = 35.0;
    public static final double CENTER_Y = 35.0;
    //选择邻居的比例
    public static final double CHOINCE = 1.0;
}
