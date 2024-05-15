package dk.voe;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.event.MouseEvent;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.clamp;
import static processing.core.PApplet.map;
import static processing.core.PConstants.*;

public class Main extends processing.core.PApplet {
    LayoutManager layoutManager;

    int fractalWindowRef;

    public static void main(String[] args) {
        PApplet.main("dk.voe.Main", args);
    }

    public void mouseWheel(MouseEvent event) {
        for (Window window : layoutManager.getWindows()) {
            if (window instanceof FractalWindow) {
                FractalWindow fractalWindow = (FractalWindow) window;
                float e = event.getCount();
                System.out.println(e);
                fractalWindow.setZoom(pow(1.1f, e));
                fractalWindow.setFramesSinceLastMovement(0);
            }
        }
    }

    public void settings() {
        size(800, 400);
        layoutManager = new LayoutManager();
        UiWindow uiWindow = new UiWindow(this, 0, 0, 400, 400);
        Slider iterationsSlider = new Slider(this, 100, 200, 200, 4, 10, 1000, 0.20f);
        int iterationsSliderRef = iterationsSlider.getId();
        TextField textField = new TextField(this, 0, 200, 100, 50, "Iterations: ");

        Slider caoSlider = new Slider(this, 100, 250, 200, 4, -2.5f, 2.5f, 0.5f);
        int caoSliderRef = caoSlider.getId();
        TextField caoTextField = new TextField(this, 0, 250, 100, 50, "Real c offset:");

        Slider cboSlider = new Slider(this, 100, 300, 200, 4, -2.5f, 2.5f, 0.5f);
        int cboSliderRef = cboSlider.getId();
        TextField cboTextField = new TextField(this, 0, 300, 100, 50, "Imaginary c offset:");

        Button resetButton = new Button(this, 50, 100, 100, 50, "Reset");
        int resetButtonRef = resetButton.getId();

        Button calcFractalDimensionButton = new Button(this, 50, 350, 200, 50, "Calculate Fractal Dimension");
        int calcFractalDimensionButtonRef = calcFractalDimensionButton.getId();

        TextField FractalDimensionText = new TextField(this, 0, 340, 100, 50, "Fractal dimension: ");
        int FractalDimensionTextRef = FractalDimensionText.getId();

        TextField titleTextField = new TextField(this, 0, 50, 400, 50, "Mandelbrotmængden SRC");

        uiWindow.addSlider(iterationsSlider);
        uiWindow.addSlider(caoSlider);
        uiWindow.addSlider(cboSlider);
        uiWindow.addTextField(textField);
        uiWindow.addTextField(caoTextField);
        uiWindow.addTextField(cboTextField);
        uiWindow.addButton(resetButton);
        uiWindow.addButton(calcFractalDimensionButton);
        uiWindow.addTextField(titleTextField);
        uiWindow.addTextField(FractalDimensionText);

        layoutManager.addWindow(uiWindow);

        layoutManager.addSliderChangeListener(
                iterationsSliderRef,
                () -> {
                    Slider el = layoutManager.getSliderById(iterationsSliderRef);
                    FractalWindow fractal = layoutManager.getFractalWindowById(fractalWindowRef);
                    if (el instanceof Slider && fractal instanceof FractalWindow) {
                        int iterations = (int) ((Slider) el).getMappedValue();
                        fractal.setIterations(iterations);
                    }
                });

        layoutManager.addSliderChangeListener(
                caoSliderRef,
                () -> {
                    Slider el = layoutManager.getSliderById(caoSliderRef);
                    FractalWindow fractal = layoutManager.getFractalWindowById(fractalWindowRef);
                    if (el instanceof Slider && fractal instanceof FractalWindow) {
                        double cao = ((Slider) el).getMappedValue();
                        fractal.params.cao = cao;
                    }
                });

        layoutManager.addSliderChangeListener(
                cboSliderRef,
                () -> {
                    Slider el = layoutManager.getSliderById(cboSliderRef);
                    FractalWindow fractal = layoutManager.getFractalWindowById(fractalWindowRef);
                    if (el instanceof Slider && fractal instanceof FractalWindow) {
                        double cbo = ((Slider) el).getMappedValue();
                        fractal.params.cbo = cbo;
                    }
                });

        layoutManager.addButtonChangeListener(
                resetButtonRef,
                () -> {
                    Button el = layoutManager.getButtonById(resetButtonRef);
                    FractalWindow fractal = layoutManager.getFractalWindowById(fractalWindowRef);
                    Slider caoSliderEl = layoutManager.getSliderById(caoSliderRef);
                    Slider cboSliderEl = layoutManager.getSliderById(cboSliderRef);
                    if (el instanceof Button && fractal instanceof FractalWindow) {
                        ((Button) el).clicked = false;
                        fractal.reset();
                        caoSliderEl.reset();
                        cboSliderEl.reset();
                    }
                });

        layoutManager.addButtonChangeListener(
                calcFractalDimensionButtonRef,
                () -> {
                    Button el = layoutManager.getButtonById(calcFractalDimensionButtonRef);
                    TextField fractalDimensionText = layoutManager.getTextFieldById(FractalDimensionTextRef);
                    FractalWindow fractal = layoutManager.getFractalWindowById(fractalWindowRef);
                    if (el instanceof Button && fractal instanceof FractalWindow) {
                        ((Button) el).clicked = false;
                        float fractalDimension = fractal.calcFratalDimension();
                        System.out.println("Fractal dimension: " + fractalDimension);
                        fractalDimensionText.setText("Fractal dimension: " + fractalDimension);
                    }
                });

        FractalParams params = new FractalParams(100, 1D, 0D, 0D, 400, 400, 1);
        FractalWindow fractalWindow = new FractalWindow(this, 400, 0, 400, 400, params);
        fractalWindowRef = fractalWindow.getId();
        layoutManager.addWindow(fractalWindow);

    }

