package de.ccetl.jparticles.systems;

import de.ccetl.jparticles.core.ParticleBase;
import de.ccetl.jparticles.core.shape.Shape;
import de.ccetl.jparticles.core.shape.ShapeType;
import de.ccetl.jparticles.types.snow.SnowElement;
import de.ccetl.jparticles.types.snow.SnowOptions;
import de.ccetl.jparticles.util.Utils;
import de.ccetl.jparticles.util.Vec2d;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SnowSystem extends ParticleBase<SnowOptions, SnowElement> {
    private long startTime = System.currentTimeMillis();
    private boolean isFinished = false;

    public SnowSystem(SnowOptions options, int width, int height) {
        super(options, width, height);
        bootstrap();
    }

    @Override
    public void init() {
        elements.clear();
        createSnowflakes(options.isFirstRandom());
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        double maxR = options.getMaxRadius();
        boolean swing = options.isSwing();
        int swingInterval = options.getSwingInterval();
        double swingProbability = options.getSwingProbability();
        double duration = options.getDuration();
        double delta = getDelta();

        List<SnowElement> toRemove = new LinkedList<>();
        List<SnowElement> toAdd = new LinkedList<>();

        for (SnowElement snowflake : elements) {
            double x = snowflake.getX();
            double y = snowflake.getY();
            double r = snowflake.getRadius();

            snowflake.getShape().render(options.getRenderer(), new Vec2d(x, y), r, snowflake.getColor());

            if (paused) {
                return;
            }

            snowflake.setX(x + snowflake.getVx() * delta);
            snowflake.setY(y + snowflake.getVy() * delta);

            if (swing && System.currentTimeMillis() - snowflake.getSwingAt() > swingInterval && Math.random() < (r / maxR) * swingProbability) {
                snowflake.setSwingAt(System.currentTimeMillis());
                snowflake.setVx(-snowflake.getVx());
            }

            if (x + r < 0 || x - r > width) {
                if (duration > 0) {
                    toRemove.add(snowflake);
                } else {
                    toAdd.add(createSnowflake(false));
                }
            } else if (y - r > height) {
                toRemove.add(snowflake);
            }
        }

        elements.removeAll(toRemove);
        for (SnowElement snowElement : toAdd) {
            if (elements.size() < options.getNumber() || !options.isStrict()) {
                elements.add(snowElement);
            }
        }

        if (paused) {
            return;
        }

        boolean timeEnd = duration > 0 && System.currentTimeMillis() - startTime > duration;

        if (!timeEnd && Math.random() > 0.9) {
            createSnowflakes(false);
        }

        if (elements.isEmpty()) {
            isFinished = true;
            onFinish();
        }
    }

    private SnowElement createSnowflake(boolean random) {
        double maxR = options.getMaxRadius();
        double minR = options.getMinRadius();
        double maxSpeed = options.getMaxSpeed();
        double minSpeed = options.getMinSpeed();

        double r = Utils.getRandomInRange(minR, maxR);

        return new SnowElement(
                r,
                Utils.getRandomInRange(0, width),
                random ? Utils.getRandomInRange(0, height) : -r,
                Utils.randomSpeed(minSpeed, maxSpeed),
                Math.abs(r * Utils.randomSpeed(minSpeed, maxSpeed)),
                options.getColorSupplier().get(),
                System.currentTimeMillis(),
                options.getShapeSupplier().get()
        );
    }

    private void createSnowflakes(boolean random) {
        if (random) {
            for (int i = 0; i < options.getNumber(); i++) {
                elements.add(createSnowflake(true));
            }
            return;
        }

        int count = Math.max(0, (int) Math.ceil(Math.random() * options.getNumber()));
        while (count-- > 0 && (elements.size() < options.getNumber() || !options.isStrict())) {
            elements.add(createSnowflake(false));
        }
    }

    public void fallAgain() {
        if (!paused && isFinished) {
            isFinished = false;
            startTime = System.currentTimeMillis();
            createSnowflakes(false);
        }
    }

    public void onFinish() {

    }

    public static abstract class DefaultConfig implements SnowOptions {
        private Supplier<Integer> colorSupplier = () -> -1;
        private Supplier<Shape> shapeSupplier = () -> new Shape(ShapeType.CIRCLE);
        private boolean firstRandom = false;
        private boolean strict = false;
        private int number = 6;
        private double maxRadius = 6.5;
        private double minRadius = 0.5;
        private double maxSpeed = 0.6;
        private double minSpeed = 0.1;
        private double duration = 0;
        private boolean swing = true;
        private int swingInterval = 2000;
        private double swingProbability = 0.06;

        @Override
        public boolean isFirstRandom() {
            return firstRandom;
        }

        public void setFirstRandom(boolean firstRandom) {
            this.firstRandom = firstRandom;
        }

        @Override
        public boolean isStrict() {
            return strict;
        }

        public void setStrict(boolean strict) {
            this.strict = strict;
        }

        @Override
        public Supplier<Integer> getColorSupplier() {
            return colorSupplier;
        }

        public void setColorSupplier(Supplier<Integer> colorSupplier) {
            this.colorSupplier = colorSupplier;
        }

        @Override
        public Supplier<Shape> getShapeSupplier() {
            return shapeSupplier;
        }

        public void setShapeSupplier(Supplier<Shape> shapeSupplier) {
            this.shapeSupplier = shapeSupplier;
        }

        @Override
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public double getMaxRadius() {
            return maxRadius;
        }

        public void setMaxRadius(double maxRadius) {
            this.maxRadius = maxRadius;
        }

        @Override
        public double getMinRadius() {
            return minRadius;
        }

        public void setMinRadius(double minRadius) {
            this.minRadius = minRadius;
        }

        @Override
        public double getMaxSpeed() {
            return maxSpeed;
        }

        public void setMaxSpeed(double maxSpeed) {
            this.maxSpeed = maxSpeed;
        }

        @Override
        public double getMinSpeed() {
            return minSpeed;
        }

        public void setMinSpeed(double minSpeed) {
            this.minSpeed = minSpeed;
        }

        @Override
        public double getDuration() {
            return duration;
        }

        public void setDuration(double duration) {
            this.duration = duration;
        }

        @Override
        public boolean isSwing() {
            return swing;
        }

        public void setSwing(boolean swing) {
            this.swing = swing;
        }

        @Override
        public int getSwingInterval() {
            return swingInterval;
        }

        public void setSwingInterval(int swingInterval) {
            this.swingInterval = swingInterval;
        }

        @Override
        public double getSwingProbability() {
            return swingProbability;
        }

        public void setSwingProbability(double swingProbability) {
            this.swingProbability = swingProbability;
        }
    }
}
