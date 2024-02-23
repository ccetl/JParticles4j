package de.ccetl.jparticles.systems;

import de.ccetl.jparticles.core.Base;
import de.ccetl.jparticles.event.ResizeEvent;
import de.ccetl.jparticles.types.wave.WaveArgument;
import de.ccetl.jparticles.types.wave.WaveElement;
import de.ccetl.jparticles.types.wave.WaveOptions;
import de.ccetl.jparticles.util.Utils;
import de.ccetl.jparticles.util.Vec2d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class WaveSystem extends Base<WaveOptions> {
    private static final double DOUBLE_PI = 2 * Math.PI;
    private final List<WaveElement> elements = new ArrayList<>();

    public WaveSystem(WaveOptions config, int width, int height) {
        super(config, width, height);
    }

    @Override
    public void init() {
        createDots();
    }

    protected void createDots() {
        elements.clear();
        for (int i = 0; i < this.options.getNumber(); i++) {
            List<Vec2d> line = new ArrayList<>();

            double step = DOUBLE_PI / options.getCrestCount().get(i);

            for (int j = 0; j <= width; j++) {
                line.add(new Vec2d(j, j * step));
            }

            elements.add(new WaveElement(line, options.getFillColor().get(i), options.getLineColor().get(i)));
        }
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        double delta = getDelta();

        for (int i = 0; i < elements.size(); i++) {
            WaveElement element = elements.get(i);
            List<Vec2d> lines = element.getLine();
            double crestHeight = options.getCrestHeight().get(i);
            double offsetLeft = options.getOffsetLeft().get(i);
            double offsetTop = options.getOffsetTop().get(i);

            Vec2d[] vertices = new Vec2d[lines.size()];

            for (int j = 0; j < lines.size(); j++) {
                Vec2d dot = lines.get(j);
                vertices[j] = new Vec2d(dot.getX(), crestHeight * Math.sin(dot.getY() + offsetLeft) + offsetTop);
                if (!isPaused()) {
                    dot.setY(dot.getY() - options.getSpeed().get(i) * delta);
                }
            }

            if (options.isFill().get(i)) {
                Vec2d[] polygonVertices = Arrays.copyOf(vertices, vertices.length + 2);
                polygonVertices[vertices.length] = new Vec2d(width, height);
                polygonVertices[vertices.length + 1] = new Vec2d(0, height);
                options.getRenderer().drawPolygon(polygonVertices, element.getFillColor());
            }

            if (options.isLine().get(i)) {
                options.getRenderer().drawLine(vertices, options.getLineWidth().get(i), element.getLineColor());
            }
        }
    }

    @Override
    public void onResize(ResizeEvent event) {
        for (WaveElement element : elements) {
            for (Vec2d vec2d : element.getLine()) {
                vec2d.onResize(event, width, height);
            }
        }
        super.onResize(event);
    }

    public static abstract class DefaultConfig implements WaveOptions {
        private int number = 3;
        private WaveArgument<Boolean> fill = new WaveArgument<>(() -> false);
        private WaveArgument<Integer> fillColor = new WaveArgument<>(Utils::generateRandomColor);
        private WaveArgument<Boolean> line = new WaveArgument<>(() -> true);
        private WaveArgument<Integer> lineColor = new WaveArgument<>(Utils::generateRandomColor);
        private WaveArgument<Double> lineWidth = new WaveArgument<>(() -> Utils.getRandomInRange(2, 0.2));
        private WaveArgument<Double> offsetLeft = new WaveArgument<>(Utils.fillArrayList(() -> Math.random() * 1800, 3));
        private WaveArgument<Double> offsetTop = new WaveArgument<>(Utils.fillArrayList(() -> Math.random() * 1000, 3));
        private WaveArgument<Double> CrestHeight = new WaveArgument<>(Utils.fillArrayList(() -> Math.random() * 1000, 3));
        private WaveArgument<Double> crestCount = new WaveArgument<>(Utils.fillArrayList(() -> Utils.getRandomInRange(1, (double) 1800 / 2), 3));
        private WaveArgument<Double> speed = new WaveArgument<>(Utils.fillArrayList(() -> Utils.getRandomInRange(0.4, 0.1), 3));

        @Override
        public Supplier<Integer> getColorSupplier() {
            return null; // not needed here
        }

        @Override
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public WaveArgument<Boolean> isFill() {
            return fill;
        }

        public void setFill(WaveArgument<Boolean> fill) {
            this.fill = fill;
        }

        @Override
        public WaveArgument<Integer> getFillColor() {
            return fillColor;
        }

        public void setFillColor(WaveArgument<Integer> fillColor) {
            this.fillColor = fillColor;
        }

        @Override
        public WaveArgument<Boolean> isLine() {
            return line;
        }

        public void setLine(WaveArgument<Boolean> line) {
            this.line = line;
        }

        @Override
        public WaveArgument<Integer> getLineColor() {
            return lineColor;
        }

        public void setLineColor(WaveArgument<Integer> lineColor) {
            this.lineColor = lineColor;
        }

        @Override
        public WaveArgument<Double> getLineWidth() {
            return lineWidth;
        }

        public void setLineWidth(WaveArgument<Double> lineWidth) {
            this.lineWidth = lineWidth;
        }

        @Override
        public WaveArgument<Double> getOffsetLeft() {
            return offsetLeft;
        }

        public void setOffsetLeft(WaveArgument<Double> offsetLeft) {
            this.offsetLeft = offsetLeft;
        }

        @Override
        public WaveArgument<Double> getOffsetTop() {
            return offsetTop;
        }

        public void setOffsetTop(WaveArgument<Double> offsetTop) {
            this.offsetTop = offsetTop;
        }

        public WaveArgument<Double> getCrestHeight() {
            return CrestHeight;
        }

        public void setCrestHeight(WaveArgument<Double> getCrestHeight) {
            this.CrestHeight = getCrestHeight;
        }

        @Override
        public WaveArgument<Double> getCrestCount() {
            return crestCount;
        }

        public void setCrestCount(WaveArgument<Double> crestCount) {
            this.crestCount = crestCount;
        }

        @Override
        public WaveArgument<Double> getSpeed() {
            return speed;
        }

        public void setSpeed(WaveArgument<Double> speed) {
            this.speed = speed;
        }
    }
}
