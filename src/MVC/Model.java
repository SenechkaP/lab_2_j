package MVC;

import cocktails.BaseCocktail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

public class Model {
    private int id_to_add = 0;

    public void updateID() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("database.txt"));
        while (reader.readLine() != null) {
            id_to_add++;
        }
    }

    public int getSize() {
        return id_to_add;
    }

    public JSONObject findUser(String userName) throws IOException, ParseException {
        Object o = new JSONParser().parse(new FileReader("src/users.json"));
        JSONArray jsonArray = (JSONArray) o;

        for (Object obj : jsonArray) {
            JSONObject user = (JSONObject) obj;
            String name = (String) user.get("name");

            if (Objects.equals(name, userName)) {
                return user;
            }
        }
        return null;
    }

    public List<String> readLines() throws IOException {
        return Files.readAllLines(Paths.get("database.txt"));
    }

    public String addCocktail(BaseCocktail cocktail) throws IOException {
        if (cocktail == null) {
            return null;
        }

        FileOutputStream out = new FileOutputStream("database.txt", true);
        OutputStreamWriter out2 = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        String str = id_to_add + ";" + cocktail.getClass().getSimpleName() + ";" + cocktail.getRecipe() + "\n";
        out2.write(str);
        out2.close();
        id_to_add += 1;

        return str;
    }
}
