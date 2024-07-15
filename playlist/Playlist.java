package playlist;
import btree.*;
import exceptions.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import listDouble.*;
import avl.*;

public class Playlist {
    BTree<Song> data;
    LinkedCircularDoubleList<String> playlist;
    NodeDouble<String> actual_song;
    AVLTree<EntryNode<Double, String>> order_view;
    int size;
    String order_parameter;
    boolean increasing;

    public Playlist(int order) {
        this.data = new BTree<>(order);
        this.playlist = new LinkedCircularDoubleList<>();
        this.actual_song = null;
        this.order_view = null;
        this.size = 0;
    }

    public void loadSongs(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltar la primera línea (encabezados)
            int count = 0; // Contador de canciones
            while ((linea = br.readLine()) != null && count < 1040000) {
                String[] valores = parseCSVLine(linea);
                String artistName = valores[1].trim();
                String trackName = valores[2].trim();
                String trackId = valores[3].trim();
                int popularity = Integer.parseInt(valores[4].trim());
                int year = Integer.parseInt(valores[5].trim());
                String genre = valores[6].trim();
                double danceability = Double.parseDouble(valores[7].trim());
                double energy = Double.parseDouble(valores[8].trim());
                int key = Integer.parseInt(valores[9].trim());
                double loudness = Double.parseDouble(valores[10].trim());
                int mode = Integer.parseInt(valores[11].trim());
                double speechiness = Double.parseDouble(valores[12].trim());
                double acousticness = Double.parseDouble(valores[13].trim());
                double instrumentalness = Double.parseDouble(valores[14].trim());
                double liveness = Double.parseDouble(valores[15].trim());
                double valence = Double.parseDouble(valores[16].trim());
                double tempo = Double.parseDouble(valores[17].trim());
                double durationMs = Double.parseDouble(valores[18].trim());
                int timeSignature = Integer.parseInt(valores[19].trim());

                Song song = new Song(artistName, trackName, trackId, popularity, year, genre, danceability,
                    energy, key, loudness, mode, speechiness, acousticness, instrumentalness, liveness,
                    valence, tempo, durationMs, timeSignature);

                data.insert(song);
                count++; // Incrementar el contador
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String[] parseCSVLine(String line) {
        String[] values = new String[20]; // Tamaño máximo basado en el ejemplo de 20 parametros
        int index = 0;
        boolean inQuotes = false;
        String current = "";
    
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
    
            if (ch == '"') {
                inQuotes = !inQuotes; // Cambiar estado de comillas
            } else if (ch == ',' && !inQuotes) {
                values[index++] = current; // Agregar valor actual al arreglo
                current = ""; // Reiniciar para el siguiente valor
            } else {
                current += ch; // Construir el valor actual
            }
        }
    
        values[index++] = current; // Agregar el último valor al arreglo
        return trimArray(values, index); // Recortar el arreglo al tamaño correcto
    }
    
    // Método para recortar el arreglo al tamaño correcto
    private String[] trimArray(String[] arr, int size) {
        String[] trimmed = new String[size];
        for (int i = 0; i < size; i++) {
            trimmed[i] = arr[i];
        }
        return trimmed;
    }

    public void insert(String trackId) throws ItemDuplicated, ItemNoFound {
        boolean exist = data.search(new Song(trackId));
        if(exist) {
            boolean duplicated = playlist.search(trackId);
            if(this.size == 0) {
                playlist.insert(trackId);
                this.size++;
            } else if (!duplicated) {
                playlist.insert(trackId);
                this.size++;
            } else {
                throw new ItemDuplicated("Song already exists in the playlist");
            }
        } else {
            throw new ItemNoFound("Song doesn't exist in the database");
        }
    }

    public void remove(String trackId) throws ItemNoFound {
        boolean exist = playlist.search(trackId);
        if(exist) {
            playlist.remove(trackId);
            this.size--;

            if(this.actual_song.getData().equals(trackId))
                this.actual_song = actual_song.getNext();
        } else {
            throw new ItemNoFound("Song doesn't exist in the playlist");
        }
    }

    public void showPlaylist() throws ExceptionIsEmpty {
        if(this.playlist.isEmpty()) 
            throw new ExceptionIsEmpty("Playlist is empty");
        NodeDouble<String> aux_song =  this.playlist.getFirst();

        System.out.println("PLAYLIST: ####################################################################");
        for(int i = 0; i < this.size; i++) {
            Song song = this.data.find(new Song(aux_song.getData()));
            System.out.println("[" + song + "]");
            aux_song  = aux_song .getNext();
        }
        System.out.println("###############################################################################");
    }

    public void changePosition(int a, int b) {
        this.playlist.cambiarOrden(a, b);
    }

    public void play() throws ExceptionIsEmpty {
        if (this.playlist.isEmpty())
            throw new ExceptionIsEmpty("Playlist is empty");
        if(this.actual_song == null)
            this.actual_song = this.playlist.getFirst();
        Song song = this.data.find(new Song(this.actual_song.getData()));
        System.out.println("{PLAY} Reproduciendo: [" + song.getTrackName() + "]");
    }

    public void pause() {
        Song song = this.data.find(new Song(this.actual_song.getData()));
        System.out.println("{PAUSE} Pausado: [" + song.getTrackName() + "]");
    }

    public void next() {
        this.actual_song = this.actual_song.getNext();
        Song song = this.data.find(new Song(this.actual_song.getData()));
        System.out.println("{NEXT} Reproduciendo: [" + song.getTrackName() + "]");
    }

    public void prev() {
        this.actual_song = this.actual_song.getBack();
        Song song = this.data.find(new Song(this.actual_song.getData()));
        System.out.println("{PREV} Reproduciendo: [" + song.getTrackName() + "]");
    }

    public String showDataActualSong() {
        if(this.actual_song == null)
            return "Actual song is null";
        Song song = this.data.find(new Song(this.actual_song.getData()));
        return song.toString();
    }

    public void playRandom() {
        // Implementación pendiente
    }

    public void sortByParameter(String parameter, boolean increasing) {
        AVLTree<EntryNode<Double, String>> avl = new AVLTree<>(increasing);
        NodeDouble<String> aux_song = this.playlist.getFirst();
        this.increasing = increasing;
        switch(parameter) {
            case "popularity":
                for(int i = 0; i < this.size; i++) {
                    Song song = this.data.find(new Song(aux_song.getData()));
                    EntryNode<Double, String> node = new EntryNode<>(song.getPopularity()/1.0, song.getTrackId());
                    avl.insert(node);
                    aux_song = aux_song.getNext();
                    this.order_parameter = "popularity";
                }
                break;
            case "year":
                for(int i = 0; i < this.size; i++) {
                    Song song = this.data.find(new Song(aux_song.getData()));
                    EntryNode<Double, String> node = new EntryNode<>(song.getYear()/1.0, song.getTrackId());
                    avl.insert(node);
                    aux_song = aux_song.getNext();
                    this.order_parameter = "year";
                }
                break;
            case "danceability":
                for(int i = 0; i < this.size; i++) {
                    Song song = this.data.find(new Song(aux_song.getData()));
                    EntryNode<Double, String> node = new EntryNode<>(song.getDanceability(), song.getTrackId());
                    avl.insert(node);
                    aux_song = aux_song.getNext();
                    this.order_parameter = "danceability";
                }
                break;
            case "energy":
                for(int i = 0; i < this.size; i++) {
                    Song song = this.data.find(new Song(aux_song.getData()));
                    EntryNode<Double, String> node = new EntryNode<>(song.getEnergy(), song.getTrackId());
                    avl.insert(node);
                    aux_song = aux_song.getNext();
                    this.order_parameter = "energy";
                }
                break;
            case "loudness":
                for(int i = 0; i < this.size; i++) {
                    Song song = this.data.find(new Song(aux_song.getData()));
                    EntryNode<Double, String> node = new EntryNode<>(song.getLoudness(), song.getTrackId());
                    avl.insert(node);
                    aux_song = aux_song.getNext();
                    this.order_parameter = "loudness";
                }
                break;
            case "durationMs":
                for(int i = 0; i < this.size; i++) {
                    Song song = this.data.find(new Song(aux_song.getData()));
                    EntryNode<Double, String> node = new EntryNode<>(song.getDurationMs(), song.getTrackId());
                    avl.insert(node);
                    aux_song = aux_song.getNext();
                    this.order_parameter = "durationMs";
                }
                break;
        }
        this.order_view = avl;
        viewOrder();
    }

    public void viewOrder() {
        if(this.order_view == null)
            System.out.println("Order parameter not set");
        AVLNode<EntryNode<Double, String>> aux = this.order_view.getRoot();
        System.out.println("Playlist order by {" + this.order_parameter +  "} Increasing {" + this.increasing + "} : #######################");
        viewOrder(this.order_view.getRoot());
        System.out.println("#####################################################");
    }
    
    private void viewOrder(AVLNode<EntryNode<Double, String>> node) {
        if (node != null) {
            viewOrder(node.getLeft());
            Song song = this.data.find(new Song(node.getData().getValor()));
            System.out.println(song.toString());
            viewOrder(node.getRight());
        }
    }
}