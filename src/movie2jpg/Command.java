package movie2jpg;
import java.io.*;

public class Command
{
    private java.lang.String cmd;    /** プロパティ cmd(実行するｺﾏﾝﾄﾞﾗｲﾝ)。 */
    private java.io.File workDir;   /** プロパティ workDir の値 */
    
    /** 
    *	サンプル
    * @param args	パラメータ
    */
    static public void main(String[] args) {
        if (args.length < 1) {
            System.out.println("exp: java movie2jpg.Command [commandLine]");
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
            command.execCommand();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 
     * ｺﾏﾝﾄﾞを実行しその出力ｽﾄﾘｰﾑを取得
     * @exception IOException I/Oｴﾗｰが発生した場合
     */
    public void execCommand() throws IOException {
        System.out.println("[Command start] " + cmd);

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
        
        try { 
            process.waitFor();  // ｺﾏﾝﾄﾞ終了まで待機
        } catch (InterruptedException e) {}

        System.out.println("[Command end] "+ getCmd() +"\n");
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

}
