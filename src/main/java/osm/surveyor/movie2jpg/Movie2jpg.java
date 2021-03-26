package osm.surveyor.movie2jpg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

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
        (new Movie2jpg()).proc();
    }
    
    public File workDir = null;
    public File movieDir;
    public File imgDir;
    
    /**
     * 
     * @throws java.io.IOException
     */
    public Movie2jpg() throws IOException {
        workDir = new File(".");
        movieDir = new File(workDir, "Movie");      // './Movie'ディレクトリ
        imgDir = new File(workDir, "img");          // './img'ディレクトリ
        System.out.println(movieDir.getAbsolutePath());
        System.out.println(imgDir.getAbsolutePath());
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
        
        String commandLine = String.format("ffprobe %s", mp4File.getAbsolutePath());
        System.out.println("# " + commandLine);

        Command command1 = new Command();
        command1.setCmd(commandLine);
        command1.setWorkDir(workDir);
        command1.execCommand(workDir);
        File outFile = new File(workDir, "stderr.txt");
        InputStream is = new FileInputStream(outFile);
        
        String fpsStr = null;
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(is, "utf-8"))) {
            String line;
            while((line = rd.readLine()) != null) {
                if (line.trim().startsWith("Stream #")) {
                    StringTokenizer st = new StringTokenizer(line, ",");
                    while (st.hasMoreTokens()) {
                        String token = st.nextToken();
                        if (token.endsWith(" fps")) {
                            fpsStr = token.substring(0, token.length()-4).trim();
                        }
                    }
                }
            }
        }


        // String rate = this.params.getProperty(AppParams.FFMPEG_OUTPUT_FRAME_RATE);
        String dest = "img/"+ name +"/%05d.jpg";
        if (fpsStr == null) {
            fpsStr = "30";
        }
        commandLine = String.format("ffmpeg -ss 0  -i %s -f image2 -r %s %s", mp4File.getAbsolutePath(), fpsStr, dest);
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
            if (name.toUpperCase().matches(".*\\.MP4$")) {
                return true;
            }
            else if (name.toUpperCase().matches(".*\\.MOV$")) {
                return true;
            }
            return false;
        }
    }
}
