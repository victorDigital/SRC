private PImage twoToneFilter(PImage img) {
    PImage img2 = p.createImage(img.width, img.height, RGB);
    img2.loadPixels();
    for (int x = 0; x < img.width; x++) {
        for (int y = 0; y < img.height; y++) {
            int c = img.pixels[x + y * img.width];
            if (p.red(c) == 0 && p.green(c) == 0 && p.blue(c) == 0) {
                img2.pixels[x + y * img.width] = p.color(0);
            } else {
                img2.pixels[x + y * img.width] = p.color(255);
            }
        }
    }
    img2.updatePixels();
    return img2;
}