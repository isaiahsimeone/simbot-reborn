package sim.bot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.*;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    public Queue<AudioTrack> queue;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
        this.queue = new LinkedList<>();
    }

    public void enqueue(AudioTrack track) {
        if (!player.startTrack(track, true) && queue.size() <= 1000)
            queue.offer(track);
    }

    public void next_track() {
        player.startTrack(queue.poll(), false);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext)
            next_track();
    }

    public void dump_queue() {
        queue.clear();
    }

    public void shuffle() {
        List<AudioTrack> tmp = new ArrayList<>(queue);
        Collections.shuffle(tmp);
        queue.clear();
        queue.addAll(tmp);
    }

    public Queue<AudioTrack> get_track_queue() {
        return queue;
    }
}
