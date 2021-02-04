package fs.csa4j.visualization;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import fs.csa4j.blocks.Entity;
import fs.csa4j.visualization.ModelInfo.VisualizableBlock.Point;
import lombok.Data;

@Data
public class JFrameCanvas implements Canvas {

    private final JFrame frame;
    private final JPanel panel;
    private final Map<String, URI> shownImages = new LinkedHashMap<>(); // key -> image-url
    private final Map<URI, BufferedImage> loadedImages = new HashMap<>(); // image-url -> image-data
    private final Map<String, Point> locations = new HashMap<>(); // key -> location

    public JFrameCanvas(URL backgroundImageUrl) {
        System.setProperty("sun.java2d.opengl", "true"); // https://stackoverflow.com/questions/41001623/java-animation-programs-running-jerky-in-linux
        frame = new JFrame("csa4j");
        frame.setBounds(100, 100, 100, 100);
        frame.toFront();
        panel = new JPanel(true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (var entry : shownImages.entrySet()) {
                    var image = loadedImages.get(entry.getValue());
                    var location = locations.get(entry.getKey());
                    g.drawImage(image, location.getX(), location.getY(), null);
                }
            }
        };
        var backgroundImage = loadBackgroundImage(toURI(backgroundImageUrl));
        panel.setPreferredSize(new DimensionUIResource(backgroundImage.getWidth(), backgroundImage.getHeight()));
        frame.add(panel);
        panel.repaint();
        frame.pack();
        frame.setVisible(true);
    }

    private BufferedImage loadBackgroundImage(URI uri) {
        var id = "background-image";
        shownImages.put(id, uri);
        locations.put(id, new Point(0, 0));
        return loadBufferedImage(uri);
    }

    @Override
    public void drawEntityAtPosition(Entity entity, Point currentPosition) {
        String id = entity.getId();
        var uri = shownImages.computeIfAbsent(id, i -> getEntityIconUri());
        loadBufferedImage(uri);
        locations.put(id, currentPosition);
        panel.repaint();
    }

    private URI getEntityIconUri() {
        return toURI(getClass().getClassLoader().getResource("simple_entity_icon.png"));
    }

    private BufferedImage loadBufferedImage(URI uri) {
        return loadedImages.computeIfAbsent(uri, imageUri -> {
            try {
                return ImageIO.read(uri.toURL());
            } catch (IOException e) {
                throw new IOError(e);
            }
        });
    }

    private URI toURI(URL url) {
        try {
            return url.toURI();
        } catch (URISyntaxException e) {
            throw new IOError(e);
        }
    }

    @Override
    public void removeEntity(Entity entity) {
        locations.remove(entity.getId());
        shownImages.remove(entity.getId());
    }

}
