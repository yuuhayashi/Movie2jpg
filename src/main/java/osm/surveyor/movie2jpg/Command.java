package osm.surveyor.movie2jpg;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import Command.StreamThread;

public class Command
{
    public static boolean debug = false;
    private java.lang.String cmd;    /** プロパティ cmd(実行するｺﾏﾝﾄﾞﾗｲﾝ)。 */
    private java.io.File workDir;   /** プロパティ workDir の値 */
    
    /** 
     * ｺﾏﾝﾄﾞを実行しその出力ｽﾄﾘｰﾑを取得
     * @param workDir
     * @exception IOException I/Oｴﾗｰが発生した場合
     */
    public void execCommand(File workDir) throws Exception {
        if (debug){
            System.out.println("[Command s] " + cmd);
        }

        if ((getCmd() == null) || getCmd().equals("")) {
            return;
        }

        // ｺﾏﾝﾄﾞを実行
        Process process;
        if (getWorkDir() == null) {
            process = Runtime.getRuntime().exec(getCmd());
        }
        else {
            process = Runtime.getRuntime().exec(getCmd(), null, getWorkDir());
        }

        new StreamThread(process.getInputStream(), new File(workDir, "stdout.txt")).start();
        new StreamThread(process.getErrorStream(), new File(workDir, "stderr.txt")).start();
        
        process.waitFor();  // ｺﾏﾝﾄﾞ終了まで待機
    }
    
    /** プロパティ cmd の取得メソッド。
     * @return プロパティ cmd の値。
     */
    public String getCmd() {
        return cmd;
    }
    
    /** プロパティ cmd の設定メソッド。
     * @param cmd プロパティ cmd の新しい値。
     */
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
    
    /** プロパティ workDir の取得メソッド。
     * @return プロパティ workDir の値。
     */
    public java.io.File getWorkDir() {
        return workDir;
    }
    
    /** プロパティ workDir の設定メソッド。
     * @param workDir プロパティ workDir の新しい値。
     */
    public void setWorkDir(java.io.File workDir) {
        this.workDir = workDir;
    }
    
    class StreamThread extends Thread {
        private static final int BUF_SIZE = 4096;
        private InputStream in;
        private BufferedOutputStream out;

        public StreamThread(InputStream in, String outputFilename) throws IOException {
            this.in  = in;
            this.out = new BufferedOutputStream(new FileOutputStream(outputFilename));
        }

        public StreamThread(InputStream in, File outputFile) throws IOException {
            this.in  = in;
            this.out = new BufferedOutputStream(new FileOutputStream(outputFile));
        }

        @Override
        public void run(){
            byte[] buf = new byte[BUF_SIZE];
            int size = -1;
            try{
                while((size = in.read(buf, 0, BUF_SIZE)) != -1){
                    out.write(buf, 0, size);
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }finally{
                try{in.close();} catch(IOException ex){}
                try{out.close();}catch(IOException ex){}
            }
        }
    }
    
    /** 
    *	サンプル
    * @param args	パラメータ
    */
    static public void main(String[] args) {
        if (args.length < 1) {
            System.out.println("exp: java tool.job.Command [commandLine]");
            return;
        }
        String commandLine = "";
        for (int i=0; i < args.length; i++) {
            if (i != 0) {
                commandLine += " ";
            }
            commandLine += args[i];
        }

        // エラー結果と実行結果を別々に取り出す
        System.out.println(commandLine);
        Command command = new Command();
        try {
            command.setCmd(commandLine);
            command.setWorkDir(null);
            command.execCommand(command.getWorkDir());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
