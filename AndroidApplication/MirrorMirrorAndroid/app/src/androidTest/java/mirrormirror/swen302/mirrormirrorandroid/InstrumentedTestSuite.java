package mirrormirror.swen302.mirrormirrorandroid;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase;
import android.test.ActivityInstrumentationTestCase2;

import com.github.nkzawa.socketio.client.Socket;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Timer;
import java.util.TimerTask;

import mirrormirror.swen302.mirrormirrorandroid.activities.HomeActivity;
import mirrormirror.swen302.mirrormirrorandroid.utilities.ServerController;
import mirrormirror.swen302.mirrormirrorandroid.utilities.SocketSingleton;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTestSuite {

    @Rule
    public ActivityTestRule<HomeActivity> homeActivityActivityTestRule = new
            ActivityTestRule<HomeActivity>(HomeActivity.class);

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("mirrormirror.swen302.mirrormirrorandroid", appContext.getPackageName());
    }

    @Test
    public void testSocketConnection (){
        Context appContext = InstrumentationRegistry.getTargetContext();
        final Socket s = SocketSingleton.getInstance(appContext).getSocket();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                assertTrue(s.connected());

            }
        }, 1000);
    }

    @Test
    public void testRequestImageAdditions (){
        Context appContext = InstrumentationRegistry.getTargetContext();

        final HomeActivity homeActivity = homeActivityActivityTestRule.getActivity();
        final int initialFiles = homeActivity.getFilePaths().size();
        ServerController.sendImageAdditionsRequest(appContext, 3, initialFiles);
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                assertTrue(homeActivity.getFilePaths().size() > initialFiles);

            }
        }, 1000);
    }

}
