public class Calculate {
    //起点坐标
    private double ballXStart = 300;
    private double ballYStart = 300;
    //起始时间
    private final double startTime;
    //速度，单位/s
    private double speed = 1000;
    //定义小球移动的弧度
    private double degree = 3.14 / 2;//初始弧度60°

    //地图大小
    private double map_Xlength = 865;
    private double map_Ylength = 500;

    //地图边界大小（类比台球桌边框）
    private double map_boundary_X = 40;
    private double map_boundary_Y = 40;

    //小球直径
    private double diameter = 0;

    //degree弧度制
    public Calculate(double ballXStart, double ballYStart, double speed, double degree) {
        this.ballXStart = ballXStart;
        this.ballYStart = ballYStart;
        this.startTime = System.currentTimeMillis();
        this.speed = speed;
        this.degree = 3.14 / degree;
    }

    public Calculate() {
        this.startTime = System.currentTimeMillis();
    }

    public Calculate(double ballXStart, double ballYStart, double speed, double degree, double map_Xlength, double map_Ylength, double map_boundary_X, double map_boundary_Y, double diameter) {
        this.ballXStart = ballXStart;
        this.ballYStart = ballYStart;
        this.startTime = System.currentTimeMillis();
        this.speed = speed;
        this.degree = degree;
        this.map_Xlength = map_Xlength;
        this.map_Ylength = map_Ylength;
        this.map_boundary_X = map_boundary_X;
        this.map_boundary_Y = map_boundary_Y;
        this.diameter = diameter;
    }

    public Double[] locationMethod(int time) {
        //double time = (System.currentTimeMillis() - startTime) / 1000;
        double X_helper = map_Xlength - map_boundary_X - diameter - map_boundary_X;
        double Y_helper = map_Ylength - map_boundary_Y - diameter - map_boundary_Y - diameter;
        //运动时间过长会出问题
        double ballX = Math.abs((ballXStart + 10000000 * map_Xlength + time * speed * Math.cos(degree)) % (2 * X_helper) - X_helper) + map_boundary_X;
        double ballY = Math.abs((ballYStart + 10000000 * map_Ylength + time * speed * Math.sin(degree)) % (2 * Y_helper) - Y_helper) + diameter + map_boundary_Y;
        return new Double[]{ballX, ballY};
    }
}
