package org.mdyk.sensesim.app;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/** Example of displaying a splash page for a standalone JavaFX application */
public class SplashScreen extends Application {
    private Pane splashLayout;
    private WebView webView;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 676;
    private static final int SPLASH_HEIGHT = 227;

    public static void main(String[] args) throws Exception { launch(args); }

    @Override public void init() {
       /* ImageView splash = null;
        try {
            splash = new ImageView(new Image(new FileInputStream("C:\\Users\\Michal\\Desktop\\SenseSim-logo2.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash);
        splashLayout.setStyle("-fx-padding: 5; -fx-background-color: #ffffff; -fx-border-width:5; -fx-border-color: linear-gradient(to bottom, rgba(255,255,255,0), derive(#ffffff, 80%));");
//        splashLayout.setEffect(new DropShadow());         */
    }

    @Override public void start(final Stage initStage) throws Exception {
        showSplash(initStage);
        showMainStage();
//
//        webView.getEngine().documentProperty().addListener(new ChangeListener<Document>() {
//            @Override public void changed(ObservableValue<? extends Document> observableValue, Document document, Document document1) {
//                if (initStage.isShowing()) {
//                    loadProgress.progressProperty().unbind();
//                    loadProgress.setProgress(1);
//                    progressText.setText("All hobbits are full.");
//                    mainStage.setIconified(false);
//                    initStage.toFront();
//                    FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
//                    fadeSplash.setFromValue(1.0);
//                    fadeSplash.setToValue(0.0);
//                    fadeSplash.setOnFinished(new EventHandler<ActionEvent>() {
//                        @Override public void handle(ActionEvent actionEvent) {
//                            initStage.hide();
//                        }
//                    });
//                    fadeSplash.play();
//                }
//            }
//        });
    }

    private void showMainStage() {
        mainStage = new Stage(StageStyle.DECORATED);
        mainStage.setTitle("FX Experience");
        mainStage.setIconified(true);

        // create a WebView.
        webView = new WebView();
        webView.getEngine().load("http://fxexperience.com/");

        // layout the scene.
        Scene scene = new Scene(webView, 1000, 600);
        webView.prefWidthProperty().bind(scene.widthProperty());
        webView.prefHeightProperty().bind(scene.heightProperty());
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void showSplash(Stage initStage) {
        Scene splashScene = new Scene(splashLayout);
        initStage.initStyle(StageStyle.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.show();
    }
}