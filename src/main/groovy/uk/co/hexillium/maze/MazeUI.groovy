package uk.co.hexillium.maze

import groovy.transform.CompileStatic
import javafx.application.Application
import javafx.application.Platform
import javafx.event.Event
import javafx.event.EventType
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.scene.input.KeyEvent
import javafx.scene.layout.Pane
import javafx.stage.FileChooser
import javafx.stage.Screen
import javafx.stage.Stage
import javafx.stage.StageStyle

import javax.imageio.ImageIO
import java.awt.image.BufferedImage

@CompileStatic
class MazeUI extends Application {

    MazeUI(){}
//
//    static void main(String[] args) {
//        launch(args)
//    }

    @Override
    void start(Stage primaryStage) throws Exception {

        MazeImpl.testRGBIsWall(0x000000)

        var chooser = new FileChooser();
        chooser.title = "Select image to use"
        chooser.initialDirectory = new File(System.getProperty("user.home"));
        var filter = new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg");
        chooser.extensionFilters.add(filter)
        chooser.setSelectedExtensionFilter(filter)
        var chosen = chooser.showOpenDialog(null)
        if (chosen == null){
            Platform.exit()
            return
        }

        var screen = Screen.getPrimary().getBounds()
        var canvas = new Canvas(screen.getWidth(), screen.getHeight())
        var pane = new Pane(canvas)
        var scene = new Scene(pane)
        primaryStage.setFullScreenExitKeyCombination KeyCombination.NO_MATCH
//        primaryStage.setFullScreenExitHint null
        primaryStage.setScene scene
        primaryStage.initStyle StageStyle.UNDECORATED
        primaryStage.setFullScreen true

        primaryStage.addEventHandler KeyEvent.KEY_PRESSED, {if (it.code == KeyCode.ESCAPE) Platform.exit()}

        primaryStage.show()

        var image = ImageIO.read chosen;

        MazeImpl mI = new MazeImpl(image)

        mI.drawMaze(canvas.getGraphicsContext2D())


    }

    private static Image convertToImage(BufferedImage bim){

    }
}
