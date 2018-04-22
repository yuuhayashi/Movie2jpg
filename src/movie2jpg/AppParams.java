package movie2jpg;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author yuu
 */
public class AppParams extends Properties {
    // 出力IMG: EXIFを変換する
    public static String FFMPEG_OUTPUT_FRAME_RATE = "FFMPEG_OUTPUT_FRAME_RATE";
    
    File file;

    public AppParams(File file) throws FileNotFoundException, IOException {
        super();
        this.file = file;
        syncFile();
    }
    
    private void syncFile() throws FileNotFoundException, IOException {
        boolean update = false;
        
        if (this.file.exists()) {
            // ファイルが存在すれば、その内容をロードする。
            this.load(new FileInputStream(file));
        }
        else {
            update = true;
        }
        
        //------------------------------------------------
        // IMG出力: EXIFを変換する
        String valueStr = this.getProperty(FFMPEG_OUTPUT_FRAME_RATE);
        if (valueStr == null) {
            update = true;
            valueStr = "30";
        }
        this.setProperty(FFMPEG_OUTPUT_FRAME_RATE, String.valueOf(valueStr));
        

        if (update) {
            // ・ファイルがなければ新たに作る
            // ・項目が足りない時は書き足す。
            this.store(new FileOutputStream(this.file), "defuilt settings");
        }
    }
	
    public void store() throws FileNotFoundException, IOException {
        this.store(new FileOutputStream(this.file), "by AppParams");
    }
}
