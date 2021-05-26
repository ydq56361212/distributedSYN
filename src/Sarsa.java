
import java.lang.String;
public class Sarsa {
    //Sarsa算法中的学习效率
    static final double A = 0.8;
    //Sarsa算法中的折扣因子
    static final double R = 0.8;


    public static double getQvale(String state,int action,double[][][] q_table){
        if (state == null){
            return 0;
        }
        String[] tmp = state.split("_");
        int state1 = Integer.valueOf(tmp[1]);
        int state2 = 0;
        double value;
        if (tmp[2].equals("dowm")){
            state2 = 0;
        }
        else if (tmp[2].equals("up")){
            state2 = 1;
        }
        else if (tmp[2].equals("equal")){
            state2 = 2;
        }
        value = q_table[state1][state2][action];
        return value;
    }
    //返回当前策略下的下一个行动
    public static int updateQtable(String state, String next_state,int action, double[][][] q_table){
        if (state == null){
            return 0;
        }
        int next_action;
        double value = getQvale(state,action,q_table);
        int reword = SelectionState.getReword(state);
        next_action = Egreedy.getNext_action(next_state,q_table);
        double next_value = getQvale(next_state,next_action,q_table);
        String[] tmp = state.split("_");
        int state1 = Integer.valueOf(tmp[1]);
        int state2 = 0;
        if (tmp[2].equals("dowm")){
            state2 = 0;
        }
        else if (tmp[2].equals("up")){
            state2 = 1;
        }
        else if (tmp[2].equals("equal")){
            state2 = 2;
        }
        value = value +  A*(reword + R*next_value - value);
        q_table[state1][state2][action] = value;
        return next_action;
    }


}
