package mirrormirror.swen302.mirrormirrorandroid.utilities;

import android.content.Context;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by Jack on 7/08/2017.
 */

public class SocketSingleton {

    private static SocketSingleton instance;
    private static final String SERVER_ADDRESS = "http://130.195.6.76:3000";
    private Socket socket;
    private Context context;

    public static SocketSingleton getInstance(Context context){
        if(instance == null){
            instance = getSync(context);
        }
        instance.context = context;
        return instance;
    }

    public static synchronized SocketSingleton getSync(Context context){
        if (instance == null) {
            instance = new SocketSingleton(context);
        }
        return instance;
    }

    public Socket getSocket(){
        if(!this.socket.connected()){
            return socket.connect();
        }else{
            return this.socket;

        }
    }

    private SocketSingleton(Context context){
        this.context = context;
        try {
            this.socket = IO.socket(SERVER_ADDRESS);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }



}