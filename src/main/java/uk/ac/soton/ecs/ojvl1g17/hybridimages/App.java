package uk.ac.soton.ecs.ojvl1g17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.resize.ResizeProcessor;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Oscar van Leusen
 */
public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Displaying hybrid of two images inside /data/ folder of launch directory");
            // If no argument was given, assume the /data/ folder in launch directory
            String path = System.getProperty("user.dir") + "/data/";
            try {
                displayFromFolder(path);
            } catch (IOException e) {
                System.err.println("Invalid path: " + path);
                e.printStackTrace();
            }
        } else {
            if (args[0].equals("example") || args[0].equals("donald") || args[0].equals("putin")) {
                System.out.println("Displaying Donald-Putin example hybrid");
                // Show example Donald-Putin hybrid
                try {
                    displayTrumpPutin();
                } catch (IOException e) {
                    System.err.println("Unable to open source images from URLs, is imgur.com down?");
                    e.printStackTrace();
                }
            } else if (args[0].length() > 0) {
                System.out.println("Displaying hybrid of two images randomly selected from your provided path");
                // Assume non 'example' arguments are paths to folders of image pairs
                try {
                    displayFromFolder(args[0]);
                } catch (IOException e) {
                    System.err.println("Invalid path: " + args[0]);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Display a hybrid image from two randomly selected images (of identical resolution) in the specified folder
     * @param path Path to check for hybrid image pairs
     * @throws IOException Invalid path
     */
    private static void displayFromFolder(String path) throws IOException {
        List<MBFImage> images = new ArrayList<>();

        // Enumerate paths of files in sub-folder 'data'
        Stream<Path> imgFolder = Files.walk(Paths.get(path));
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
            throw new IOException();
        }

        MBFImage hybridImage = MyHybridImages.makeHybrid(image1, (float) 4.0, image2, (float) 4.0);
        MyHybridImages.displayCascade(hybridImage, 4);
    }

    /**
     * Display my Trump Putin hybrid image example, I title this artwork "Campaign funds".
     * @throws IOException Throws exception if source URLs cannot be opened (eg: imgur.com is inaccessible)
     */
    private static void displayTrumpPutin() throws IOException {
        MBFImage donald = ImageUtilities.readMBF(new URL("https://i.imgur.com/Xwf3HdQ.png"));
        MBFImage putin = ImageUtilities.readMBF(new URL("https://i.imgur.com/MjzKopj.png"));

        //High frequency (donald) can be seen close, at long distance only low frequency (putin) can be seen
        //Sigma1: How much high frequency to remove from first image. Sigma2: How much low frequency to leave in second
        MBFImage hybridImage = MyHybridImages.makeHybrid(putin, (float) 2.3, donald, (float) 4.0);

        MyHybridImages.displayCascade(hybridImage, 4);
    }
}
