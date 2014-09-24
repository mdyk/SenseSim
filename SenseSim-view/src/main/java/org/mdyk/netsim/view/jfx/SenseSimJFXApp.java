package org.mdyk.netsim.view.jfx;


import com.google.inject.Singleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.mdyk.netsim.view.SenseSimView;

@Singleton
public class SenseSimJFXApp extends Application implements Runnable, SenseSimView {

    private static final Logger logger = Logger.getLogger(SenseSimJFXApp.class);

    @Override
    public void start(Stage stage) throws Exception {
        logger.debug(">> SesnseSim start");
        String fxmlFile = "/fxml/SenseSim.fxml";
        logger.debug("Loading FXML for main view from: " + fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        logger.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("SenseSim");
        stage.setScene(scene);
        stage.show();
        logger.debug("<< SesnseSim start");
    }

    @Override
    public void run() {
        launch();
    }

    @Override
    public void show() {
        SenseSimJFXApp  senseSimJFXApp = new SenseSimJFXApp();
        new Thread(senseSimJFXApp).start();
    }
}
