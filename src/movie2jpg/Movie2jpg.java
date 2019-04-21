package movie2jpg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

/**
 *
 * @author yuu
 */
public class Movie2jpg {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws Exception {
        System.out.println("exp: java movie2jpg.Movie2jpg");
        (new Movie2jpg(new File(args[0]))).proc();
    }
    
    public File workDir = null;
    public File movieDir;
    public File imgDir;
    public AppParams params;
    
    /**
     * 
     * @param iniFile   "/mnt/osm/Movie/Movie2jpg.ini"
     * @throws java.io.IOException
     */
    public Movie2jpg(File iniFile) throws IOException {
        workDir = new File(".");
        movieDir = new File(workDir, "Movie");      // './Movie'ディレクトリ
        imgDir = new File(workDir, "img");          // './img'ディレクトリ
        this.params = new AppParams(iniFile);
        System.out.println(" - param: "+ AppParams.FFMPEG_OUTPUT_FRAME_RATE +"="+ this.params.getProperty(AppParams.FFMPEG_OUTPUT_FRAME_RATE));
    }
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    public void proc() throws Exception {
        if (!movieDir.exists()) {
            throw new FileNotFoundException(movieDir.getAbsolutePath());
        }
        if (!movieDir.isDirectory()) {
            throw new FileNotFoundException(movieDir.getAbsolutePath() + " is not directory.");
        }
        
        // './Movie'ディレクトリ直下の'.mp4'ファイルをすべて処理する
        File[] files = movieDir.listFiles(new Mp4FileFilter());
        for (File mp4 : files) {
            ffmpeg(mp4);
        }
        
        // 作成されたファイルのロックを解除
        chmod777(workDir);
    }
    
    /**
     * コマンド：　~ffmpeg -ss 0 -i $(mp4 file) -f image2 -vf fps=$(FFMPEG_OUTPUT_FRAME_RATE) $(output file)~
     * @param mp4File
     * @throws java.io.IOException 
     */
    public void ffmpeg(File mp4File) throws Exception {
        String name = mp4File.getName();
        name = name.substring(0, name.length()-4);
        if (!imgDir.exists()) {
            imgDir.mkdir();
        }
        File outDir = new File(imgDir, name);
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        
        String rate = this.params.getProperty(AppParams.FFMPEG_OUTPUT_FRAME_RATE);
        String dest = "img/"+ name +"/%05d.jpg";
        String commandLine = String.format("ffmpeg -ss 0  -i %s -f image2 -vf fps=%s %s", mp4File.getAbsolutePath(), rate, dest);
        System.out.println("# " + commandLine);
        
        Command command = new Command();
        command.setCmd(commandLine);
        command.setWorkDir(workDir);
        command.execCommand(workDir);
    }
    
    /**
     * コマンド：　~chmod 777 -R $(dir)~
     * @param dir
     * @throws java.io.IOException 
     */
    public void chmod777(File dir) throws Exception {
        if (dir.exists()) {
            String path = dir.getAbsolutePath();
            String commandLine = String.format("chmod 777 -R %s", path);
            System.out.println("# " + commandLine);

            Command command = new Command();
            command.setCmd(commandLine);
            command.setWorkDir(null);
            command.execCommand(command.getWorkDir());
        }
    }
    
    /**
     * MP4ファイルフィルター
     * @author yuu
     */
    class Mp4FileFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.toUpperCase().matches(".*\\.MP4$");
        }
    }
}
