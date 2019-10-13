package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Controller {
    public Controller(){}

    public String leerArchivo(){
        try(FileReader reader = new FileReader("src/application/resources/Usuarios.txt")){
            int character;
            StringBuilder s = new StringBuilder();
            while ((character = reader.read()) != -1)
                s.append((char) character);
            reader.close();
            return s.toString();
        } catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    public static void escribirArchivo(String usuario_fecha) throws Exception{
        FileWriter writer = new FileWriter("src/application/resources/Usuarios.txt", false);
        writer.write(usuario_fecha);
        writer.close();
    }
}
