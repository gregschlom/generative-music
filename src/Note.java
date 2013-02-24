public class Note
{
    private final long tick;
    private final int key;
    private final int velocity;
    private long duration; // in ticks

    private static final String[] KEY_NAMES = {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};

    public Note(MidiTrackConverter.Event event)
    {
        tick = event.tick;
        key = event.key;
        velocity = event.velocity;
    }

    public void setEndTick(long endTick)
    {
        duration = endTick - tick;
    }

    @Override
    public String toString()
    {
        String stringKey = KEY_NAMES[key % 12] + (int)(key / 12);
        return stringKey + "(" + tick + ")";
    }
}
