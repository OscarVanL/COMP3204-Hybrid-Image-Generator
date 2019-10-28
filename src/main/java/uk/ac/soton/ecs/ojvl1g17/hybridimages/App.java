package uk.ac.soton.ecs.ojvl1g17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * OpenIMAJ Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        List<MBFImage> images = new ArrayList<>();

        //Get full project directory
        String projectPath = System.getProperty("user.dir");
        // Enumerate paths of files in sub-folder 'data'
        Stream<Path> imgFolder = Files.walk(Paths.get(projectPath + "/data/"));
        // Read these as MBFImages where possible
        imgFolder.filter(Files::isRegularFile)
                .map(Path::toFile)
                .forEach(e -> {
                    try {
                        MBFImage img = ImageUtilities.readMBF(e);
                        images.add(img);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

        // Shuffle loaded images so image1 and image2 are randomly selected
        Collections.shuffle(images);

        // Randomly select two images in the folder with the same width and height
        MBFImage image1 = images.remove(0);
        MBFImage image2 = null;
        for (MBFImage img : images) {
            if (img.getWidth() == image1.getWidth() && img.getHeight() == image1.getHeight()) {
                image2 = img;
            }
        }
        // If a corresponding image with the same width & height is not found, throw an exception
        if (image2 == null) {
            System.err.println("An image in the 'data' folder does not have a second image with the same resolution to make into a hybrid image.");
            System.exit(1);
        }

        MBFImage hybridImage = MyHybridImages.makeHybrid(image1, 4f, image2, 4f);
        DisplayUtilities.display(hybridImage);
    }
}
