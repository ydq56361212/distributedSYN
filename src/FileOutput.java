import java.io.*;
import java.util.ArrayList;
import java.util.List;

//定义相关的文件操作
public class FileOutput {
    private List<Node> nodes;
    public static final String ABSOLUTEPATH = "D:\\ideaworkpspace\\distributedSYN\\output";
    public static final String RELATIVEPATH = "./output";
    public static final String MATLAB_APATH = "D:\\ideaworkpspace\\distributedSYN\\Matlaboutput";
    public static final String MATLAB_RPATH = "./Matlaboutput";
    public static final String VARIANCE_PATH = "./Varianceoutput";
    public static final String QTABLE_PATH = "./Qtable";
    public static final String TIME_PATH = "./Timeoutput";
    public static final String LOCATION_PATH = "./Location";
    public FileOutput(List<Node> nodes) {
        this.nodes = nodes;
    }

    public static void fileoutput(Node node, String filename){
        File file = new File(RELATIVEPATH + "\\" + filename);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建失败");
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(RELATIVEPATH + "\\" + filename,true));
            //String output = node.toString() + "\t当前时隙数：" + MyMap.SOLT_NUMBER + "\t时钟偏差：" + node.getClockOffset();
            String output = node.toString() + "\t当前基础时钟：" + Clock.BASECLOCK + "\t时钟偏差：" + (node.getLocalTime() - Clock.BASECLOCK);
            bw.write(output);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }
    //开始记录前将文件清空
    public static void clearFile(Node node,String filename){
        File file = new File(RELATIVEPATH + "\\" + filename);
        if(file == null){
            System.out.println("打开文件失败");
        }
        else if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件创建失败");
            }
        }
        else {

            try {
                FileWriter fileWriter = new FileWriter(RELATIVEPATH + "\\" + filename);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("清理失败");
            }
        }
    }
    public static void matlabFileoutput(Node node,String filename){
        File file = new File(MATLAB_RPATH + "\\" + filename);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建失败");
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(MATLAB_RPATH + "\\" + filename,true));
//            String output = MyMap.SOLT_NUMBER + "\t" + node.getLocalTime();
            String output = Clock.BASECLOCK + "\t" + node.getLocalTime();
//            String output = MyMap.BASECLOCK + "\t" + node.getClockOffset();
//            String output = System.currentTimeMillis() + "\t" + node.getLocalTime();
            bw.write(output);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }
    //开始记录前将文件清空
    public static void matlabClearFile(Node node,String filename){
        File file = new File(MATLAB_RPATH + "\\" + filename);
        if(file == null){
            System.out.println("打开文件失败");
        }
        else if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件创建失败");
            }
        }
        else {

            try {
                FileWriter fileWriter = new FileWriter(MATLAB_RPATH + "\\" + filename);
                fileWriter.write("");
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("清理失败");
            }
        }
    }

    public static void deleteFile(){
        File file = new File(RELATIVEPATH);
        String[] filelist = file.list();
        for(String name : filelist){
            File tempfile = new File(RELATIVEPATH +"\\"+ name);
            tempfile.delete();
        }
    }
    public static void deleteMatlabFile(){
        File file = new File(MATLAB_RPATH);
        String[] filelist = file.list();
        for(String name : filelist){
            File tempfile = new File(MATLAB_RPATH +"\\"+ name);
            tempfile.delete();
        }
    }
    public void printList(){
        for (Node node : nodes){
            String filename = "node" + node.getID() + ".txt";
            FileOutput.fileoutput(node,filename);
            FileOutput.matlabFileoutput(node,filename);
        }
    }
    public static void variancefileoutput(int round,double variance){
        File file = new File(VARIANCE_PATH + "\\" + "variance.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建失败");
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(VARIANCE_PATH + "\\" + "variance.txt",true));
            //String output = node.toString() + "\t当前时隙数：" + MyMap.SOLT_NUMBER + "\t时钟偏差：" + node.getClockOffset();
            String output = round + "\t" + variance;
            bw.write(output);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }
    public static void deletevarianceFile(){
        File file = new File(VARIANCE_PATH);
        String[] filelist = file.list();
        for(String name : filelist){
            File tempfile = new File(VARIANCE_PATH +"\\"+ name);
            tempfile.delete();
        }
    }
    public static void makeQtablefile(Node node,String filename){
        File file = new File(QTABLE_PATH + "\\" + filename);
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建失败");
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            double[][][] tmpQtable = node.getQ_table();
            if (tmpQtable.length == 0){
                return;
            }
            for (int i = 0;i < tmpQtable.length;i++){
                for (int j = 0;j < 3;j++){
                    for (int k = 0;k < tmpQtable[i][j].length;k++){
                        bw.write(Double.toString(tmpQtable[i][j][k]) + "\t");
                    }
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
           System.out.println("写入失败");
        }

    }
    public static void loadQtable(Node node,String filename){
        File file = new File(QTABLE_PATH + "\\" + filename);
        BufferedReader br = null;
        try {
             br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        double[][][] tmpQtable = node.getQ_table();
        for (int i = 0;i < tmpQtable.length;i++){
            for (int j = 0;j < 3;j++){
                try {
                    String tmpString = br.readLine();
                    String[] tmp = tmpString.split("\t");
                    for (int k = 0;k < tmpQtable[i][j].length;k++){
                        tmpQtable[i][j][k] = Double.valueOf(tmp[k]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        node.setQ_table(tmpQtable);
    }
    public static void deletQtable(){
        File file = new File(QTABLE_PATH);
        String[] filelist = file.list();
        for (String name : filelist){
            File tmpfile = new File(QTABLE_PATH + "\\" + name);
            tmpfile.delete();
        }
    }
    public static void timefileoutput(int round,int time){
        File file = new File( TIME_PATH + "\\" + "time.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建失败");
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(TIME_PATH + "\\" + "time.txt",true));
            //String output = node.toString() + "\t当前时隙数：" + MyMap.SOLT_NUMBER + "\t时钟偏差：" + node.getClockOffset();
            String output = round + "\t" + time;
            bw.write(output);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }
    public static void endtimefileoutput(int round,int time){
        File file = new File( TIME_PATH + "\\" + "endtime.txt");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建失败");
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(TIME_PATH + "\\" + "endtime.txt",true));
            //String output = node.toString() + "\t当前时隙数：" + MyMap.SOLT_NUMBER + "\t时钟偏差：" + node.getClockOffset();
            String output = round + "\t" + time;
            bw.write(output);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入失败");
        }
    }
    public static void deleteTimeFile(){
        File file = new File(TIME_PATH);
        String[] filelist = file.list();
        for(String name : filelist){
            File tempfile = new File(TIME_PATH +"\\"+ name);
            tempfile.delete();
        }
    }
    public static void locationFile(Node node,int roundNumber,String filename){
        File file = new File(LOCATION_PATH + "\\" + filename);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("创建失败");
            }
        }
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(LOCATION_PATH + "\\" + filename,true));
            ArrayList<Double[]> locations = (ArrayList<Double[]>) node.getMoveTrack();
            for (int i = 0;i < roundNumber; i++){
                String output = locations.get(i)[0] + "\t" + locations.get(i)[1];
                bw.write(output);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("写入失败");
            e.printStackTrace();
        }
    }
    public static void deleteTrackFile(){
        File file = new File(LOCATION_PATH);
        String[] filelist = file.list();
        for(String name : filelist){
            File tempfile = new File(LOCATION_PATH +"\\"+ name);
            tempfile.delete();
        }
    }



}
