package uk.ac.soton.ecs.ojvl1g17.hybridimages;

import org.openimaj.image.DisplayUtilities;
import org.openimaj.image.FImage;
import org.openimaj.image.processor.SinglebandImageProcessor;

/**
 * @author Oscar van Leusen
 */
public class MyConvolution implements SinglebandImageProcessor<Float, FImage> {
    private float[][] kernel;

    public MyConvolution(float[][] kernel) {
        //note that like the image pixels kernel is indexed by [row][column]
        this.kernel = kernel;
    }

    @Override
    public void processImage(FImage image) {
        // convolve image with kernel and store result back in image
        //
        // hint: use FImage#internalAssign(FImage) to set the contents
        // of your temporary buffer image to the image.

        // Zero-pad the image with required pixels to allow the convolution to reach the edges
        int padHeightPx = (kernel.length - 1) / 2;
        int padWidthPx = (kernel[0].length - 1) / 2;
        FImage temp = image.padding(padWidthPx, padHeightPx, 0.0f);

        //Ensure the centre of the kernel is always within the pixels of the original image, not the padding.
        for (int x=padWidthPx; x<=(temp.width-(padWidthPx+1)); x++) {
            for (int y=padHeightPx; y<=(temp.height-(padHeightPx+1)); y++) {
                float convolutionVal = calculateConvolution(temp, x, y);
                temp.setPixel(x, y, convolutionVal);
            }
        }

        //Remove padding as no longer required
        temp = temp.padding(-padWidthPx, -padHeightPx);

        //Overwrite original image with our temp/buffer image
        image.internalAssign(temp);
    }

    private float calculateConvolution(FImage image, int x, int y) {
        //System.out.println("CALCULATING CONVOLUTION FOR : " + x + "," + y);
        float total = 0;

        int cent = (kernel.length-1) / 2;
        int halfKernelHeightLen = (kernel.length - 1) / 2;
        int halfKernelWidthLen = (kernel[0].length - 1) / 2;

        int kernelPos = 0;


        for (int i=-halfKernelWidthLen; i<=halfKernelWidthLen; i++) {
            for (int j=-halfKernelHeightLen; j<=halfKernelHeightLen; j++) {
                int currX = x + i;
                int currY = y + j;
                int kernelX = kernelPos % kernel[0].length;
                int kernelY = kernelPos / kernel[0].length;
                //System.out.println("Kernel Pos: " + kernelX + "," + kernelY + ": " + kernel[kernelY][kernelX]);
                //System.out.println("Current pixel val: " + image.getPixel(x, y));
                total += kernel[kernelY][kernelX] * image.getPixel(currX, currY);
                kernelPos++;
            }
        }
        //System.out.println("Total for that pixel: " + total);
        return total;
    }
}
