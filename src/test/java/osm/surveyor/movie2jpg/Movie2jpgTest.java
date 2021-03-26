package osm.surveyor.movie2jpg;

import java.io.File;
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
    File imgdir = new File("img");
    File dir = new File(imgdir, "FILE190428-121735F");
    
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
        File work = new File("src/test/resources");
        Movie2jpg ins = new Movie2jpg(work);
        ins.setDestinationDir(new File(work, "img"));
        ins.setSourceDir(new File(work, "Movie"));
        ins.proc();
        assertThat(imgdir.exists(), is(true));
        assertThat(dir.isDirectory(), is(true));
        File[] filelist = dir.listFiles();
        assertNotNull(filelist);
        assertThat(filelist.length, is(109));
        for (File file : filelist) {
            assertThat(file.getName().endsWith(".jpg"), is(true));
        }
    }
}
