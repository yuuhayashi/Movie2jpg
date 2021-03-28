package osm.surveyor.movie2jpg;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yuu
 */
public class Movie2jpgTest {
	String spa = FileSystems.getDefault().getSeparator();
	File workdir = new File("target");
	File mp4file = new File(workdir, "20191102_151730A.mp4");
    File imgdir = new File(workdir, "img");
    File dir = new File(imgdir, "20191102_151730A");
    int fileCount = 157;
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
        dir.deleteOnExit();
        imgdir.deleteOnExit();
    }

    @Test
    public void testMain() throws Exception {
    	System.out.println("mp4file = "+ mp4file.getAbsolutePath());
    	
    	assertThat(mp4file.exists(), is(true));
    	assertThat(mp4file.isFile(), is(true));

    	List<String> args = (new ArrayList<>());
    	args.add("target");
    	Movie2jpg.main((String[])args.toArray(new String[1]));
        
        assertThat(imgdir.exists(), is(true));
        assertThat(dir.isDirectory(), is(true));
        File[] filelist = dir.listFiles();
        assertNotNull(filelist);
        assertThat(filelist.length, is(fileCount));
        for (File file : filelist) {
            assertThat(file.getName().endsWith(".jpg"), is(true));
        }
    }
}
