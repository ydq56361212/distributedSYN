import java.util.Random;

public class Egreedy {
    //贪心算法中的E
    static final double E = 0.05;

    static int maxQ_index = 1;



    public static int getNext_action(String state,double[][][] Q_table) {
        if (state == null){
            return 0;
        }
        int next_action;
        if (Math.random() <= E){
            next_action = getRandomQ_index(state,Q_table);
        }else
            next_action = getMaxQ_inex(state,Q_table);
        return next_action;
    }



    public static int getRandomQ_index(String state, double[][][] Q_table) {
        if (state == null){
            return 0;
        }
        int randomQ_index = 1;
        String[] tmp = state.split("_");
        int number = Integer.valueOf(tmp[1]);
        int action_space = 1;
        maxQ_index = getMaxQ_inex(state,Q_table);
        Random random = new Random();
        if (tmp[2].equals("up")){
            action_space = Q_table[number][0].length - 1;
        }
        else if (tmp[2].equals("down")){
            action_space = Q_table[number][1].length - 1;
        }
        else if (tmp[2].equals("equal")){
            action_space = Q_table[number][2].length - 1;
        }
        randomQ_index = random.nextInt(action_space) % (action_space) + 1;
        while (randomQ_index == 0 || randomQ_index == maxQ_index){
            randomQ_index = random.nextInt(action_space) % (action_space) + 1;
        }
        return randomQ_index;
    }



    public static int getMaxQ_inex(String state, double[][][] Q_table) {
        if (state == null){
            return 0;
        }
        String[] tmp = state.split("_");
        int number = Integer.valueOf(tmp[1]);
        if (tmp[2].equals("up")){
            maxQ_index = getMaxQ_inex(Q_table,number,0);
        }
        else if (tmp[2].equals("down")){
            maxQ_index = getMaxQ_inex(Q_table,number,1);
        }
        else if (tmp[2].equals("equal")){
            maxQ_index = getMaxQ_inex(Q_table,number,2);
        }
        return maxQ_index;
    }
    public static int getMaxQ_inex(double[][][] Q_Table,int state1,int state2){
        double max = 0.0;
        int max_index = 1;

        for (int i = 1;i < Q_Table[state1][state2].length;i++){
            if (Q_Table[state1][state2][i] >= max){
                max = Q_Table[state1][state2][i];
                max_index = i;
            }
        }
        return max_index;
    }




}
