public class Clock {
    ClockListener clockListener;
    public static volatile int  BASECLOCK = 0;
    public Clock(ClockListener clockListener) {
        this.clockListener = clockListener;
    }
    public static  void changeBaseClock(){
        Object lock = new Object();
        synchronized (lock){
        BASECLOCK++;
        }
    }
    public void send(){
        changeBaseClock();
        clockListener.onMessage();
    }

}
