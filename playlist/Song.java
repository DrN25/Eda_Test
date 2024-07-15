package playlist;

public class Song implements Comparable<Song> {
    private String trackName;
    private String artistName;
    private String trackId;
    private int popularity;
    private int year;
    private String genre;
    private double danceability;
    private double energy;
    private int key;
    private double loudness;
    private int mode;
    private double speechiness;
    private double acousticness;
    private double instrumentalness;
    private double liveness;
    private double valence;
    private double tempo;
    private double durationMs;
    private int timeSignature;

    public Song(String trackId) {
        this(null, null, trackId, 0, 0, null, 0.0, 0.0, 0, 0.0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0);
    }

    public Song(String artistName, String trackName, String trackId, int popularity, int year, String genre,
    double danceability, double energy, int key, double loudness, int mode, double speechiness,
    double acousticness, double instrumentalness, double liveness, double valence, double tempo,
    double durationMs, int timeSignature) {
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
    public String getArtistName() { return this.artistName; }
    public int getPopularity() { return this.popularity; }
    public int getYear() { return this.year; }
    public double getDanceability() { return this.danceability; }
    public double getEnergy() { return this.energy; }
    public double getLoudness() { return this.loudness; }
    public double getDurationMs() { return this.durationMs; }

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
        return "[trackName: " + this.trackName + "; artistName: " + this.artistName + "; trackId: " + this.trackId  + "; popularity: " + this.popularity +
        "; year: " + this.year + "; genre: " + this.genre + "; danceability: " + this.danceability + "; energy: " + this.energy + "; key: " + this.key +
        "; loudness: " + this.loudness + "; mode: " + this.mode + "; speechiness: " + this.speechiness + "; acousticness: " + this.acousticness +
        "; instrumentalness: " + this.instrumentalness + "; liveness: " + this.liveness + "; valence: " + this.valence + "; tempo: " + this.tempo +
        "; durationMs: " + this.durationMs + ";  timeSignature: " + this.timeSignature + "]";
        
    }
}