    public void draw() {
        background(0);
        colorMode(HSB);
        layoutManager.handleEvents();
        for (Window window : layoutManager.getWindows()) {
            window.render();
        }
    }
}

class Drawable {
    protected PApplet p;
    protected int x, y, w, h;
    protected int id = 0;

    public Drawable(PApplet p, int x, int y, int w, int h) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        id = (int) p.random(0, 100000);
    }

    public int getId() {
        return id;
    }
}

class ButtonEventTrigger {
    int id;
    Runnable onClick;

    public ButtonEventTrigger(int id, Runnable onClick) {
        this.id = id;
        this.onClick = onClick;
    }
}

class SliderEventTrigger {
    int id;
    Runnable onValueChange;

    public SliderEventTrigger(int id, Runnable onValueChange) {
        this.id = id;
        this.onValueChange = onValueChange;
    }
}

class LayoutManager {

    protected ArrayList<Window> windows;

    protected ArrayList<ButtonEventTrigger> ButtonRefs;
    protected ArrayList<SliderEventTrigger> SliderRefs;
    protected ArrayList<Integer> TextFieldRefs;

    public LayoutManager() {
        windows = new ArrayList<>();

        ButtonRefs = new ArrayList<>();
        SliderRefs = new ArrayList<>();
        TextFieldRefs = new ArrayList<>();
    }

    public void handleEvents() {
        for (ButtonEventTrigger buttonRef : ButtonRefs) {
            Button button = getButtonById(buttonRef.id);
            if (button != null && button.clicked) {
                buttonRef.onClick.run();
            }
        }

        for (SliderEventTrigger sliderRef : SliderRefs) {
            Slider slider = getSliderById(sliderRef.id);
            if (slider != null) {
                sliderRef.onValueChange.run();
            }
        }
    }

    void addWindow(Window window) {
        windows.add(window);
    }

