import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
import java.io.*;
import java.util.Comparator;

public class ProgAss1 implements Comparable<ProgAss1> {
    public int compareTo(ProgAss1 that) {
        if (this.y < that.y)
            return -1;
        if (this.y > that.y)
            return +1;
        if (this.x < that.x)
            return -1;
        if (this.x > that.x)
            return +1;
        return 0;
    }

    private double x;
    private double y;

    private Stack<ProgAss1> hull = new Stack<ProgAss1>();

    public ProgAss1(double x, double y) {
        if (Double.isInfinite(x) || Double.isInfinite(y))
            System.out.println("Coordinates are not finite");
        if (Double.isNaN(x) || Double.isNaN(y))
            System.out.println("Coordinates are NaN");
        if (x == 0.0)
            this.x = 0.0;
        else
            this.x = x;

        if (y == 0.0)
            this.y = 0.0;
        else
            this.y = y;
    }

    public static int checkForLeftTurn(ProgAss1 a, ProgAss1 b, ProgAss1 c) {
        double area2 = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
        if (area2 < 0)
            return -1;
        else if (area2 > 0)
            return +1;
        else
            return 0;
    }

    public Comparator<ProgAss1> polarOrder() {
        return new PolarOrder();
    }

    private class PolarOrder implements Comparator<ProgAss1> {
        public int compare(ProgAss1 p1, ProgAss1 p2) {
            double newX1 = p1.x - x, newY1 = p1.y - y, newX2 = p2.x - x, newY2 = p2.y - y;

            if (newY1 >= 0 && newY2 < 0) {
                return -1;
            } else if (newY2 >= 0 && newY1 < 0) {
                return +1;
            } else if (newY1 == 0 && newY2 == 0) {
                if (newX1 >= 0 && newX2 < 0) {
                    return -1;
                } else if (newX2 >= 0 && newX1 < 0) {
                    return +1;
                } else {
                    return 0;
                }
            } else
                return -checkForLeftTurn(ProgAss1.this, p1, p2);
        }
    }

    public ProgAss1(ProgAss1[] points) {
        if (points == null) {
            System.out.println("null arg");
            return;
        }
        if (points.length == 0) {
            System.out.println("length 0 for points");
            return;
        }

        int n = points.length;
        ProgAss1[] a = new ProgAss1[n];
        for (int i = 0; i < n; i++) {
            if (points[i] == null) {
                System.out.println("points[" + i + "] is null");
                break;
            }
            a[i] = points[i];
        }

        Arrays.sort(a);
        Arrays.sort(a, 1, n, a[0].polarOrder());

        hull.push(a[0]);
        int k1;
        for (k1 = 1; k1 < n; k1++)
            if (!a[0].equals(a[k1]))
                break;
        if (k1 == n)
            return;

        int k2;
        for (k2 = k1 + 1; k2 < n; k2++)
            if (ProgAss1.checkForLeftTurn(a[0], a[k1], a[k2]) != 0)
                break;
        hull.push(a[k2 - 1]);

        for (int i = k2; i < n; i++) {
            ProgAss1 top = hull.pop();
            while (ProgAss1.checkForLeftTurn(hull.peek(), top, a[i]) <= 0) {
                top = hull.pop();
            }
            hull.push(top);
            hull.push(a[i]);
        }

        assert isFullConvex();
    }

    public Iterable<ProgAss1> hull() {
        Stack<ProgAss1> s = new Stack<ProgAss1>();
        for (ProgAss1 p : hull)
            s.push(p);
        return s;
    }

    private boolean isFullConvex() {
        int n = hull.size();
        if (n <= 2)
            return true;

        ProgAss1[] points = new ProgAss1[n];
        int k = 0;
        for (ProgAss1 p : hull()) {
            points[k++] = p;
        }

        for (int i = 0; i < n; i++) {
            if (ProgAss1.checkForLeftTurn(points[i], points[(i + 1) % n], points[(i + 2) % n]) <= 0) {
                return false;
            }
        }
        return true;
    }

    public static int getLines(String fileName) {
        int length = 0;
        String line = null;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            while ((line = bufferedReader.readLine()) != null) {
                length++;
            }
            bufferedReader.close();
            return length;
        } catch (IOException ex) {
            System.out.println("cant read file '" + ex + "'");
            return 0;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter fileName : ");

        String fileName = sc.nextLine();
        sc.close();
        String line = null;
        int numberOfLines = ProgAss1.getLines(fileName);

        ProgAss1[] points = new ProgAss1[numberOfLines];

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            int n = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String coordinates[] = line.split(" ");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                points[n] = new ProgAss1(x, y);
                n++;
            }
            bufferedReader.close();
        } catch (IOException ex) {
            System.out.println("cant read file '" + fileName + "'");
        }
        ProgAss1 graham = new ProgAss1(points);
        for (ProgAss1 p : graham.hull())
            System.out.println("(" + p.x + "," + p.y + ")");
    }

}