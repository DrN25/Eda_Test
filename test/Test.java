package test;
import playlist.*;

public class Test {
    public static void main(String[] args) {
        Playlist playlist = new Playlist(8);
        playlist.loadSongs("D:\\\\PROGRAMACION\\\\III Semestre\\\\eda\\\\ProyectoFinal\\\\pyfinal-2024-py24_a4\\\\Data\\\\spotify_data.csv");
        try {
            playlist.insert("7sUkziM9Ydla0i8G8WvNiX");
            playlist.insert("6obPXlIOuKuXpGN728oJmT");
            playlist.insert("7fFwjMZOZBaF7nuQmZ3kfc");
            playlist.insert("6og0Eyj8ECKdRiSfy1KzH1");
            playlist.showPlaylist();
            playlist.play();
            playlist.changePosition(0,3);
            playlist.prev();
            playlist.next();
            playlist.pause();
            playlist.next();
            playlist.insert("12r7dx0g78cQ8gPiRwF2c2");
            playlist.insert("65MVsQOcfdeFbFRUYdfFq5");
            playlist.insert("0GK6pzHhJNuxRPOULLPFo6");
            playlist.insert("23FJw5F8nipFrzkZviz5vQ");
            playlist.play();
            playlist.next();
            playlist.showPlaylist();
            playlist.next();
            playlist.play();
            playlist.pause();
            playlist.next();
            playlist.pause();
            playlist.next();
            

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}