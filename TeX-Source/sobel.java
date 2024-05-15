private PImage sobelEdgeDetection(PImage img) {
    PImage edgeImg = p.createImage(img.width, img.height, RGB);
    for (int x = 1; x < img.width - 1; x++) {
        for (int y = 1; y < img.height - 1; y++) {
            int topLeft = img.pixels[y * img.width + x - 1 - img.width];
            int top = img.pixels[y * img.width + x - img.width];
            int topRight = img.pixels[y * img.width + x + 1 - img.width];
            int left = img.pixels[y * img.width + x - 1];
            int right = img.pixels[y * img.width + x + 1];
            int bottomLeft = img.pixels[y * img.width + x - 1 + img.width];
            int bottom = img.pixels[y * img.width + x + img.width];
            int bottomRight = img.pixels[y * img.width + x + 1 + img.width];

            float gx = (-topLeft + topRight - 2f * left + 2f * right - bottomLeft + bottomRight) / 4.0f;
            float gy = (-topLeft - 2f * top - topRight + bottomLeft + 2f * bottom + bottomRight) / 4.0f;
            
            float gradient = PApplet.sqrt(gx * gx + gy * gy);
            edgeImg.pixels[y * edgeImg.width + x] = p.color(PApplet.min(gradient * 255, 255));
        }
    }
    edgeImg.updatePixels();
    return edgeImg;
}