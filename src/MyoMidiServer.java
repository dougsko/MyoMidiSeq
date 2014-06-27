import java.net.*;
import java.util.Vector;
import java.io.*;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Sequencer;


 
public class MyoMidiServer {
    public static void main(String[] args) throws IOException, MidiUnavailableException {

    	if (args.length != 1) {
    		System.err.println("Usage: java MyoMidiServer <port number>");
    		System.exit(1);
    	}
 
        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        Transmitter device = chooseMidiDevice();
        System.exit(0);
        
        try {
        	ServerSocket serverSocket = new ServerSocket(portNumber);
            while (listening) {
                new MyoMidiServerThread(serverSocket.accept(), device).start();
            }
            
            serverSocket.close();
            
        } catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        } 
    }
    
    private static Transmitter chooseMidiDevice() throws MidiUnavailableException{
    	// Obtain information about all the installed synthesizers.
    	//Vector rcvrInfos = null;
    	MidiDevice device = null;
    	MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    	for (int i = 0; i < infos.length; i++) {
    	    try {
    	        device = MidiSystem.getMidiDevice(infos[i]);
    	    } catch (MidiUnavailableException e) {
    	          // Handle or throw exception...
    	    }
    	    if (device instanceof Transmitter) {
    	        //rcvrInfos.add(infos[i]);
    	        System.out.println("Transmitter: " + infos[i].getDescription());
    	    }
    	    else if (device instanceof Receiver) {
    	        //rcvrInfos.add(infos[i]);
    	        System.out.println("Receiver: " + infos[i].getDescription());
    	    }
    	    else if (device instanceof Synthesizer ) {
    	        //rcvrInfos.add(infos[i]);
    	        System.out.println("Synthesizer: " + infos[i].getDescription() + " max tx: " + device.getMaxTransmitters() + ", max rx: " + device.getMaxReceivers());
    	    }
    	    else if (device instanceof Sequencer ) {
    	        //rcvrInfos.add(infos[i]);
    	        System.out.println("Sequencer: " + infos[i].getDescription() + " max tx: " + device.getMaxTransmitters() + ", max rx: " + device.getMaxReceivers());
    	    }
    	}
    	System.exit(0);
    	return MidiSystem.getTransmitter();
    }
}
