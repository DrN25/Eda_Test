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
    int size;
    boolean playing_song;

    AVLTree<EntryNode<Double, String>> order_view;
    String order_parameter;
    boolean increasing;

    public Playlist(int order) {
        this.data = new BTree<>(order);
        this.playlist = new LinkedCircularDoubleList<>();
        this.actual_song = null;
        this.order_view = null;
        this.size = 0;
        this.playing_song = false;
    }
    // Metodo para cargar la informacion de las canciones del CSV al BTree
    public void loadSongs(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            br.readLine(); // Saltar la primera línea (encabezados)
            while ((linea = br.readLine()) != null) {
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
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    // Este metodo analiza la linea entregada, devolviendo el array resultante. Ademas, evita errores relacionados a "," dentro del trackName
    private String[] parseCSVLine(String line) {
        String[] values = new String[20]; // Tamaño máximo basado en el ejemplo de 20 parametros
        int index = 0;
        boolean inQuotes = false;   // Indica si la lectura actual esta dentro de comillas dobles
        String current = "";
        
        //Si un campo del csv tiene "," dentro de si, este sera colocado entre "" para poder analizarlo correctamente
        for(int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
    
            if (c == '"') {
                inQuotes = !inQuotes; // Si se encuentran comillas, entonces las "," no agregaran el elemento hasta salir de las comillas 
            } else if (c == ',' && !inQuotes) {
                values[index++] = current; // Agregar valor actual al arreglo
                current = ""; // Reiniciar para el siguiente valor
            } else {
                current += c; // Agrega el valor actual
            }
        }
    
        values[index++] = current; // Agregar el último valor al arreglo
        return values;
    }

    // Metodo que inserta el trackId entregado en la Playlist
    public void insert(String trackId) throws ItemDuplicated, ItemNoFound {
        if(!data.search(new Song(trackId))) {    // No se permite insertar canciones que no existan en la base de datos
            throw new ItemNoFound("Song doesn't exist in the database");
        } else {
            boolean duplicated = playlist.search(trackId);
            if(duplicated) {    // No se permite insertar canciones duplicadas
                throw new ItemDuplicated("Song already exists in the playlist");
            } else {       // Caso contrario, inserta la cancion a la playlist
                playlist.insert(trackId);
                this.size++;
            }
        }
    }

    // Metodo que elimina el trackId entregado en la Playlist
    public void remove(String trackId) throws ItemNoFound {
        if(!playlist.search(trackId)) {    // No se permite eliminar canciones que no existan en la playlist
            throw new ItemNoFound("Song doesn't exist in the playlist");
        } else {    // Caso contrario, elimina la cancion de la playlist
            playlist.remove(trackId);
            this.size--;

            if(this.actual_song.getData().equals(trackId))      // Si la cancion eliminada era referenciada por actual_song, se pasa al siguiente nodo
                this.actual_song = actual_song.getNext();
        }
    }

    // Metodo que imprime las canciones de la playlist actual
    public void showPlaylist() throws ExceptionIsEmpty {
        if(this.playlist.isEmpty())     // No se permite mostrar una playlist vacia
            throw new ExceptionIsEmpty("Playlist is empty");

        NodeDouble<String> aux_song =  this.playlist.getFirst();    // Se crea un nodo auxiliar que ayudara a recorrer toda la playlist

        System.out.println("PLAYLIST: ####################################################################");
        for(int i = 0; i < this.size; i++) {
            Song song = this.data.find(new Song(aux_song.getData()));
            System.out.println("[" + song + "]");
            aux_song  = aux_song .getNext();
        }
        System.out.println("###############################################################################");
    }

    // Metodo que cambia de posicion las canciones
    public void changePosition(int a, int b) {
        this.playlist.cambiarOrden(a, b);       // El cambio se realiza en el metodo cambiarOrden() de LinkedCircularDoubleList
    }

    // Metodo que simula la reproduccion o pausa de la cancion actual de la playlist
    public void play_pause() throws ExceptionIsEmpty {
        if(this.playlist.isEmpty())    // No se permite reproducir o pausar la cancion si la playlist esta vacia
            throw new ExceptionIsEmpty("Playlist is empty");

        if(this.actual_song == null)    // Si la cancion actual es null, se le asigna la primera posicion de la playlist
            this.actual_song = this.playlist.getFirst();

        Song song = this.data.find(new Song(this.actual_song.getData()));
        if(this.playing_song)  {         // Se verifica si la cancion actual se esta reproduciendo o no
            this.playing_song = true;
            System.out.println("{PLAY} Reproduciendo: [" + song.getTrackName() + "]");      // Simulacion reproduccion de la cancion actual
        } else {
            this.playing_song = false;
            System.out.println("{PAUSE} En pausa: [" + song.getTrackName() + "]");      // Simulacion pausa de la cancion actual
        }
    }

    public void next() {
        this.actual_song = this.actual_song.getNext();
        this.playing_song = true;
        Song song = this.data.find(new Song(this.actual_song.getData()));
        System.out.println("{NEXT} Reproduciendo: [" + song.getTrackName() + "]");
    }

    public void prev() {
        this.actual_song = this.actual_song.getBack();
        this.playing_song = true;
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
        if(this.order_view == null) {
            System.out.println("Order parameter not set");
            return;
        }
        AVLNode<EntryNode<Double, String>> aux = this.order_view.getRoot();
        System.out.println("Playlist order by {" + this.order_parameter +  "} Increasing {" + this.increasing + "} : #######################");
        viewOrder(this.order_view.getRoot());
        System.out.println("#####################################################");
    }
    
    public void viewOrder(AVLNode<EntryNode<Double, String>> node) {
        if (node != null) {
            viewOrder(node.getLeft());
            Song song = this.data.find(new Song(node.getData().getValor()));
            System.out.println(song.toString());
            viewOrder(node.getRight());
        }
    }

    public void viewToPlaylist() {
        if(this.order_view == null) {
            System.out.println("Order parameter not set");
            return;
        }
        this.playlist.clear();
        viewToPlaylist(this.order_view.getRoot());
    }

    public void viewToPlaylist(AVLNode<EntryNode<Double, String>> node) {
        if (node != null) {
            viewToPlaylist(node.getLeft());
            this.playlist.insert(node.getData().getValor());
            viewToPlaylist(node.getRight());
        }
    }
}