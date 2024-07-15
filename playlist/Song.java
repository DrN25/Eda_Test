package playlist;

public class Song implements Comparable<Song> {
    private String trackName;
    private String artistName;
    private String trackId;
    private String popularity;
    private String year;
    private String genre;
    private String danceability;
    private String energy;
    private String key;
    private String loudness;
    private String mode;
    private String speechiness;
    private String acousticness;
    private String instrumentalness;
    private String liveness;
    private String valence;
    private String tempo;
    private String durationMs;
    private String timeSignature;

    public Song(String trackId) {
        this(null, null, trackId, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    public Song(String artistName, String trackName, String trackId, String popularity, String year, String genre,
    String danceability, String energy, String key, String loudness, String mode, String speechiness,
    String acousticness, String instrumentalness, String liveness, String valence, String tempo,
    String durationMs, String timeSignature) {
        this.artistName = artistName;
        this.trackName = trackName;
        this.trackId = trackId;
        this.popularity = popularity;
        this.year = year;
        this.genre = genre;
        this.danceability = danceability;
        this.energy = energy;
        this.key = key;
        this.loudness = loudness;
        this.mode = mode;
        this.speechiness = speechiness;
        this.acousticness = acousticness;
        this.instrumentalness = instrumentalness;
        this.liveness = liveness;
        this.valence = valence;
        this.tempo = tempo;
        this.durationMs = durationMs;
        this.timeSignature = timeSignature;
    }

    public String getTrackId() { return this.trackId; }
    public String getTrackName() { return this.trackName; }

    @Override
    public int compareTo(Song other) {
        return this.trackId.compareTo(other.getTrackId());
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Song song = (Song) obj;
        return trackId.equals(song.getTrackId());
    }

    @Override
    public String toString() {
        return "[" + this.trackName + "; " + this.artistName + "; " + this.trackId  + "; " + this.popularity +
        "; " + this.year + "; " + this.genre + "; " + this.danceability + "; " + this.energy + "; " + this.key +
        "; " + this.loudness + "; " + this.mode + "; " + this.speechiness + "; " + this.acousticness +
        "; " + this.instrumentalness + "; " + this.liveness + "; " + this.valence + "; " + this.tempo +
        "; " + this.durationMs + "; " + this.timeSignature + "]";
        
    }
}