    ArrayList<Window> getWindows() {
        return windows;
    }

    Button getButtonById(int id) {
        for (Window window : windows) {
            if (window instanceof UiWindow) {
                UiWindow uiWindow = (UiWindow) window;
                for (Button button : uiWindow.buttons) {
                    if (button.getId() == id) {
                        return button;
                    }
                }
            }
        }
        return null;
    }

    Slider getSliderById(int id) {
        for (Window window : windows) {
            if (window instanceof UiWindow) {
                UiWindow uiWindow = (UiWindow) window;
                for (Slider slider : uiWindow.sliders) {
                    if (slider.getId() == id) {
                        return slider;
                    }
                }
            }
        }
        return null;
    }

    TextField getTextFieldById(int id) {
        for (Window window : windows) {
            if (window instanceof UiWindow) {
                UiWindow uiWindow = (UiWindow) window;
                for (TextField textField : uiWindow.textFields) {
                    if (textField.getId() == id) {
                        return textField;
                    }
                }
            }
        }
        return null;
    }

    FractalWindow getFractalWindowById(int id) {
        for (Window window : windows) {
            if (window instanceof FractalWindow) {
                FractalWindow fractalWindow = (FractalWindow) window;
                if (fractalWindow.getId() == id) {
                    return fractalWindow;
                }
            }
        }
        return null;
    }

    void addButtonChangeListener(int id, Runnable onClick) {
        ButtonRefs.add(new ButtonEventTrigger(id, onClick));
    }

    void addSliderChangeListener(int id, Runnable onValueChange) {
        SliderRefs.add(new SliderEventTrigger(id, onValueChange));
    }

    void addTextFieldRef(int id) {
        TextFieldRefs.add(id);
    }

}

class Window extends Drawable {
    protected PImage content;

    public Window(PApplet p, int x, int y, int w, int h) {
        super(p, x, y, w, h);
        content = p.createImage(w, h, PConstants.ARGB);
    }

    public void render() {
        p.image(content, x, y, w, h);
    }
}

class UiWindow extends Window {
    protected ArrayList<Button> buttons;
    protected ArrayList<Slider> sliders;
    protected ArrayList<TextField> textFields;

    public UiWindow(PApplet p, int x, int y, int w, int h) {
        super(p, x, y, w, h);
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        textFields = new ArrayList<>();
    }

    void addButton(Button button) {
        buttons.add(button);
    }

    void addSlider(Slider slider) {
        sliders.add(slider);
    }

    void addTextField(TextField textField) {
        textFields.add(textField);
    }

    @Override
    public void render() {
        for (Button button : buttons) {
            button.render();
        }
        for (Slider slider : sliders) {
            slider.render();
        }
        for (TextField textField : textFields) {
            textField.render();
        }
    }
}

final class FractalParams {
    int w;
    int h;
    int sf;
    int i;
    double z;
    double ox;
    double oy;
    double cao;
    double cbo;

    public FractalParams(int i, Double z, Double ox, Double oy, int w, int h, int sf) {
        this.i = i;
        this.z = z;
        this.ox = ox;
        this.oy = oy;
        this.w = w;
        this.h = h;
        this.sf = sf;
        this.cao = 0;
        this.cbo = 0;
    }
}

class FractalWindow extends Window {
    protected int framesSinceLastMovement;
    protected FractalParams params;
    protected boolean isResetting;

    public FractalWindow(PApplet p, int x, int y, int w, int h, FractalParams params) {
        super(p, x, y, w, h);
        content.loadPixels();
        this.params = params;
    }

    public void reset() {
        isResetting = true;
    }

