import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class GetSetData {


    public String getUsername(){
        File file = new File("Data/Username.txt");
        BufferedReader brfile = null;
        try {
            brfile = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String username;

        try {
            username = brfile.readLine();
            return username;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getFontSize(){
        File file = new File("Data/FontSize.txt");
        BufferedReader brfile = null;
        try {
            brfile = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String fontsize;

        try {
            fontsize = brfile.readLine();
            return Integer.parseInt(fontsize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 12;
    }

    public void setUsername(String text){

        // Defining the file name of the file
        Path fileName = Path.of("Data/Username.txt");

        try {
            Files.writeString(fileName, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFontSize(String text){

        // Defining the file name of the file
        Path fileName = Path.of("Data/FontSize.txt");

        try {
            Files.writeString(fileName, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
