public class SelectionState {
    private String[][] state_space;
    private int[] action_space;



    public static int getReword(String state) {
        if (state == null){
            return 0;
        }
        int reword = 0;
        String[] tmp = state.split("_");
        int communicatePay = Integer.valueOf(tmp[1]);
        reword += (communicatePay * -1);
        if (tmp[2].equals("down")){
            reword += 10;
        }
        else if (tmp[2].equals("up")){
            reword -= 100;
        }
        else if (tmp[2].equals("equal")){
            reword += 0;
        }
        return reword;
    }



    public String[][] getState_space() {
        return state_space;
    }

    public void setState_space(String[][] state_space) {
        this.state_space = state_space;
    }

    public int[] getAction_space() {
        return action_space;
    }

    public void setAction_space(int[] action_space) {
        this.action_space = action_space;
    }



}
