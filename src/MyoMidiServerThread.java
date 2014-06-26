import java.net.*;
import java.io.*;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

import com.google.gson.Gson;
 
public class MyoMidiServerThread extends Thread {
    private Socket socket = null;
    private int beatCount = 0;
    private MyoMidiPlayer mplayer;
    private Gson gson;
 
    public MyoMidiServerThread(Socket socket, Transmitter device) {
        super("MyoMidiServerThread");
        this.socket = socket;
        
        gson = new Gson();
        mplayer = new MyoMidiPlayer();
        mplayer.addListener(new MIDIBeatListener());
        mplayer.start();
    }
     
    public void run() {
 
        try {
            //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));
         
            String inputLine; //, outputLine;
            //KnockKnockProtocol kkp = new KnockKnockProtocol();
            //outputLine = kkp.processInput(null);
            //out.println(outputLine);
 
            while ((inputLine = in.readLine()) != null) {    
            	
            	MyoEvent me = gson.fromJson(inputLine, MyoEvent.class);
            	//System.out.println(me.x);
            	mplayer.processMyoEvent(me);
            	            	
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mplayer.stop();
    }
    
    class MIDIBeatListener implements ControllerEventListener
    {
        public void controlChange (ShortMessage mess)
        {  
            beatCount++;
            if(beatCount == 16) beatCount = 0;
            //System.out.println(beatCount);
        }    
    }
}