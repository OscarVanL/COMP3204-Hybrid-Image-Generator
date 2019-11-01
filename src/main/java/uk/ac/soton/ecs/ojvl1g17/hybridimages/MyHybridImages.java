package uk.ac.soton.ecs.ojvl1g17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;
import org.openimaj.image.processing.resize.ResizeProcessor;

/**
 * @author Oscar van Leusen, University of Southampton
 */
public class MyHybridImages {
    /**
     * Compute a hybrid image combining low-pass and high-pass filtered images
     *
     * @param lowImage
     *            the image to which apply the low pass filter
     * @param lowSigma
     *            the standard deviation of the low-pass filter
     * @param highImage
     *            the image to which apply the high pass filter
     * @param highSigma
     *            the standard deviation of the low-pass component of computing the
     *            high-pass filtered image
     * @return the computed hybrid image
     */
    public static MBFImage makeHybrid(MBFImage lowImage, float lowSigma, MBFImage highImage, float highSigma) {
        //implement your hybrid images functionality here.
        //Your submitted code must contain this method, but you can add
        //additional static methods or implement the functionality through
        //instance methods on the `MyHybridImages` class of which you can create
        //an instance of here if you so wish.
        //Note that the input images are expected to have the same size, and the output
        //image will also have the same height & width as the inputs.

        // Generates low pass filtered version of lowImage
        lowImage = generateLowPass(lowImage, lowSigma);

        // Generates low pass filtered version of highImage - required to calculate the high passed image
        MBFImage lowPassedComponent = generateLowPass(highImage, highSigma);
        // Generates high pass filtered version of highImage
        highImage = highImage.subtract(lowPassedComponent);

        // Calculate the hybrid image
        MBFImage hybridImage = lowImage.add(highImage);
        return hybridImage;
    }

    /**
     * Generates a low-pass filtered image from an input image and sigma value
     * @param image Image to filter
     * @param sigma Cutoff frequency control
     * @return
     */
    public static MBFImage generateLowPass(MBFImage image, float sigma) {
        // === This small section is not my code and is provided in CW specification ===
        int size = (int) (8.0f * sigma + 1.0f);
        if (size % 2 == 0) size++;
        System.out.println("Using Kernel of size " + size);
        // === End of code not written by myself ===

        //Generate Low-pass filter kernel
        float[][] lowPassKernel = Gaussian2D.createKernelImage(size, sigma).pixels;

        //Perform low pass filtering convolution on first image
        MyConvolution lowPassConvolution = new MyConvolution(lowPassKernel);
        image = image.process(lowPassConvolution);
        return image;
    }

    /**
     * Displays the cascading sized hybrid images to emphasise the hybrid image effect
     * @param hybridImage Generated hybrid image
     * @param cascadeAmount Amount of times to cascade (halve) the image by
     */
    public static void displayCascade(MBFImage hybridImage, int cascadeAmount) {
        int requiredWidth = 0;
        int widthBuff = hybridImage.getWidth();
        for (int i=0; i<=cascadeAmount; i++) {
            requiredWidth += widthBuff + 10;
            widthBuff /= 2;
        }

        // Create a new larger MBFImage in order to contain the cascade of resized images
        MBFImage canvas = new MBFImage(requiredWidth, hybridImage.getHeight());
        // Draw the full size hybrid image
        canvas.drawImage(hybridImage, 0, 0);

        int horizontalOffset = hybridImage.getWidth() + 10;
        ResizeProcessor resizeHalf = new ResizeProcessor(ResizeProcessor.Mode.HALF);
        // Create 4 cascading sized hybrid images
        for (int i=0; i<cascadeAmount; i++) {
            hybridImage = hybridImage.process(resizeHalf);
            canvas.drawImage(hybridImage, horizontalOffset, canvas.getHeight()-hybridImage.getHeight());
            horizontalOffset += hybridImage.getWidth() + 10;
        }

        //Display overall hybrid image
        DisplayUtilities.display(canvas);
    }

}
