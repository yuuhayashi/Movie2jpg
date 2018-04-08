package osm.jp.gpx;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import org.apache.commons.imaging.ImageReadException;

/**
 * 動画から一定間隔で切り出したIMAGEのファイル更新日時を書き換える
 * 
 * @author yuu
 */
public class Restamp extends Thread {
    /**
     * 実行中に発生したExceptionを保持する場所
     */
    public Exception ex = null;
	
    /**
     * メイン
     * 動画から一定間隔で切り出したIMAGEのファイル更新日時を書き換える
     * 
     * ・画像ファイルの更新日付を書き換えます。(Exi情報は無視します)
     *    ※ 指定されたディレクトリ内のすべての'*.jpg'ファイルを処理の対象とします
     * ・画像は連番形式（名前順に並べられること）の名称となっていること
     * 
     * パラメータ
     * ・対象のフォルダ（ディレクトリ内のすべての'*.jpg'ファイルを処理の対象とします）
     * ・基準となる画像
     * ・基準画像の正しい日時
     * ・画像ファイルの間隔（秒）
     * 
     *  exp) $ java -cp .:AdjustTime.jar:commons-imaging-1.0-SNAPSHOT.jar  [AdjustTime.ini]
     *  exp) > java -cp .;AdjustTime.jar;commons-imaging-1.0-SNAPSHOT.jar [AdjustTime.ini]
     * 
     * 1. 予め、動画から画像を切り出す
     * 　　ソースファイル（mp4ファイル）; 「-i 20160427_104154.mp4」
     *     出力先: 「-f image2 img/%06d.jpg」 imgフォルダに６桁の連番ファイルを差出力する
     * 　　切り出し開始秒数→ 「-ss 0」 （ファイルの０秒から切り出し開始）
     * 　　切り出し間隔； 「-r 30」 (１秒間隔=３０fps間隔)
     * ```
     * $ cd /home/yuu/Desktop/OSM/20180325_横浜新道
     * $ ffmpeg -ss 0  -i 20160427_104154.mp4 -f image2 -r 15 img/%06d.jpg
     * ```
     * 
     * 2. ファイルの更新日付を書き換える
     * ```
     * $ cd /home/yuu/Desktop/workspace/AdjustTime/importPicture/dist
     * $ java -cp .:AdjustTime2.jar osm.jp.gpx.Restamp /home/yuu/Desktop/OSM/20180325_横浜新道/img 000033.jpg 2018-03-25_12:20:32 003600.jpg  2018-03-25_13:20:09
     * ```
     * 
     * @param argv
     * argv[0] = 画像ファイルが格納されているディレクトリ		--> imgDir
     * argv[1] = 時刻補正の基準とする画像ファイル			--> baseFile
     * argv[2] = 基準画像ファイルの精確な撮影日時 "yyyy-MM-dd_HH:mm:ss" --> baseTime
     * argv[3] = 時刻補正の基準とする画像ファイル			--> baseFile
     * argv[4] = 基準画像ファイルの精確な撮影日時 "yyyy-MM-dd_HH:mm:ss" --> baseTime
     * 
     * @throws IOException
     * @throws ImageReadException 
     */
    public static void main(String[] argv) throws Exception
    {
        if (argv.length < 5) {
            System.out.println("java Restamp <imgDir> <baseFile1> <timeStr1> <baseFile2> <timeStr2>");
            return;
        }
        
        File imgDir = new File(argv[0]);
        if (!imgDir.exists()) {
            System.out.println("[error] <imgDir>が存在しません。");
            return;
        }
        if (!imgDir.isDirectory()) {
            System.out.println("[error] <imgDir>がフォルダじゃない");
            return;
        }
        
        File baseFile1 = new File(imgDir, argv[1]);
        if (!baseFile1.exists()) {
            System.out.println("[error] <baseFile1>が存在しません。");
            return;
        }
        if (!baseFile1.isFile()) {
            System.out.println("[error] <baseFile1>がファイルじゃない");
            return;
        }
        
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss");
    	Date baseTime1 = df1.parse(argv[2]);

        File baseFile2 = new File(imgDir, argv[3]);
        if (!baseFile2.exists()) {
            System.out.println("[error] <baseFile2>が存在しません。");
            return;
        }
        if (!baseFile2.isFile()) {
            System.out.println("[error] <baseFile2>がファイルじゃない");
            return;
        }
        
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss");
    	Date baseTime2 = df2.parse(argv[4]);

        Restamp obj = new Restamp();
        obj.setUp(imgDir, baseFile1, baseTime1, baseFile2, baseTime2);
    }
    
