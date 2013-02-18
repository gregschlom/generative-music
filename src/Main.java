import edu.columbia.stat.wood.pub.sequencememoizer.IntSequenceMemoizer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main
{
    public static void main(String[] args) throws InvalidMidiDataException, IOException
    {
        testMidi();
        //testMemoizer();
    }

    private static void testMidi() throws InvalidMidiDataException, IOException
    {
        // Note: for this to work, make sure the current working dir is properly set to the the root of the project
        File midiFile = new File("midi/vivaldi_andante.mid");

        Sequence seq = MidiSystem.getSequence(midiFile);
        System.out.println("Tracks: " + Arrays.toString(seq.getTracks()));
        System.out.println(seq.getTracks()[0].get(0).getMessage().getLength());
    }

    private static void testMemoizer()
    {
        IntSequenceMemoizer seq = new IntSequenceMemoizer();
        int[] input = {1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,1,2,3,4,5,6,7,8,1,2,5,10,20,40,80,100};
        seq.newSequence();
        seq.continueSequence(input);
        int[] result = seq.generateSequence(new int[]{1,2,4}, 3);
        System.out.println("result: " + Arrays.toString(result));
    }
}