    private void update() {
        if (isResetting) {
            framesSinceLastMovement = 0;
            params.z += (1 - params.z) * 0.05;
            if (abs(1 - params.z) < 0.8) {
                params.ox += (0 - params.ox) * 0.1;
                params.oy += (0 - params.oy) * 0.1;
                if (abs(0 - params.ox) < 0.0001 && abs(0 - params.oy) < 0.0001 && abs(1 - params.z) < 0.0001) {
                    isResetting = false;
                    params.z = 1;
                    params.ox = 0;
                    params.oy = 0;
                }
            }
        }
    }

    @Override
    public void render() {
        handleDrag();
        if (framesSinceLastMovement > 30) {
            params.sf = 1;
            PImage fractal = drawMandelbrot(params);
            content = fractal;
        } else if (framesSinceLastMovement > 7) {
            params.sf = 2;
            PImage fractal = drawMandelbrot(params);
            content = fractal;
        } else {
            params.sf = 3;
            PImage fractal = drawMandelbrot(params);
            content = fractal;
        }
        update();
        framesSinceLastMovement++;
        super.render();
    }

    protected PImage drawMandelbrot(FractalParams params) {
        int w = params.w;
        int h = params.h;
        int sf = params.sf;
        int i = params.i;
        double z = params.z;
        double ox = params.ox;
        double oy = params.oy;
        double cao = params.cao;
        double cbo = params.cbo;

        PImage img = p.createImage(w / sf, h / sf, RGB);
        img.loadPixels();
        for (int x = 0; x < img.width; x++) {
            for (int y = 0; y < img.height; y++) {
                // double a = map(x, 0, img.width,(double)( -2.5 / z + ox),(double)(2.5 / z +
                // ox));
                // double b = map(y, 0, img.height,(double)( -2.5 / z + oy),(double)(2.5 / z +
                // oy));
                double a = dmap((double) x, 0D, (double) img.width, (double) (-2.5 / z + ox), (double) (2.5 / z + ox));
                double b = dmap((double) y, 0D, (double) img.height, (double) (-2.5 / z + oy), (double) (2.5 / z + oy));
                double ca = a + cao;
                double cb = b + cbo;
                int n = 0;
                while (n < i) {
                    double aa = a * a - b * b;
                    double bb = 2 * a * b;
                    a = aa + ca;
                    b = bb + cb;
                    if (abs((double) (a + b)) > 16) {
                        break;
                    }
                    n++;
                }
                int bright = (int) (map(n, 0f, i, 0f, 255f));
                if (n == i) {
                    bright = 10000000; // dette betyder at punktet er indenfor mængden "0" kan ikke brugs pga. kanten
                                       // er ude efter 0 iterationer
                }
                int pix = x + y * img.width;
                img.pixels[pix] = p.color(((bright + 50) * 2) % 255, bright == 10000000 ? 0 : 255,
                        bright == 10000000 ? 0 : 255);

            }
        }
        img.updatePixels();
        return img;
    }

