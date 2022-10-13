package org.geotools.javafx;


import java.awt.GraphicsEnvironment;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/** Bringing up the first map */
public class MapViewer extends Application {
	
	static {
		// Make sure this is called before adding layers to the map
		GraphicsEnvironment.getLocalGraphicsEnvironment();
	}

	private final static int WIDTH = 1024;
	private final static int HEIGHT = 768;
	
	private static MapViewer instance;
	
	private Thread launcher;
	
	public synchronized static MapViewer getInstance() throws InterruptedException {
		if (instance == null) {
			Thread launcher = new MapLauncherThread();
			launcher.start();
			while (instance == null) {
				Thread.sleep(100);
			}
			instance.launcher = launcher;
		}
		return instance;
	}
	
	private MapCanvas canvas;
	
	@Override
    public void start(Stage primaryStage) { 
        try { 
            canvas = new MapCanvas(WIDTH, HEIGHT);

            StackPane pane = new StackPane();
            pane.setBackground(new Background(new BackgroundFill(Color.WHITE, 
            										CornerRadii.EMPTY, 
            										Insets.EMPTY)));
            pane.getChildren().add(canvas);
            
            canvas.widthProperty().bind(pane.widthProperty());
            canvas.heightProperty().bind(pane.heightProperty());
            
            Scene scene = new Scene(pane, WIDTH, HEIGHT);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Map Viewer");
            primaryStage.show();
            
            instance = this;
            
        } catch (Exception e) { 
            e.printStackTrace(); 
        } 
    }
	
	@Override
	public void stop() {
		instance = null;
	}
	
	public void close() {
		Platform.exit();
	}
	
	/** This will hang until MapViewer is closed */
	public void join() throws InterruptedException {
		launcher.join();
	}
	
	public MapCanvas getMap() {
		if (canvas == null) {
			throw new IllegalStateException("You should call start prior to getMap");
		}
		return canvas;
	}
	
	public static void main(String... args) {
		launch(args);
	}
	
    
    private static class MapLauncherThread extends Thread {
    	
    	MapLauncherThread() {
    		super("Map Viewer Thread");
    	}
    	
    	@Override
    	public void run() {
    		MapViewer.main();
    	}
    }
}
