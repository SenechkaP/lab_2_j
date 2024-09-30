package MVC;

import cocktails.BaseCocktail;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Model {
    private int id_to_add = 0;

    public void updateID() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader("database.txt"));
        while (reader.readLine() != null) {
            id_to_add++;
        }
    }

    public JSONObject findUser(String userName) {
        try {
            Object o = new JSONParser().parse(new FileReader("src/users.json"));
            JSONArray jsonArray = (JSONArray) o;

            for (Object obj : jsonArray) {
                JSONObject user = (JSONObject) obj;
                String name = (String) user.get("name");

                if (Objects.equals(name, userName)) {
                    return user;
                }
            }

        } catch (IOException | ParseException e) {
            System.out.println("обрабатываем ошибку"); // TODO ошибка при чтении json
        }
        return null;
    }

    public boolean addCocktail(BaseCocktail cocktail) {
        if (cocktail == null) {
            return false;
        }
        try {
            FileOutputStream out = new FileOutputStream("database.txt", true);
            OutputStreamWriter out2 = new OutputStreamWriter(out, StandardCharsets.UTF_8);
            String str = id_to_add + ";" + cocktail.getClass().getSimpleName() + ";" + cocktail.getStrength() + ";" + cocktail.getTotalAmount() + ";" + cocktail.getRecipe() + "\n";
            out2.write(str);
            out2.close();
            id_to_add += 1;
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
