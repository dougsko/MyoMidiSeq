import java.net.*;
import java.io.*;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;


 
public class MyoMidiServer {
    public static void main(String[] args) throws IOException {

    	if (args.length != 1) {
    		System.err.println("Usage: java MyoMidiServer <port number>");
    		System.exit(1);
    	}
 
        int portNumber = Integer.parseInt(args[0]);
        boolean listening = true;
        MidiDevice device = chooseMidiDevice();
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
    
    private static MidiDevice chooseMidiDevice(){
    	MidiDevice device = null;
    	MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
    	String deviceChoice = null;
    	Integer deviceNumber = null;
    	
    	for (int i = 0; i < infos.length; i++) {
    	    System.out.println(i+1 + ". " + infos[i].getDescription());
    	}
    	System.out.println("Please choose a device...");
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	    
    	try {
    		deviceChoice = br.readLine();
    	    deviceNumber = Integer.parseInt(deviceChoice);
    	    deviceNumber--;
    	}
    	catch (IOException ioe) {
    	     System.err.println("IO error trying to read your choice!");
    	     System.exit(-1);
    	}
    	System.out.println("You chose " + infos[deviceNumber].getDescription());
    	try {
    	    device = MidiSystem.getMidiDevice(infos[deviceNumber]);
    	} catch (MidiUnavailableException e) {
    		System.err.println("Midi System Unavailable Exception");
    	  	System.exit(-1);
    	}
    	return device;
    }
}
