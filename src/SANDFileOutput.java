import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

//定义相关的文件操作
public class SANDFileOutput {
    public static final String ABSOLUTEPATH = "D:\\ideaworkpspace\\distributedSYN\\SANDoutput";
    public static final String RELATIVEPATH = "./SANDoutput";
    public static final String MATLAB_APATH = "D:\\ideaworkpspace\\distributedSYN\\SANDMatlaboutput";
    public static final String MATLAB_RPATH = "./SANDMatlaboutput";
    public static void fileoutput(Node node,String filename){
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

}
