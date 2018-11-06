package org.smileframework.ioc.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.smileframework.ioc.bean.core.env.Environment;
import org.smileframework.tool.asserts.Assert;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;

/**
 * @Package: org.smileframework.ioc.util
 * @Description: ${todo}
 * @date: 2018/4/30 下午8:42
 * @author: liuxin
 */
public class SpringImageBanner implements SpringBanner {
    private static final String PROPERTY_PREFIX = "spring.banner.image.";
    private static final Log logger = LogFactory.getLog(SpringImageBanner.class);
    private static final double[] RGB_WEIGHT = new double[]{0.2126D, 0.7152D, 0.0722D};
    private static final char[] PIXEL = new char[]{' ', '.', '*', ':', 'o', '&', '8', '#', '@'};
    private static final int LUMINANCE_INCREMENT = 10;
    private static final int LUMINANCE_START;
    private final InputStream image;

    public SpringImageBanner(InputStream image) {
        Assert.notNull(image, "Image must not be null");
        this.image = image;
    }

    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        String headless = System.getProperty("java.awt.headless");

        try {
            System.setProperty("java.awt.headless", "true");
            this.printBanner(environment, out);
        } catch (Throwable var9) {
            logger.warn("Image banner not printable: " + this.image + " (" + var9.getClass() + ": \'" + var9.getMessage() + "\')");
            logger.debug("Image banner printing failure", var9);
        } finally {
            if (headless == null) {
                System.clearProperty("java.awt.headless");
            } else {
                System.setProperty("java.awt.headless", headless);
            }

        }

    }

    private void printBanner(Environment environment, PrintStream out) throws IOException {
//        int width = ((Integer)this.getProperty(environment, "width", Integer.class, Integer.valueOf(76))).intValue();
//        int height = ((Integer)this.getProperty(environment, "height", Integer.class, Integer.valueOf(0))).intValue();
//        int margin = ((Integer)this.getProperty(environment, "margin", Integer.class, Integer.valueOf(2))).intValue();
//        boolean invert = ((Boolean)this.getProperty(environment, "invert", Boolean.class, Boolean.valueOf(false))).booleanValue();

        int width = 76;
        int height = 0;
        int margin = 2;
        boolean invert = false;
        SpringImageBanner.Frame[] frames = this.readFrames(width, height);

        for (int i = 0; i < frames.length; ++i) {
            if (i > 0) {
                this.resetCursor(frames[i - 1].getImage(), out);
            }

            this.printBanner(frames[i].getImage(), margin, invert, out);
            this.sleep(frames[i].getDelayTime());

        }

    }

    private <T> T getProperty(Environment environment, String name, Class<T> targetType, T defaultValue) {
        return environment.getProperty("spring.banner.image." + name, targetType, defaultValue);
    }

    private SpringImageBanner.Frame[] readFrames(int width, int height) throws IOException {
        InputStream inputStream = this.image;
        Throwable var4 = null;

        Object var7;
        try {
            ImageInputStream imageStream = ImageIO.createImageInputStream(inputStream);
            Throwable var6 = null;

            try {
                var7 = this.readFrames(width, height, imageStream);
            } catch (Throwable var30) {
                var7 = var30;
                var6 = var30;
                throw var30;
            } finally {
                if (imageStream != null) {
                    if (var6 != null) {
                        try {
                            imageStream.close();
                        } catch (Throwable var29) {
                            var6.addSuppressed(var29);
                        }
                    } else {
                        imageStream.close();
                    }
                }

            }
        } catch (Throwable var32) {
            var4 = var32;
            throw var32;
        } finally {
            if (inputStream != null) {
                if (var4 != null) {
                    try {
                        inputStream.close();
                    } catch (Throwable var28) {
                        var4.addSuppressed(var28);
                    }
                } else {
                    inputStream.close();
                }
            }

        }

        return (SpringImageBanner.Frame[]) var7;
    }

    private SpringImageBanner.Frame[] readFrames(int width, int height, ImageInputStream stream) throws IOException {
        Iterator readers = ImageIO.getImageReaders(stream);
        ImageReader reader = (ImageReader) readers.next();

        try {
            ImageReadParam readParam = reader.getDefaultReadParam();
            reader.setInput(stream);
            int frameCount = reader.getNumImages(true);
            SpringImageBanner.Frame[] frames = new SpringImageBanner.Frame[frameCount];

            for (int i = 0; i < frameCount; ++i) {
                frames[i] = this.readFrame(width, height, reader, i, readParam);
            }

            SpringImageBanner.Frame[] var13 = frames;
            return var13;
        } finally {
            reader.dispose();
        }
    }

    private SpringImageBanner.Frame readFrame(int width, int height, ImageReader reader, int imageIndex, ImageReadParam readParam) throws IOException {
        BufferedImage image = reader.read(imageIndex, readParam);
        BufferedImage resized = this.resizeImage(image, width, height);
        int delayTime = this.getDelayTime(reader, imageIndex);
        return new SpringImageBanner.Frame(resized, delayTime);
    }

    private int getDelayTime(ImageReader reader, int imageIndex) throws IOException {
        IIOMetadata metadata = reader.getImageMetadata(imageIndex);
        IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());
        IIOMetadataNode extension = findNode(root, "GraphicControlExtension");
        String attribute = extension == null ? null : extension.getAttribute("delayTime");
        return attribute == null ? 0 : Integer.parseInt(attribute) * 10;
    }

    private static IIOMetadataNode findNode(IIOMetadataNode rootNode, String nodeName) {
        if (rootNode == null) {
            return null;
        } else {
            for (int i = 0; i < rootNode.getLength(); ++i) {
                if (rootNode.item(i).getNodeName().equalsIgnoreCase(nodeName)) {
                    return (IIOMetadataNode) rootNode.item(i);
                }
            }

            return null;
        }
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) {
        if (width < 1) {
            width = 1;
        }

        if (height <= 0) {
            double resized = (double) width / (double) image.getWidth() * 0.5D;
            height = (int) Math.ceil((double) image.getHeight() * resized);
        }

        BufferedImage resized1 = new BufferedImage(width, height, 1);
        Image scaled = image.getScaledInstance(width, height, 1);
        resized1.getGraphics().drawImage(scaled, 0, 0, (ImageObserver) null);
        return resized1;
    }

    private void resetCursor(BufferedImage image, PrintStream out) {
        int lines = image.getHeight() + 3;
        out.print("\u001b[" + lines + "A\r");
    }

    private void printBanner(BufferedImage image, int margin, boolean invert, PrintStream out) {
        AnsiBackground background = invert ? AnsiBackground.BLACK : AnsiBackground.DEFAULT;
        out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
        out.print(AnsiOutput.encode(background));
        out.println();
        out.println();
        AnsiColor lastColor = AnsiColor.DEFAULT;

        for (int y = 0; y < image.getHeight(); ++y) {
            int x;
            for (x = 0; x < margin; ++x) {
                out.print(" ");
            }

            for (x = 0; x < image.getWidth(); ++x) {
                Color color = new Color(image.getRGB(x, y), false);
                AnsiColor ansiColor = AnsiColors.getClosest(color);
                if (ansiColor != lastColor) {
                    out.print(AnsiOutput.encode(ansiColor));
                    lastColor = ansiColor;
                }

                out.print(this.getAsciiPixel(color, invert));
            }

            out.println();
        }

        out.print(AnsiOutput.encode(AnsiColor.DEFAULT));
        out.print(AnsiOutput.encode(AnsiBackground.DEFAULT));
        out.println();
    }

    private char getAsciiPixel(Color color, boolean dark) {
        double luminance = (double) this.getLuminance(color, dark);

        for (int i = 0; i < PIXEL.length; ++i) {
            if (luminance >= (double) (LUMINANCE_START - i * 10)) {
                return PIXEL[i];
            }
        }

        return PIXEL[PIXEL.length - 1];
    }

    private int getLuminance(Color color, boolean inverse) {
        double luminance = 0.0D;
        luminance += this.getLuminance(color.getRed(), inverse, RGB_WEIGHT[0]);
        luminance += this.getLuminance(color.getGreen(), inverse, RGB_WEIGHT[1]);
        luminance += this.getLuminance(color.getBlue(), inverse, RGB_WEIGHT[2]);
        return (int) Math.ceil(luminance / 255.0D * 100.0D);
    }

    private double getLuminance(int component, boolean inverse, double weight) {
        return (double) (inverse ? 255 - component : component) * weight;
    }

    private void sleep(int delay) {
        try {
            Thread.sleep((long) delay);
        } catch (InterruptedException var3) {
            Thread.currentThread().interrupt();
        }

    }

    static {
        LUMINANCE_START = 10 * PIXEL.length;
    }

    private static class Frame {
        private final BufferedImage image;
        private final int delayTime;

        Frame(BufferedImage image, int delayTime) {
            this.image = image;
            this.delayTime = delayTime;
        }

        public BufferedImage getImage() {
            return this.image;
        }

        public int getDelayTime() {
            return this.delayTime;
        }

    }
}
