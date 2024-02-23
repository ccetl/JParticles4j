package de.ccetl.jparticles.util;

import de.ccetl.jparticles.core.shape.ShapeType;
import de.ccetl.jparticles.types.particle.Direction;
import de.ccetl.jparticles.types.particle.ParticleElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Utils {
    private static final Random random = new Random();

    public static double sq(double d) {
        return d * d;
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double getRandomInRange(double min, double max) {
        double range = max - min;
        double scaled = min + random.nextDouble() * range;
        return Math.min(max, scaled);
    }

    public static int generateRandomColor() {
        return (255 << 24) | (random.nextInt(256) << 16) | (random.nextInt(256) << 8) | random.nextInt(256);
    }

    public static Vec2d getSpeed(Direction direction, double min, double max) {
        double x = getRandomInRange(min, max);
        double y = getRandomInRange(min, max);
        switch (direction) {
            case UP:
                return new Vec2d(0, -y);
            case LEFT:
                return new Vec2d(-x, 0);
            case RIGHT:
                return new Vec2d(x, 0);
            case DOWN:
                return new Vec2d(0, y);
            case UP_LEFT:
                return new Vec2d(-x, -y);
            case UP_RIGHT:
                return new Vec2d(x, -y);
            case DOWN_LEFT:
                return new Vec2d(-x, y);
            case DOWN_RIGHT:
                return new Vec2d(x, y);
            case RANDOM:
                return new Vec2d(randomizeDirection(x), randomizeDirection(y));
            default:
                throw new IllegalArgumentException();
        }
    }

    public static double randomSpeed(double min, double max) {
        return randomizeDirection(getRandomInRange(min, max));
    }

    private static int randomizeDirection(double d) {
        return d * Math.random() >= 0.5 ? 1 : -1;
    }

    public static List<Double> fillArrayList(Supplier<Double> value, int size) {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(value.get());
        }
        return list;
    }

    public static boolean intersect(ParticleElement element, ParticleElement other) {
        ShapeType a = element.getShape().getType();
        ShapeType b = other.getShape().getType();

        boolean otherRound = b == ShapeType.CIRCLE || b == ShapeType.STAR;
        if (a == ShapeType.CIRCLE || a == ShapeType.STAR) {
            if (otherRound) {
                return intersectCircleCircle(element, other);
            } else {
                return intersectCircleRectangle(element, other);
            }
        } else {
            if (otherRound) {
                return intersectCircleRectangle(other, element);
            } else {
                return intersectRectangleRectangle(element, other);
            }
        }
    }

    private static boolean intersectCircleCircle(ParticleElement circle, ParticleElement circle1) {
        double dx = sq(getX(circle) - getX(circle1));
        double dy = sq(getY(circle) - getY(circle1));
        double distance = Math.sqrt(dx + dy);
        return distance <= circle.getRadius() + circle1.getRadius();
    }

    public static boolean intersectCircleRectangle(ParticleElement circle, ParticleElement rectangle) {
        double cx = getX(circle);
        double cy = getY(circle);

        double rx = getX(rectangle) - rectangle.getRadius();
        double ry = getY(rectangle) - rectangle.getRadius();
        double rx1 = getX(rectangle) + rectangle.getRadius();
        double ry1 = getY(rectangle) + rectangle.getRadius();

        if (cx >= rx && cx <= rx1 && cy >= ry && cy <= ry1) {
            return true;
        }

        double radiusSq = sq(circle.getRadius());

        return intersectCirclePoint(rx, ry, circle, radiusSq) || intersectCirclePoint(rx, ry1, circle, radiusSq) || intersectCirclePoint(rx1, ry, circle, radiusSq) || intersectCirclePoint(rx1, ry1, circle, radiusSq);
    }

    private static boolean intersectCirclePoint(double x, double y, ParticleElement circle, double radiusSq) {
        double dx = sq(x - getX(circle));
        double dy = sq(y - getY(circle));
        return dx + dy <= radiusSq;
    }

    private static boolean intersectRectangleRectangle(ParticleElement rectangle, ParticleElement rectangle1) {
        double aX = getX(rectangle) - rectangle.getRadius();
        double aY = getY(rectangle) - rectangle.getRadius();
        double aX1 = getX(rectangle) + rectangle.getRadius();
        double aY1 = getY(rectangle) + rectangle.getRadius();

        double bX = getX(rectangle1) - rectangle1.getRadius();
        double bY = getY(rectangle1) - rectangle1.getRadius();
        double bX1 = getX(rectangle1) + rectangle1.getRadius();
        double bY1 = getY(rectangle1) + rectangle1.getRadius();

        return aX <= bX1 && aX1 >= bX && aY <= bY1 && aY1 >= bY;
    }

    public static double getX(ParticleElement element) {
        return element.getX() + element.getParallaxOffsetX();
    }

    public static double getY(ParticleElement element) {
        return element.getY() + element.getParallaxOffsetY();
    }
}
