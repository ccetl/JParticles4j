package de.ccetl.jparticles.systems;

import de.ccetl.jparticles.core.Base;
import de.ccetl.jparticles.event.MouseEvent;
import de.ccetl.jparticles.types.line.LineElement;
import de.ccetl.jparticles.types.line.LineOptions;
import de.ccetl.jparticles.util.Utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class LineSystem extends Base<LineOptions> {
    private final List<LineElement> elements = new LinkedList<>();
    private final int[] specificAngles = {-180, -90, 0, 90, 180};

    public LineSystem(LineOptions config, int width, int height) {
        super(config, width, height);
        bootstrap();
    }

    @Override
    public void init() {
        elements.clear();
        createLines(options.getNumber(), null);
    }

    private void createLines(int number, Integer positionX) {
        double minWidth = options.getMinWidth();
        double maxWidth = options.getMaxWidth();
        double minSpeed = options.getMinSpeed();
        double maxSpeed = options.getMaxSpeed();
        double minDegree = options.getMinDegree();
        double maxDegree = options.getMaxDegree();

        while (number-- > 0) {
            double width = Utils.getRandomInRange(minWidth, maxWidth);
            double speed = Utils.randomSpeed(minSpeed, maxSpeed);
            int degree = (int) (Utils.getRandomInRange(minDegree, maxDegree) % 180);
            double x = positionX == null ? Utils.getRandomInRange(0, this.width) : positionX;

            elements.add(new LineElement(x, width, options.getColorSupplier().get(), speed, degree));
        }
    }

    public void onMouseCLick(MouseEvent event) {
        if (!options.isCreateOnClick() || paused) {
            return;
        }

        createLines(options.getNumberOfCreations(), (int) event.x);
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        double hypotenuse = Math.hypot(width, height);
        double lineLength = hypotenuse * 10;
        double delta = getDelta();

        double OC = Math.max(0, options.getOverflowCompensation());

        List<LineElement> toRemove = new LinkedList<>();

        for (LineElement line : elements) {
            double radian = Math.toRadians(-line.getDegree());

            double adjacentSide = 0;
            if (Arrays.stream(specificAngles).noneMatch(angle -> angle == line.getDegree())) {
                adjacentSide = Math.abs(height * 0.5 / Math.tan(radian));
            }

            options.getRenderer().drawLineRotated(-lineLength, 0, lineLength, 0, line.getX(), height * 0.5, line.getWidth(), radian, line.getColor());

            if (!paused) {
                line.setX(line.getX() + line.getSpeed() * delta);
            }

            boolean isOverflow = false;
            boolean isOverflowOnLeft = false;

            if (line.getX() + adjacentSide + line.getWidth() + OC < 0) {
                isOverflow = true;
                isOverflowOnLeft = true;
            } else if (line.getX() > width + adjacentSide + line.getWidth() + OC) {
                isOverflow = true;
            }

            if (isOverflow) {
                if (options.isRemoveOnOverflow() && elements.size() > options.getReservedLines()) {
                    toRemove.add(line);
                } else {
                    line.setSpeed(Math.abs(line.getSpeed()) * (isOverflowOnLeft ? 1 : -1));
                }
            }
        }

        elements.removeAll(toRemove);
    }

    public static abstract class DefaultConfig implements LineOptions {
        private Supplier<Integer> colorSupplier = () -> -65536;
        private int number = 6;
        private double maxWidth = 2;
        private double minWidth = 1;
        private double maxSpeed = 3;
        private double minSpeed = 1;
        private double maxDegree = 90;
        private double minDegree = 80;
        private boolean createOnClick = true;
        private int numberOfCreations = 3;
        private boolean removeOnOverflow = true;
        private double overflowCompensation = 20;
        private int reservedLines = 6;

        @Override
        public Supplier<Integer> getColorSupplier() {
            return colorSupplier;
        }

        public void setColorSupplier(Supplier<Integer> colorSupplier) {
            this.colorSupplier = colorSupplier;
        }

        @Override
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public double getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(double maxWidth) {
            this.maxWidth = maxWidth;
        }

        @Override
        public double getMinWidth() {
            return minWidth;
        }

        public void setMinWidth(double minWidth) {
            this.minWidth = minWidth;
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
        public double getMaxDegree() {
            return maxDegree;
        }

        public void setMaxDegree(double maxDegree) {
            this.maxDegree = maxDegree;
        }

        @Override
        public double getMinDegree() {
            return minDegree;
        }

        public void setMinDegree(double minDegree) {
            this.minDegree = minDegree;
        }

        @Override
        public boolean isCreateOnClick() {
            return createOnClick;
        }

        public void setCreateOnClick(boolean createOnClick) {
            this.createOnClick = createOnClick;
        }

        @Override
        public int getNumberOfCreations() {
            return numberOfCreations;
        }

        public void setNumberOfCreations(int numberOfCreations) {
            this.numberOfCreations = numberOfCreations;
        }

        @Override
        public boolean isRemoveOnOverflow() {
            return removeOnOverflow;
        }

        public void setRemoveOnOverflow(boolean removeOnOverflow) {
            this.removeOnOverflow = removeOnOverflow;
        }

        @Override
        public double getOverflowCompensation() {
            return overflowCompensation;
        }

        public void setOverflowCompensation(double overflowCompensation) {
            this.overflowCompensation = overflowCompensation;
        }

        @Override
        public int getReservedLines() {
            return reservedLines;
        }

        public void setReservedLines(int reservedLines) {
            this.reservedLines = reservedLines;
        }
    }
}
