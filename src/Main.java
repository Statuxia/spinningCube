import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Main {

    private static float A, B, C;
    private static double x, y, z;
    private static final double cubeWidth = 20;
    private static final double width = 160, height = 35;
    private static final double[] zBuffer = new double[160 * 35];
    private static final char[] buffer = new char[160 * 35];
    private static final char backgroundASCIICode = ' ';
    private static final int distance = 100;
    private static final float speed = 0.6f;
    private static final float K1 = 40;
    private static double ooz;
    private static int xp, yp;
    private static int idx;

    public static double calculateX(int i, int j, int k) {
        return j * sin(A) * sin(B) * cos(C)
                - k * cos(A) * sin(B) * cos(C)
                + j * cos(A) * sin(C)
                + k * sin(A) * sin(C)
                + i * cos(B) * cos(C);
    }

    public static double calculateY(int i, int j, int k) {
        return j * cos(A) * cos(C)
                + k * sin(A) + cos(C)
                - j * sin(A) * sin(B) * sin(C)
                + k * cos(A) * sin(B) * sin(C)
                - i * cos(B) * sin(C);
    }

    public static double calculateZ(int i, int j, int k) {
        return k * cos(A) * cos(B)
                - j * sin(A) * cos(B)
                + i * sin(B);
    }

    public static void calculateCords(double cubeX, double cubeY, double cubeZ, int ch) {
        x = calculateX((int) cubeX, (int) cubeY, (int) cubeZ);
        y = calculateY((int) cubeX, (int) cubeY, (int) cubeZ);
        z = calculateZ((int) cubeX, (int) cubeY, (int) cubeZ) + distance;

        ooz = 1 / z;

        xp = (int) (width / 2 + K1 * ooz * x * 2);
        yp = (int) (height / 2 + K1 * ooz * y);

        idx = (int) (xp + yp * width);
        if (idx >= 0 && idx < width * height) {
            if (ooz > zBuffer[idx]) {
                zBuffer[idx] = ooz;
                buffer[idx] = (char) ch;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        StringBuilder builder = new StringBuilder();
        while (true) {
            Arrays.fill(buffer, backgroundASCIICode);
            Arrays.fill(zBuffer, 0);
            for (double cubeX = -cubeWidth; cubeX < cubeWidth; cubeX += speed) {
                for (double cubeY = -cubeWidth; cubeY < cubeWidth; cubeY += speed) {
                    calculateCords(cubeX, cubeY, -cubeWidth, '.');
                    calculateCords(cubeWidth, cubeY, cubeX, '$');
                    calculateCords(-cubeWidth, cubeY, -cubeX, '~');
                    calculateCords(-cubeX, cubeY, cubeWidth, '#');
                    calculateCords(cubeX, cubeWidth, cubeY, '-');
                }
            }

            for (int k = 0; k < width * height; k++) {
                builder.append(buffer[k]);
                if ((k + 1) % width == 0) {
                    builder.append("\n");
                }
            }
            builder.append("\n\n\n\n\n");
            System.out.println(builder);
            builder.setLength(0);

            A += 0.02;
            B += 0.02;

            Thread.sleep(1);
        }
    }
}