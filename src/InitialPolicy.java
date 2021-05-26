import java.util.Random;

public  class InitialPolicy {
    int next_action;
    Random random = new Random();

    public int getNext_action(String state,int space_size) {
        String[] tmp = state.split("_");
        next_action = random.nextInt(space_size/2)%(space_size/2)+1;
        if (tmp[2].equals("down")){
            while (next_action > space_size/2){
                next_action = random.nextInt(space_size/2)%(space_size/2)+1;
            }
        }
        else if (tmp[2].equals("up")){
            while (next_action <= space_size/2 ){
                next_action = random.nextInt(space_size)%((space_size/2)+1)+1;
            }
        }
        else if (next_action == 0){
            next_action = 1;
        }
        return next_action;
    }

    public void setNext_action(int next_action) {
        this.next_action = next_action;
    }
}
