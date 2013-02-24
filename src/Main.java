import edu.columbia.stat.wood.pub.sequencememoizer.IntSequenceMemoizer;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        Main main = new Main();
        main.testMidi();
        //testMemoizer();
        System.out.println("Fin du porg");
    }

    private void testMidi() throws Exception
    {
        // Note: for this to work, make sure the current working dir is properly set to the the root of the project
        File midiFile = new File("midi/teddybear.mid");

        Sequence seq = MidiSystem.getSequence(midiFile);
        Track[] tracks = seq.getTracks();
        System.out.println("Tracks: " + Arrays.toString(seq.getTracks()));

        System.out.println("Notes: " + Arrays.toString(new MidiTrackConverter().convert(tracks[1])));
    }

    private String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) {
            result.append(String.format("%02X ", b));
            result.append(" "); // delimiter
        }
        return result.toString();
    }

    private void playSequence(Sequence seq) throws InvalidMidiDataException, MidiUnavailableException, InterruptedException{
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setSequence(seq);
        sequencer.start();

        Thread.sleep(1000);
        sequencer.stop();
        sequencer.close();
    }

    private void testMemoizer()
    {
        IntSequenceMemoizer seq = new IntSequenceMemoizer();
        int[] input = {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,1,2,5,10,20,40,80,100};
        seq.newSequence();
        seq.continueSequence(input);
        int[] result = seq.generateSequence(new int[]{1,2,3,4,5,6,7,8}, 3);
        System.out.println("result: " + Arrays.toString(result));
    }
}
