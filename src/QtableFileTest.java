import java.io.*;

public class QtableFileTest {
    public static final String QTABLE_PATH = "./Qtable";
    public static void makefile(String filename){
        File file = new File(QTABLE_PATH + "\\" + filename);
        if (!file.exists()){
            file.mkdir();
        }

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(QTABLE_PATH + "\\" + filename + "\\" + "test.txt"));
            for (int i = 0; i < 3;i++){
                bw.write("1" + "\t" + "2" + "\t" + "3"  + "\t");
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void readfile(String filename,int[][][] qtable){
        File file = new File(QTABLE_PATH + "\\" + filename + "\\" + "test.txt");
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (int i = 0;i < qtable.length;i++){
            for (int j = 0;j < qtable[i].length;j++){
                String tmpString = null;
                try {
                    tmpString = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String[] tmp = tmpString.split("\t");
                for (int k = 0; k < qtable[i][j].length;k++){
                    qtable[i][j][k] = Integer.valueOf(tmp[k]);
                }
            }


        }
    }

    public static void main(String[] args) {
        QtableFileTest.makefile("node");
        int[][][] qtable = new int[1][3][3];
        QtableFileTest.readfile("node",qtable);
        for (int i = 0;i < qtable.length;i++){
            for (int j = 0;j < 3;j++){
                for (int k = 0;k < qtable[i][j].length;k++){
                    System.out.print(qtable[i][j][k] + "\t");
                }
                System.out.println();
            }
        }
    }
}
