package idv.ron.server.main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil {

	/**
	 * @param srcImageData
	 *            source image
	 * @param newSize
	 *            the new image dimension; e.g.50 means to reduce the image less
	 *            than 50x50
	 * @return reduced image
	 */
	public static byte[] reduceImage(byte[] srcImageData, int newSize) {
		ByteArrayInputStream bais = new ByteArrayInputStream(srcImageData);
		int sampleSize = 1;
		int imageWidth = 0;
		int imageHeight = 0;

		if (newSize <= 1) {
			return srcImageData;
		}

		try {
			BufferedImage srcBufferedImage = ImageIO.read(bais);
			// return TYPE_INT_RGB if not recognize the image type
			// (TYPE_CUSTOM); or return the known image types
			int type = srcBufferedImage.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_RGB
					: srcBufferedImage.getType();
			imageWidth = srcBufferedImage.getWidth();
			imageHeight = srcBufferedImage.getHeight();
			if (imageWidth == 0 || imageHeight == 0) {
				return srcImageData;
			}
			// the longer side is divided by the newSize to get the reduced ratio
			int longer = Math.max(imageWidth, imageHeight);
			if (longer > newSize) {
				sampleSize = longer / newSize;
				imageWidth = srcBufferedImage.getWidth() / sampleSize;
				imageHeight = srcBufferedImage.getHeight() / sampleSize;
			}
			BufferedImage reducedBufferedImage = new BufferedImage(imageWidth,
					imageHeight, type);
			Graphics graphics = reducedBufferedImage.createGraphics();
			graphics.drawImage(srcBufferedImage, 0, 0, imageWidth, imageHeight,
					null);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(reducedBufferedImage, "jpg", baos);
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return srcImageData;
		}
	}
}
