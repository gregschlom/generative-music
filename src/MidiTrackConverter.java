import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import sun.plugin2.message.SetAppletSizeMessage;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;
import java.util.*;

public class MidiTrackConverter
{
    public Note[] convert(Track track) throws Exception
    {
        Multimap<Integer, Integer> pendingEvents = ArrayListMultimap.create();
        ArrayList<Note> notes = new ArrayList<Note>();

        for (int i = 0; i < track.size(); i++) {
            Event event = new Event(track.get(i));
            System.out.println("Event: " + event);

            switch (event.type) {
                case NoteOn:
                    Note note = new Note(event);
                    notes.add(note);
                    pendingEvents.put(event.getHash(), notes.size() - 1);
                    break;
                case NoteOff:

                    System.out.println("EVENTS IN THE HASH " + Arrays.toString(pendingEvents.keySet().toArray()));

                    if (!pendingEvents.containsKey(event.getHash())) throw new Exception("note off without note on: ");
                    int index = Iterables.getFirst(pendingEvents.get(event.getHash()), -1);
                    assert index >= 0;
                    pendingEvents.remove(event.getHash(), index);
                    notes.get(index).setEndTick(event.tick);
                    break;
            }
        }

        return notes.toArray(new Note[notes.size()]);
    }

    private static int high4(int b)
    {
        return b >> 4;
    }

    private static int low4(int b)
    {
        return b & 0xF;
    }

    public static class Event
    {
        private enum Type { Unknown, NoteOn, NoteOff }
        public final Type type;
        public final int key;
        public final int velocity;
        public final long tick;
        public final int channel;

        public Event(MidiEvent event)
        {
            tick = event.getTick();
            byte[] data = event.getMessage().getMessage();

            int t = high4(toInt(data[0]));
            type = (t == 9) ? Type.NoteOn : (t == 8) ? Type.NoteOff : Type.Unknown;

            channel = low4(toInt(data[0]));

            if (type == Type.NoteOn || type == Type.NoteOff) {
                key = toInt(data[1]);
                velocity = toInt(data[2]);
            } else {
                key = velocity = 0;
            }
        }

        private static int toInt(byte b)
        {
            return b & 0xFF;
        }

        public int getHash()
        {
            assert key > 0 && key < 128 : key;
            assert channel >= 0 && channel < 16 : channel;

            return (channel << 7) + key ;
        }

        @Override
        public String toString() {
            return type + "(" + key + ":" + channel + ")";
        }
    }

}
