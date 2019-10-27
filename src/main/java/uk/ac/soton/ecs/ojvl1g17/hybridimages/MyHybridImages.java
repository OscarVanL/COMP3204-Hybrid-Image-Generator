package uk.ac.soton.ecs.ojvl1g17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.MBFImage;
import org.openimaj.image.processing.convolution.Gaussian2D;

import java.util.Arrays;

/**
 * @author Oscar van Leusen
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

        // === Code provided in CW specification, not my code ===
        int size = (int) (8.0f * lowSigma + 1.0f);
        if (size % 2 == 0) size++;
        System.out.println("Using Kernel of size " + size);
        // === (End of code not written by myself) ===

        //Generate Low-pass filter kernel
        float[][] lowPassKernel = Gaussian2D.createKernelImage(size, lowSigma).pixels;
        System.out.println("Generated kernel:");
        for (int i=0; i<lowPassKernel.length; i++) {
            System.out.println(Arrays.toString(lowPassKernel[i]));
        }

        DisplayUtilities.display(lowImage);
        //Perform low pass filtering convolution on first image
        MyConvolution lowPassConvolution = new MyConvolution(lowPassKernel);
        lowImage = lowImage.process(lowPassConvolution);
        DisplayUtilities.display(lowImage);
        return null;
    }
}
