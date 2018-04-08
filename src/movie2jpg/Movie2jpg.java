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
    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("exp: java movie2jpg.Movie2jpg");
        (new Movie2jpg()).proc();
    }
    
    public File workDir = null;
    public File movieDir;
    public File imgDir;
    
    /**
     * 
     */
    public Movie2jpg() {
        workDir = new File(".");
        movieDir = new File(workDir, "Movie");      // './Movie'ディレクトリ
        imgDir = new File(workDir, "img");          // './img'ディレクトリ
    }
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    public void proc() throws FileNotFoundException, IOException {
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
    }
    
    /**
     * 
     * @param mp4File 
     * @throws java.io.IOException 
     */
    public void ffmpeg(File mp4File) throws IOException {
        String name = mp4File.getName();
        name = name.substring(0, name.length()-4);
        if (!imgDir.exists()) {
            imgDir.mkdir();
        }
        File outDir = new File(imgDir, name);
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        
        String dest = "img/"+ name +"/%05d.jpg";
        String commandLine = String.format("ffmpeg -ss 0  -i %s -f image2 -r 15 %s", mp4File.getAbsolutePath(), dest);
        System.out.println(commandLine);
        
        Command command = new Command();
        command.setCmd(commandLine);
        command.setWorkDir(workDir);
        command.execCommand();
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
