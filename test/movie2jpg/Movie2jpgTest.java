/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package movie2jpg;

import java.io.File;
import java.util.ArrayList;
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
    
    public Movie2jpgTest() {
    }
    
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
    }

    @Test
    public void testMain() throws Exception {
        System.out.println("main");
        ArrayList<String> strArray = new ArrayList();
        strArray.add("./Movie/Movie2jpg.ini");
        String[] args = strArray.toArray(new String[1]);
        Movie2jpg.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testProc() throws Exception {
        System.out.println("proc");
        Movie2jpg instance = null;
        instance.proc();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    public void testFfmpeg() throws Exception {
        System.out.println("ffmpeg");
        File mp4File = null;
        Movie2jpg instance = null;
        instance.ffmpeg(mp4File);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
