package uk.ac.soton.ecs.ojvl1g17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.colour.ColourSpace;
import org.openimaj.image.colour.RGBColour;
import org.openimaj.image.processing.convolution.FGaussianConvolve;
import org.openimaj.image.typography.hershey.HersheyFont;

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

        MBFImage image1 = images.get(0);
        MBFImage image2 = images.get(1);

        MyHybridImages.makeHybrid(image1, 4f, image2, 4f);
    }
}