    private Double dmap(Double value, Double start1, Double stop1, Double start2, Double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    public void setFramesSinceLastMovement(int frameCount) {
        this.framesSinceLastMovement = frameCount;
    }

    public void setZoom(double zoom) {
        params.z *= zoom;
        PApplet.println(params.z);
    }

    public void setIterations(int iterations) {
        params.i = iterations;
    }

    private void handleDrag() {
        if (p.mousePressed) {
            framesSinceLastMovement = 0;
            if (p.mouseX > x && p.mouseX < x + w && p.mouseY > y && p.mouseY < y + h) {
                params.ox -= (double) (p.mouseX - p.pmouseX) / 100 / params.z;
                params.oy -= (double) (p.mouseY - p.pmouseY) / 100 / params.z;
            }
        }
    }

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

    private float[] linearRegression(float[] x, float[] y) {
        float sumX = 0;
        float sumY = 0;
        float sumXY = 0;
        float sumXX = 0;
        for (int i = 0; i < x.length; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumXX += x[i] * x[i];
        }
        float a = (sumY * sumXX - sumX * sumXY) / (x.length * sumXX - sumX * sumX);
        float b = (x.length * sumXY - sumX * sumY) / (x.length * sumXX - sumX * sumX);
        return new float[] { a, b };
    }

    public float calcFratalDimension() {
        long[] totalPixels = new long[10];
        for (int scalingFactor = 10; scalingFactor >= 1; scalingFactor--) {
            FractalParams params = new FractalParams(100, 1.4d, -1.2d, 0.0d, 4000, 4000, scalingFactor);
            PImage img = drawMandelbrot(params);
            img = twoToneFilter(img);
            img = sobelEdgeDetection(img);
            img.save("fractal" + scalingFactor + ".png");
            long whitePixels = 0;
            img.loadPixels();
            for (int x = 0; x < img.width; x++) {
                for (int y = 0; y < img.height; y++) {
                    int c = img.pixels[x + y * img.width];
                    if (p.red(c) == 255 && p.green(c) == 255 && p.blue(c) == 255) {
                        whitePixels++;
                    }
                }
            }
            totalPixels[scalingFactor - 1] = whitePixels;
            PApplet.println("White pixels: " + whitePixels);
        }
        float[] x = new float[10];
        float[] y = new float[10];
        for (int i = 0; i < 10; i++) {
            x[i] = PApplet.log(1.0f / (float) (i + 1));
            y[i] = PApplet.log((float) totalPixels[i]);
        }
        float[] regression = linearRegression(x, y);
        return regression[1];
    }
}

class Clickable extends Drawable {
    protected boolean clicked;
    protected boolean hovered;

    public Clickable(PApplet p, int x, int y, int w, int h) {
        super(p, x, y, w, h);
        this.clicked = false;
        this.hovered = false;
    }
}

class Button extends Clickable {
    protected String text;

    public Button(PApplet p, int x, int y, int w, int h, String text) {
        super(p, x, y, w, h);
        this.text = text;
    }

    public void render() {
        p.rect(x, y, w, h);
        p.fill(0);
        p.text(text, x + 10, y + h / 2);
        p.fill(255);
        checkHover();
    }

    private void checkHover() {
        int margin = 20;
        if (p.mouseX > x - margin && p.mouseX < x + w + margin && p.mouseY > y - margin && p.mouseY < y + h + margin) {
            hovered = true;
        } else {
            hovered = false;
        }
        if (hovered && p.mousePressed) {
            clicked = true;
        }
    }
}

class Slider extends Clickable {
    protected float value;
    protected float startValue;
    protected boolean isResetting;
    protected float min, max;

    public Slider(PApplet p, int x, int y, int w, int h, float min, float max, float value) {
        super(p, x, y, w, h);
        this.min = min;
        this.max = max;
        this.value = value;
        this.startValue = value;
    }

    public float getMappedValue() {
        return map(value, 0, 1, min, max);
    }

    public void reset() {
        isResetting = true;
    }

    private void update() {
        if (isResetting) {
            value += (startValue - value) * 0.1;
            if (abs(startValue - value) < 0.0001) {
                isResetting = false;
                value = startValue;
            }
        }
        checkHover();
        if (hovered && p.mousePressed) {
            float v = clamp(p.mouseX, x, x + w);
            value = map(v, x, x + w, 0, 1);
        }
    }

    public void render() {
        update();
        p.rect(x, y, w, h);
        p.ellipse(x + w * value, y + h / 2, 10, 10);
        p.text((int) map(value, 0, 1, min, max), x + w + 10, y + h / 2);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    private void checkHover() {
        int margin = 20;
        if (p.mouseX > x - margin && p.mouseX < x + w + margin && p.mouseY > y - margin && p.mouseY < y + h + margin) {
            hovered = true;
        } else {
            hovered = false;
        }
    }
}

class TextField extends Drawable {
    protected String text;

    public TextField(PApplet p, int x, int y, int w, int h, String text) {
        super(p, x, y, w, h);
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void render() {
        p.text(text, x, y);
    }

}