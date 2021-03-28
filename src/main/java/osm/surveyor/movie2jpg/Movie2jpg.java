package osm.surveyor.movie2jpg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import java.util.function.Consumer;

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
        System.out.println("exp: java osm.surveyor.movie2jpg.Movie2jpg <work directory>");
        Path workpath = Paths.get(".");
        if (args.length > 0) {
        	workpath = Paths.get(args[0]);
        }
        (new Movie2jpg()).proc(workpath);
    }
    
    /**
     * コンストラクタ
     * 
     * @throws java.io.IOException
     */
    public Movie2jpg() throws IOException {
    }
    
    /**
     * 
     * @throws FileNotFoundException 
     */
    public void proc(Path workPath) throws Exception {
    	
        if (!Files.exists(workPath)) {
            throw new FileNotFoundException("'"+ workPath.toAbsolutePath().toString() +"'");
        }
        if (!Files.isDirectory(workPath)) {
            throw new FileNotFoundException("'"+ workPath.toAbsolutePath().toString() + "' is not directory.");
        }
        System.out.println(" workpath : '" + workPath.toAbsolutePath().toString() +"'");
        
        Path imgPath = Paths.get(workPath.toString() + FileSystems.getDefault().getSeparator() + "img");
        if (Files.exists(imgPath)) {
        	if (Files.isDirectory(imgPath)) {
                throw new FileNotFoundException("'"+ imgPath.toAbsolutePath().toString() + "' is not directory.");
        	}
        }
        else {
        	Files.createDirectories(imgPath);
        }
        System.out.println(" img path : '" + imgPath.toAbsolutePath().toString() +"'");

        // workPath 直下の 'mp4' と 'mov'ファイルをすべて処理する
        Files.list(workPath).forEach(new Consumer<Path>() {
        	@Override
        	public void accept(Path a) {
        		try {
            		String name = a.getFileName().toString();
                    if (name.toUpperCase().matches(".*\\.MP4$")) {
                        ffmpeg(a, imgPath);
                    }
                    else if (name.toUpperCase().matches(".*\\.MOV$")) {
                        ffmpeg(a, imgPath);
                    }
        		}
        		catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        });
        
        // 作成されたファイルのロックを解除
        chmod777(imgPath);
    }
    
    /**
     * コマンド：　~ffprobe $(movie file)~
     * コマンド：　~ffmpeg -ss 0 -i $(movie file) -f image2 -vf fps=$(FFMPEG_OUTPUT_FRAME_RATE) $(output file)~
     * @param mp4File
     * @throws java.io.IOException 
     */
    public void ffmpeg(Path mp4File, Path imgpath) throws Exception {
        String name = mp4File.getFileName().toString();
        name = name.substring(0, name.length()-4);

        Path outPath = Paths.get(imgpath.toString(), name);
        if (!Files.exists(outPath)) {
        	Files.createDirectories(outPath);
        }
        
        Path workDir = mp4File.getParent();
        Path stderr = Paths.get(workDir.toAbsolutePath().toString(), "stderr.txt");
        Files.deleteIfExists(stderr);
        
        String commandLine = String.format("ffprobe %s", mp4File.toAbsolutePath().toString());
        System.out.println("$ " + commandLine);

        Command command1 = new Command();
        command1.setCmd(commandLine);
        command1.setWorkDir(workDir.toFile());
        command1.execCommand(workDir.toFile());
        
        String fpsStr = null;
        List<String> lines = Files.readAllLines(stderr, Charset.forName("UTF-8"));
        for (String line : lines) {
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
        
        // String rate = this.params.getProperty(AppParams.FFMPEG_OUTPUT_FRAME_RATE);
        String spa = FileSystems.getDefault().getSeparator();
        String dest = imgpath.getFileName().toString() + spa + name + spa + "%05d.jpg";
        if (fpsStr == null) {
            fpsStr = "30";
        }
        commandLine = String.format("ffmpeg -ss 0  -i %s -f image2 -r %s %s", mp4File.toAbsolutePath().toString(), fpsStr, dest);
        System.out.println("$ " + commandLine);
        
        Command command = new Command();
        command.setCmd(commandLine);
        command.setWorkDir(workDir.toFile());
        command.execCommand(workDir.toFile());
    }
    
    /**
     * コマンド：　~chmod 777 -R $(dir)~
     * @param dir
     * @throws java.io.IOException 
     */
    public void chmod777(Path path) throws Exception {
        if (Files.exists(path)) {
            String commandLine = String.format("chmod 777 -R %s", path.toAbsolutePath().toString());
            System.out.println("$ " + commandLine);

            Command command = new Command();
            command.setCmd(commandLine);
            command.setWorkDir(null);
            command.execCommand(command.getWorkDir());
        }
    }
}
