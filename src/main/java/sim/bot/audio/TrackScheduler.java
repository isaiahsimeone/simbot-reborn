package sim.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.*;

public class TrackScheduler extends AudioEventAdapter {

    /**
     * The audio player that this class schedules tracks for
     */
    private final AudioPlayer player;
    /**
     * Holds the current queue of tracks to be played
     */
    public Queue<AudioTrack> queue;

    /**
     * The TrackScheduler class is responsible for managing the playback
     * of AudioTracks for the AudioPlayer it is constructed with.
     *
     * @param player The AudioPlayer for which this class schedules songs
     */
    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    /**
     * Push an AudioTrack object to the end of the queue
     * @param track The AudioTrack to be pushed
     */
    public void enqueue(AudioTrack track) {
        if (!player.startTrack(track, true) && queue.size() <= 1000)
            queue.offer(track);
    }

    /**
     * Start playing the next track in the queue of AudioTracks
     */
    public void next_track() {
        player.startTrack(queue.poll(), false);
    }

    /**
     * Play the next track in the queue if permitted
     * @param player Audio player
     * @param track Audio track that ended
     * @param endReason The reason why the track stopped playing
     */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext)
            next_track();
    }

    /**
     * Empty the queue of AudioTracks
     */
    public void dump_queue() {
        queue.clear();
    }

    /**
     * Shuffle the AudioTrack objects in the queue
     */
    public void shuffle() {
        List<AudioTrack> tmp = new ArrayList<>(queue);
        Collections.shuffle(tmp);
        queue.clear();
        queue.addAll(tmp);
    }

    /**
     * Get a queue of tracks to be played
     * @return A queue of AudioTrack objects
     */
    public Queue<AudioTrack> get_track_queue() {
        return queue;
    }
}
