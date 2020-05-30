public enum ClockState {
    //枚举类方便后期增加权值，目前不做研究
    HAS_GPS(1.0,"GPS"),NO_GPS(0.2,"No GPS"),NIGHBOR_GPS(0.8,"Neighbor GPS");
    private double weight;
    private String state_name;

    ClockState(double weight) {
        this.weight = weight;
    }

    ClockState(double weight, String state_name) {
        this.weight = weight;
        this.state_name = state_name;
    }

    public double getWeight() {
        return weight;
    }

    public String getState_name() {
        return state_name;
    }
}
