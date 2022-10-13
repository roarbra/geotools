package org.geotools.javafx;

import java.awt.Rectangle;
import java.awt.Color;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.jfree.fx.FXGraphics2D;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

/**
 * JavaFX Canvas specific for holding a GeoTools MapContent.
 * 
 * @author Roar Brænden
 *
 */
public class MapCanvas extends Canvas {
	
	private MapContent map;
	
	private boolean repaint = true;
	
	
	MapCanvas(int width, int height) { 
	    super(width, height);
	    
	    initMap();	    
	    initEvent();
	    initPaintThread();
	}

	@Override
	public boolean isResizable() {
		return true;
	}
	
	
	private void initMap() {
		map = new MapContent();
		map.setTitle("Quickstart");
	}
	
	/** Adds the given layer, and sets repaint to true in order to update map at next cycle. */
	public void addLayer(Layer layerToAdd) {
		map.addLayer(layerToAdd);
		if (map.getViewport().isEmpty()) {
			map.getViewport().setBounds(layerToAdd.getBounds());
		}
		map.getViewport().setScreenArea(new Rectangle((int) getWidth(), (int) getHeight()));
		repaint = true;
	}

	/** repaint must be set. A new StreamingRenderer is used */
	private void drawMap() {
		if (!repaint) return;
		
		try {
			final GraphicsContext gc = getGraphicsContext2D();
			final int width = (int) getWidth();
			final int height = (int) getHeight();
			
			StreamingRenderer draw = new StreamingRenderer();
			draw.setMapContent(map);
			
			FXGraphics2D graphics = new FXGraphics2D(gc);
			graphics.setBackground(Color.WHITE);
			graphics.clearRect(0, 0, width, height);
			
			if (!map.getViewport().isEmpty()) {
				draw.paint(graphics, new Rectangle(width, height), map.getViewport().getBounds());
			}
			repaint = false;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private double baseDrageX;
	private double baseDrageY;
	/*
	 * initial for mouse event
	 */
	private void initEvent() {
	    /*
	     * setting the original coordinate
	     */
	    addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
	 
	        @Override
	        public void handle(MouseEvent e) {
	            baseDrageX = e.getSceneX();
	            baseDrageY = e.getSceneY();
	            e.consume();
	        }
	    });
	    /*
	     * translate according to the mouse drag
	     */
	    addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent e) {
	        	DirectPosition2D mapPrev = new DirectPosition2D();
	        	map.getViewport().getScreenToWorld().transform(new DirectPosition2D(baseDrageX, baseDrageY), mapPrev);
	        	
	            baseDrageX = e.getSceneX();               // Set for next drag
	            baseDrageY = e.getSceneY();
	            DirectPosition2D mapAfter = new DirectPosition2D();
	            map.getViewport().getScreenToWorld().transform(new DirectPosition2D(baseDrageX, baseDrageY), mapAfter);
	            
	            double difX = mapPrev.getX() - mapAfter.getX();
	            double difY = mapPrev.getY() - mapAfter.getY();
	            
	            ReferencedEnvelope env = new ReferencedEnvelope(map.getViewport().getBounds());
	            env.translate(difX, difY);
	            doSetDisplayArea(env);
	            
	            e.consume(); 
	        }
	    });
	    /*
	     * double clicks to restore to original map
	     */
	    addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent t) {
	            if (t.getClickCount() > 1) {
	                doSetDisplayArea(map.getMaxBounds());
	            }
	            t.consume();
	        }
	    });
	    /*
	     * scroll for zoom in and out
	     */
	    addEventHandler(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
	 
	        @Override
	        public void handle(ScrollEvent e) {
	            ReferencedEnvelope envelope = map.getViewport().getBounds();
	            double percent = e.getDeltaY() / getWidth();
	            double width = envelope.getWidth();
	            double height = envelope.getHeight();
	            double deltaW = width * percent;
	            double deltaH = height * percent;
	            envelope.expandBy(deltaW, deltaH);
	            doSetDisplayArea(envelope);
	            e.consume();
	        }
	    });
	    
	    widthProperty().addListener(evt -> changeCanvasSize());
	    heightProperty().addListener(evt -> changeCanvasSize());
	}
	 
	protected void doSetDisplayArea(ReferencedEnvelope envelope) {
	    map.getViewport().setBounds(envelope);
	    repaint = true;
	}
	
	private void changeCanvasSize() {
		
		final double newWidth = getWidth();
		final double newHeight = getHeight();
		
		map.getViewport().setScreenArea(new Rectangle((int) newWidth, (int) newHeight));	
		repaint = true;
	}

	private void initPaintThread() {
	    new DrawMapService().withPeriod(1000).start();
	}
	
	class DrawMapService extends ScheduledService<Boolean> {

		private final double PAINT_HZ = 60.0;
		
		DrawMapService withPeriod(long millis) {
			this.setPeriod(Duration.millis(millis / PAINT_HZ));
			return this;
		}
		
		
		protected Task<Boolean> createTask() {
            return new Task<Boolean>() {
                protected Boolean call() {
                    Platform.runLater(() -> {
                        drawMap();
                    });
                    return true;
                }
            };
        }	
	}
}