    public File imgDir;
    public Date baseTime1;
    public Date baseTime2;
    public int bCount1 = 0;
    public int bCount2 = 0;
    public long span = 0;
    public ArrayList<File> jpgFiles = new ArrayList<>();
	
    @SuppressWarnings("Convert2Lambda")
    public void setUp(File imgDir, File baseFile1, Date baseTime1,  File baseFile2, Date baseTime2) throws Exception {
    	// 指定されたディレクトリ内のGPXファイルすべてを対象とする
        File[] files = imgDir.listFiles();
        java.util.Arrays.sort(files, new java.util.Comparator<File>() {
            @Override
            public int compare(File file1, File file2){
                return file1.getName().compareTo(file2.getName());
            }
        });
        bCount1 = 0;
        bCount2 = 0;
        boolean base1 = false;
        boolean base2 = false;
        for (File file : files) {
            if (file.isFile()) {
                String filename = file.getName().toUpperCase();
                if (filename.toUpperCase().endsWith(".JPG")) {
                    this.jpgFiles.add(file);
                    bCount1 += (base1 ? 0 : 1);
                    bCount2 += (base2 ? 0 : 1);
                    if (file.getName().equals(baseFile1.getName())) {
                        base1 = true;
                    }
                    if (file.getName().equals(baseFile2.getName())) {
                        base2 = true;
                    }
                }
            }
        }

        try {
            // imgDir内の画像ファイルを処理する
            long span1 = baseTime2.getTime() - baseTime1.getTime();
            long spanX = span1 / (bCount2 - bCount1);
            int i = 0;
            for (File jpgFile : this.jpgFiles) {
                long deltaMsec = (i - bCount1) * spanX;
                i++;
                Calendar cal = Calendar.getInstance();
                cal.setTime(baseTime1);
                cal.add(Calendar.MILLISECOND, (int) deltaMsec);
                jpgFile.setLastModified(cal.getTimeInMillis());
            }
            
            // 
            // 0.5sec間隔の時は１秒間隔に間引く
            // 
            if ((spanX > 450) && (spanX < 550)) {
                boolean even = ((bCount1 & 1) == 0);
                int j = 0;
                for (File jpgFile : this.jpgFiles) {
                    j++;
                    if (even) {
                        if ((j & 1) != 0) {
                            jpgFile.deleteOnExit();
                        }
                    }
                    else {
                        if ((j & 1) == 0) {
                            jpgFile.deleteOnExit();
                        }
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            this.ex = new Exception(e);
        }
    }
    
    /**
     * 対象は '*.JPG' のみ対象とする
     * @return 
     * @param name
     */
    public static boolean checkFile(String name) {
        return ((name != null) && name.toUpperCase().endsWith(".JPG"));
    }

    /**
     * ファイル名の順序に並び替えるためのソートクラス
     * 
     * @author hayashi
     */
    static class FileSort implements Comparator<File> {
        @Override
        public int compare(File src, File target){
            int diff = src.getName().compareTo(target.getName());
            return diff;
        }
    }

    /**
     * JPEGファイルフィルター
     * @author yuu
     */
    class JpegFileFilter implements FilenameFilter {
    	@Override
        public boolean accept(File dir, String name) {
            return name.toUpperCase().matches(".*\\.JPG$");
    	}
    }
}