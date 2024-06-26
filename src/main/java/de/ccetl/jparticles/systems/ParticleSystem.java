package de.ccetl.jparticles.systems;

import de.ccetl.jparticles.core.ParticleBase;
import de.ccetl.jparticles.core.shape.Shape;
import de.ccetl.jparticles.core.shape.ShapeType;
import de.ccetl.jparticles.types.particle.*;
import de.ccetl.jparticles.util.Utils;
import de.ccetl.jparticles.util.Vec2d;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ParticleSystem extends ParticleBase<ParticleOptions, ParticleElement> {
    private static final double HALF_PI = 0.5 * Math.PI;
    private LineShapeMaker lineShapeMaker;
    private long lastTrailUpdate;
    private double positionX;
    private double positionY;

    public ParticleSystem(ParticleOptions defaultConfig, int width, int height) {
        super(defaultConfig, width, height);
        bootstrap();
    }

    @Override
    public void init() {
        if (this.options.getRange() > 0) {
            this.positionX = Math.random() * width;
            this.positionY = Math.random() * height;
            this.defineLineShape();
        }

        elements.clear();
        createDots();
        lastTrailUpdate = System.currentTimeMillis();
    }

    private void defineLineShape() {
        double proximity = this.options.getProximity();
        double range = this.options.getRange();
        LineShape lineShape = this.options.getLineShape();

        if (lineShape == LineShape.CUBE) {
            this.lineShapeMaker = (x, y, sx, sy, cb) -> {
                double positionX = this.positionX;
                double positionY = this.positionY;
                if (Math.abs(x - sx) <= proximity && Math.abs(y - sy) <= proximity && Math.abs(x - positionX) <= range && Math.abs(y - positionY) <= range && Math.abs(sx - positionX) <= range && Math.abs(sy - positionY) <= range) {
                    cb.run();
                }
            };
        } else {
            this.lineShapeMaker = (x, y, sx, sy, cb) -> {
                double positionX = this.positionX;
                double positionY = this.positionY;
                if (Math.abs(x - sx) <= proximity && Math.abs(y - sy) <= proximity && ((Math.abs(x - positionX) <= range && Math.abs(y - positionY) <= range) || (Math.abs(sx - positionX) <= range && Math.abs(sy - positionY) <= range))) {
                    cb.run();
                }
            };
        }
    }

    private void createDots() {
        for (int i = elements.size(); i < options.getNumber(); i++) {
            createDot(false);
        }
    }

    private void createDot(boolean force) {
        double r = Utils.getRandomInRange(options.getMinRadius(), options.getMaxRadius());
        double minX = r;
        double maxX = width - r;
        double minY = r;
        double maxY = height - r;
        switch (options.getSpawnRegion()) {
            case ABOVE_RIGHT:
            case BELLOW_RIGHT:
            case RIGHT:
                minX += width;
                maxX += width;
                break;
            case ABOVE_LEFT:
            case BELLOW_LEFT:
            case LEFT:
                minX -= width;
                maxX -= width;
                break;
        }
        switch (options.getSpawnRegion()) {
            case ABOVE_LEFT:
            case ABOVE_RIGHT:
            case ABOVE:
                minY -= height;
                maxY -= height;
                break;
            case BELLOW:
            case BELLOW_LEFT:
            case BELLOW_RIGHT:
                minY += height;
                maxY += height;
                break;
        }
        Vec2d speed = Utils.getSpeed(options.getDirection(), options.getMinSpeed(), options.getMaxSpeed());
        ParticleElement dot = new ParticleElement(r, Utils.getRandomInRange(minX, maxX), Utils.getRandomInRange(minY, maxY), speed.getX(), speed.getY(), options.getColorSupplier().get(), options.getShapeSupplier().get(), options.getParallaxLayer()[new Random().nextInt(options.getParallaxLayer().length)], 0, 0, Utils.getRandomInRange(options.getMinSpinSpeed(), options.getMaxSpinSpeed()), options.isSpin() ? Utils.getRandomInRange(0, 360) : 0);

        if (!force && options.getCollisionIntern() != Obstacle.IGNORE && elements.stream().anyMatch(dot1 -> Utils.intersect(dot, dot1))) {
            try {
                createDot(false);
            } catch (StackOverflowError e) {
                createDot(true);
            }
        }

        this.elements.add(dot);
    }

    @Override
    public void draw(double mouseX, double mouseY) {
        positionX = mouseX;
        positionY = mouseY;
        double delta = getDelta();

        List<ParticleElement> toRemove = new LinkedList<>();
        boolean rotate = options.isSpin();
        if (!paused) {
            for (ParticleElement dot : elements) {
                updateXY(dot, toRemove, mouseX, mouseY, delta);
                if (rotate) {
                    dot.updateRotation(delta);
                }
            }
        }
        elements.removeAll(toRemove);

        boolean trails = options.isTrails();
        long time = 0L;
        double alive = 0D;
        boolean create = false;
        if (trails) {
            time = System.currentTimeMillis();
            alive = options.getTrailAlive();
            create = time - lastTrailUpdate >= options.getTrailUpdate();
        }

        boolean ignore = options.getCollisionEdge() != Obstacle.IGNORE || options.getCollisionIntern() != Obstacle.IGNORE;
        boolean connect = options.getRange() > 0;
        for (ParticleElement dot : elements) {
            if (options.getCollisionIntern() == Obstacle.BOUNCE) {
                handleInternCollision(dot);
            }

            if (ignore) {
                dot.setVelocityChanged(false);
            }

            if (trails) {
                drawTrails(dot, alive, time, create);
            }

            double x = dot.getX() + dot.getParallaxOffsetX();
            double y = dot.getY() + dot.getParallaxOffsetY();
            dot.getShape().render(options.getRenderer(), new Vec2d(x, y), dot.getRadius(), dot.getRotation(), dot.getColor());

            if (connect) {
                connectDots(dot);
            }
        }
        if (trails) {
            lastTrailUpdate = time;
        }

        createDots();
    }

    private void drawTrails(ParticleElement dot, double alive, long time, boolean create) {
        LinkedList<Trail> trail = dot.getTrail();

        if (create) {
            trail.add(new Trail(Utils.getX(dot), Utils.getY(dot), time, dot.getRotation()));
        }

        int length = trail.size();
        if (length == 0) {
            return;
        }

        if (time - trail.peek().getCreation() > alive) {
            trail.poll();
            if (length == 1) {
                return;
            }
        }

        int dotColor = dot.getColor();
        double alpha = (dotColor >> 24) & 0xFF;
        double size = dot.getRadius() + dot.getRadius();

        for (Trail t : trail) { // TODO maybe reverse draw order
            double p = 1 - (time - t.getCreation()) / alive;
            if (p <= 0 || p > 1) {
                continue;
            }

            int modified = (int) Math.round(p * alpha);

            double modifiedSize = size;
            if (options.isTrailShrink()) {
                modifiedSize *= p;
            }

            dot.getShape().render(options.getRenderer(), t, modifiedSize * 0.5, t.getRotation(), (modified << 24) | (dotColor & 0x00FFFFFF));
        }
    }

    private void connectDots(ParticleElement dot) {
        Vec2d vec = new Vec2d(Utils.getX(dot), Utils.getY(dot));

        for (ParticleElement dot1 : elements) {
            if (dot == dot1) {
                continue;
            }

            Vec2d vec1 = new Vec2d(Utils.getX(dot1), Utils.getY(dot1));

            lineShapeMaker.apply(vec.getX(), vec.getY(), vec1.getX(), vec1.getY(), () -> {
                double x;
                double y;
                double x1;
                double y1;

                if (options.isCenterLines()) {
                    x = vec.getX();
                    y = vec.getY();
                    x1 = vec1.getX();
                    y1 = vec1.getY();
                } else {
                    Vec2d vec2 = vec.copy().set(vec);

                    Vec2d deltaVec = vec1.copy().subtract(vec2);
                    double rotation = Math.PI - Math.atan2(deltaVec.getX(), deltaVec.getY());
                    double correctedRotation = rotation - HALF_PI;
                    double correctedRotation1 = rotation + HALF_PI;

                    x = vec.getX() + Math.cos(correctedRotation) * dot.getRadius();
                    y = vec.getY() + Math.sin(correctedRotation) * dot.getRadius();
                    x1 = vec1.getX() + Math.cos(correctedRotation1) * dot1.getRadius();
                    y1 = vec1.getY() + Math.sin(correctedRotation1) * dot1.getRadius();
                }

                options.getRenderer().drawLine(x, y, x1, y1, options.getLineWidth(), dot.getColor());
            });
        }
    }

    private void updateXY(ParticleElement dot, List<ParticleElement> toRemove, double mouseX, double mouseY, double delta) {
        boolean parallax = this.options.isParallax();
        double parallaxStrength = this.options.getParallaxStrength();

        dot.setX(dot.getX() + dot.getVx() * delta);
        dot.setY(dot.getY() + dot.getVy() * delta);

        if (parallax) {
            double divisor = parallaxStrength * dot.getParallaxLayer();
            double parallaxOffsetX = (mouseX / divisor - dot.getParallaxOffsetX()) / 10;
            double parallaxOffsetY = (mouseY / divisor - dot.getParallaxOffsetY()) / 10;
            double newX = dot.getX() + parallaxOffsetX;
            double newY = dot.getY() + parallaxOffsetY;

            if (options.getCollisionEdge() == Obstacle.IGNORE || newX - dot.getRadius() >= 0 && newX + dot.getRadius() <= width) {
                dot.setParallaxOffsetX(parallaxOffsetX);
            } else {
                double minX = -dot.getX() + dot.getRadius();
                double maxX = width - dot.getX() - dot.getRadius();
                dot.setParallaxOffsetX(Utils.clamp(parallaxOffsetX, minX, maxX));
            }

            if (options.getCollisionEdge() == Obstacle.IGNORE || newY - dot.getRadius() >= 0 && newY + dot.getRadius() <= height) {
                dot.setParallaxOffsetY(parallaxOffsetY);
            } else {
                double minY = -dot.getY() + dot.getRadius();
                double maxY = height - dot.getY() - dot.getRadius();
                dot.setParallaxOffsetY(Utils.clamp(parallaxOffsetY, minY, maxY));
            }
        }

        if (options.isHoverRepulse()) {
            Vec2d deltaVec = new Vec2d(Utils.getX(dot), Utils.getY(dot)).subtract(new Vec2d(mouseX, mouseY));
            double distance = deltaVec.getLength();

            if (distance < options.getRepulseRadius()) {
                dot.set(deltaVec.add(dot));
            }
        }

        double r = dot.getRadius();
        double x = dot.getX();
        double y = dot.getY();
        x += dot.getParallaxOffsetX();
        y += dot.getParallaxOffsetY();

        boolean movingOutHorizontal = (dot.getVx() > 0 && (x + r >= width)) || (dot.getVx() < 0 && (x - r <= 0));
        boolean movingOutVertical = (dot.getVy() > 0 && (y + r >= height)) || (dot.getVy() < 0 && (y - r <= 0));

        if (movingOutHorizontal && options.getCollisionEdge() == Obstacle.BOUNCE) {
            dot.setVx(-dot.getVx());
            dot.setVelocityChanged(true);
        } else {
            Trail trail = dot.getTrail().peek();
            if (movingOutHorizontal && (x + r < 0 || x - r > width) && dot.getTrail().isEmpty() && options.getCollisionEdge() == Obstacle.IGNORE && (trail == null || trail.getX() + r < 0 || trail.getX() - r > width)) {
                toRemove.add(dot);
            }
        }

        if (movingOutVertical && options.getCollisionEdge() == Obstacle.BOUNCE) {
            dot.setVy(-dot.getVy());
            dot.setVelocityChanged(true);
        } else {
            Trail trail = dot.getTrail().peek();
            if (movingOutVertical && (y + r < 0 || y - r > height) && options.getCollisionEdge() == Obstacle.IGNORE && (trail == null || trail.getY() + r < 0 || trail.getY() - r > height)) {
                toRemove.add(dot);
            }
        }
    }

    private void handleInternCollision(ParticleElement dot) {
        for (ParticleElement dot1 : elements) {
            if (dot1 == dot || dot1.isVelocityChanged() && dot.isVelocityChanged() || !Utils.intersect(dot, dot1)) {
                continue;
            }

            dot.handleCollision(dot1);
        }
    }

    //    public void onMouseClick(MouseEvent event) {
    //
    //    }

    private interface LineShapeMaker {
        void apply(double x, double y, double sx, double sy, Runnable cb);
    }

    public static abstract class DefaultConfig implements ParticleOptions {
        private Supplier<Shape> shapeSupplier = () -> new Shape(ShapeType.CIRCLE);
        private Supplier<Integer> colorSupplier = () -> -65536;
        private Obstacle collisionIntern = Obstacle.IGNORE;
        private Obstacle collisionEdge = Obstacle.BOUNCE;
        private Direction direction = Direction.RANDOM;
        private SpawnRegion spawnRegion = SpawnRegion.INSIDE;
        private int number = 20;
        private double maxRadius = 2.4;
        private double minRadius = 0.6;
        private double minSpeed = 0.1;
        private double maxSpeed = 1;
        private double proximity = 0.2;
        private double range = 0.2;
        private double lineWidth = 0.2;
        private LineShape lineShape = LineShape.SPIDER;
        private boolean centerLines = false;
        private boolean parallax = false;
        private int[] parallaxLayer = {1, 2, 3};
        private double parallaxStrength = 3;
        private boolean hoverRepulse = false;
        private double repulseRadius = 100;
        private boolean trails = false;
        private double trailUpdate = 20;
        private double trailAlive = 1000;
        private boolean trailShrink = true;
        private boolean spin = false;
        private double minSpinSpeed = 1;
        private double maxSpinSpeed = 5;

        @Override
        public Supplier<Shape> getShapeSupplier() {
            return shapeSupplier;
        }

        public void setShapeSupplier(Supplier<Shape> shapeSupplier) {
            this.shapeSupplier = shapeSupplier;
        }

        @Override
        public Supplier<Integer> getColorSupplier() {
            return colorSupplier;
        }

        public void setColorSupplier(Supplier<Integer> colorSupplier) {
            this.colorSupplier = colorSupplier;
        }

        @Override
        public Obstacle getCollisionIntern() {
            return collisionIntern;
        }

        public void setCollisionIntern(Obstacle collisionIntern) {
            this.collisionIntern = collisionIntern;
        }

        @Override
        public Obstacle getCollisionEdge() {
            return collisionEdge;
        }

        @Override
        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public void setCollisionEdge(Obstacle collisionEdge) {
            this.collisionEdge = collisionEdge;
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
        public double getMinSpeed() {
            return minSpeed;
        }

        public void setMinSpeed(double minSpeed) {
            this.minSpeed = minSpeed;
        }

        @Override
        public double getMaxSpeed() {
            return maxSpeed;
        }

        public void setMaxSpeed(double maxSpeed) {
            this.maxSpeed = maxSpeed;
        }

        @Override
        public double getProximity() {
            return proximity;
        }

        public void setProximity(double proximity) {
            this.proximity = proximity;
        }

        @Override
        public double getRange() {
            return range;
        }

        public void setRange(double range) {
            this.range = range;
        }

        @Override
        public double getLineWidth() {
            return lineWidth;
        }

        public void setLineWidth(double lineWidth) {
            this.lineWidth = lineWidth;
        }

        @Override
        public LineShape getLineShape() {
            return lineShape;
        }

        public void setLineShape(LineShape lineShape) {
            this.lineShape = lineShape;
        }

        @Override
        public boolean isParallax() {
            return parallax;
        }

        public void setParallax(boolean parallax) {
            this.parallax = parallax;
        }

        @Override
        public int[] getParallaxLayer() {
            return parallaxLayer;
        }

        public void setParallaxLayer(int[] parallaxLayer) {
            this.parallaxLayer = parallaxLayer;
        }

        @Override
        public double getParallaxStrength() {
            return parallaxStrength;
        }

        public void setParallaxStrength(double parallaxStrength) {
            this.parallaxStrength = parallaxStrength;
        }

        @Override
        public boolean isCenterLines() {
            return centerLines;
        }

        public void setCenterLines(boolean centerLines) {
            this.centerLines = centerLines;
        }

        @Override
        public SpawnRegion getSpawnRegion() {
            return spawnRegion;
        }

        public void setSpawnRegion(SpawnRegion spawnRegion) {
            this.spawnRegion = spawnRegion;
        }

        @Override
        public boolean isHoverRepulse() {
            return hoverRepulse;
        }

        public void setHoverRepulse(boolean hoverRepulse) {
            this.hoverRepulse = hoverRepulse;
        }

        @Override
        public double getRepulseRadius() {
            return repulseRadius;
        }

        public void setRepulseRadius(double repulseRadius) {
            this.repulseRadius = repulseRadius;
        }

        @Override
        public boolean isTrails() {
            return trails;
        }

        public void setTrails(boolean trails) {
            this.trails = trails;
        }

        @Override
        public double getTrailUpdate() {
            return trailUpdate;
        }

        public void setTrailUpdate(double trailUpdate) {
            this.trailUpdate = trailUpdate;
        }

        @Override
        public double getTrailAlive() {
            return trailAlive;
        }

        public void setTrailAlive(double trailAlive) {
            this.trailAlive = trailAlive;
        }

        @Override
        public boolean isTrailShrink() {
            return trailShrink;
        }

        public void setTrailShrink(boolean trailShrink) {
            this.trailShrink = trailShrink;
        }

        @Override
        public boolean isSpin() {
            return spin;
        }

        public void setSpin(boolean spin) {
            this.spin = spin;
        }

        @Override
        public double getMaxSpinSpeed() {
            return maxSpinSpeed;
        }

        @Override
        public double getMinSpinSpeed() {
            return minSpinSpeed;
        }

        public void setMinSpinSpeed(double minSpinSpeed) {
            this.minSpinSpeed = minSpinSpeed;
        }

        public void setMaxSpinSpeed(double maxSpinSpeed) {
            this.maxSpinSpeed = maxSpinSpeed;
        }
    }
}
