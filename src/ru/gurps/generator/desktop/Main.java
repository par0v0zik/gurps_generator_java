package ru.gurps.generator.desktop;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;

import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main extends Application {
    public static final String VERSION = "v0.2.2";
    public static final ResourceBundle locale = ResourceBundle.getBundle("bundles.generator", new Locale("ru", "RU"));
    public static String jarFolder;
    public static String protocol;
    public static String serverAddress;
    public static int updateStart;
    public static int serverPort;

    @Override
    public void start(Stage primaryStage) throws Exception {
        String parent = "\\w*.jar";
        jarFolder = URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()
                .replaceAll(parent, ""), "UTF-8");

        File file = new File(jarFolder + "db/gurps.mv.db");

        if (!file.exists() || file.isDirectory()) {
            File dir = new File(jarFolder + "db");
            if (!dir.exists() || !dir.isDirectory()) dir.mkdir();
            ExportResource("/db/gurps.mv.db");
        }

        loadProperties();
        AbstractController.stage = primaryStage;
        checkNewVersion();
        charactersStage();
    }

    static public void checkNewVersion() throws IOException, URISyntaxException {
        try {
            URL url = new URL("https://api.github.com/repos/jokius/gurps_generator_java/tags");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) return;

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            String response = br.readLine();
            conn.disconnect();

            JsonArray json = new JsonParser().parse(response).getAsJsonArray();
            String last_version = json.get(0).getAsJsonObject().get("name").getAsString();
            if (!VERSION.equals(last_version)) {
                AbstractController.urlToLastVersion = new URI("https://github.com/jokius/gurps_generator_java/releases/tag/" +
                        last_version);
            }
        } catch (UnknownHostException ignored) {
        }
    }

    protected void charactersStage() {
        AbstractController.stage.setResizable(false);
        AbstractController.stage.setMinWidth(397);
        AbstractController.stage.setMinHeight(293);
        AbstractController.stage.setWidth(397);
        AbstractController.stage.setHeight(293);
        FXMLLoader view = new FXMLLoader(Main.class.getResource("resources/views/characters/select.fxml"));
        view.setResources(locale);
        try {
            Parent root = view.load();
            AbstractController.stage.setScene(new Scene(root));
            AbstractController.stage.setTitle(locale.getString("app_name_character_select"));
            AbstractController.stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    static public String ExportResource(String resourceName) throws Exception {
        InputStream stream;
        OutputStream resStreamOut;
        String jarFolder;
        stream = Main.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
        if (stream == null) {
            throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
        }
        int readBytes;
        byte[] buffer = new byte[4096];
        jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                .getParentFile().getPath().replace('\\', '/');
        resStreamOut = new FileOutputStream(jarFolder + resourceName);
        while ((readBytes = stream.read(buffer)) > 0) {
            resStreamOut.write(buffer, 0, readBytes);
        }
        stream.close();
        resStreamOut.close();

        return jarFolder + resourceName;
    }


    public void loadProperties() {
        Properties props = new Properties();
        File file = new File(jarFolder + "properties/all.properties");
        if (!file.exists() || file.isDirectory()) saveDefaultProperties();
        try {
            props.load(new FileInputStream(file));
            protocol = props.getProperty("Protocol");
            serverAddress = props.getProperty("ServerAddress");
            serverPort = new Integer(props.getProperty("ServerPort"));
            updateStart = new Integer(props.getProperty("UpdateStart"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultProperties() {
        try {
            File dir = new File(jarFolder + "properties");
            if (!dir.exists() || !dir.isDirectory()) dir.mkdir();
            Properties props = new Properties();
            props.setProperty("Protocol", "http");
            props.setProperty("ServerAddress", "gurps.cloudapp.net");
            props.setProperty("ServerPort", "80");
            props.setProperty("UpdateStart", "16");
            File f = new File(jarFolder + "properties/all.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out, "This default properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveProperties() {
        try {
            Properties props = new Properties();
            props.setProperty("Protocol", protocol);
            props.setProperty("ServerAddress", serverAddress);
            props.setProperty("ServerPort", "" + serverPort);
            props.setProperty("UpdateStart", "" + updateStart);
            File f = new File(jarFolder + "properties/all.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out, "This default properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}