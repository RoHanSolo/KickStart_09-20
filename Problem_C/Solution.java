import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.util.Set;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.HashSet;
import java.io.Writer;
import java.io.OutputStreamWriter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author krishna.kota
 */
public class Solution {
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        OutputStream outputStream = System.out;
        InputReader in = new InputReader(inputStream);
        OutputWriter out = new OutputWriter(outputStream);
        PaintersDual solver = new PaintersDual();
        int testCount = Integer.parseInt(in.next());
        for (int i = 1; i <= testCount; i++)
            solver.solve(i, in, out);
        out.close();
    }

    static class PaintersDual {
        int[][] odd = {{0, 1}, {0, -1}, {1, 1}};
        int[][] even = {{0, 1}, {0, -1}, {-1, -1}};

        public void solve(int testNumber, InputReader in, OutputWriter out) {
            int s = in.nextInt(), ra = in.nextInt(), pa = in.nextInt(), rb = in.nextInt(), pb = in.nextInt(), c = in.nextInt();
            Set<String> blocked = new HashSet<>();

            for (int j = 0; j < c; j++) {
                int rc = in.nextInt();
                int pc = in.nextInt();
                blocked.add(rc + "-" + pc);
            }

            String alma = ra + "-" + pa;
            String berthe = rb + "-" + pb;
            blocked.add(alma);
            blocked.add(berthe);
            int ans = calcScore(blocked, s, alma, berthe, true, 0);
//        System.out.println("--------------");
            out.println("Case #" + testNumber + ": " + ans);
        }

        private String move(String str, int[] arr) {
            int[] par = parse(str);
            int r = par[0] + arr[0];
            int p = par[1] + arr[1];
            return r + "-" + p;
        }

        private boolean check(String str, int s) {
            int[] arr = parse(str);
            int r = arr[0];
            int p = arr[1];
            if (r < 1 || r > s || p < 1 || p > 2 * r - 1) {
                return false;
            }
            return true;
        }

        private int getP(String str) {
            return parse(str)[1];
        }

        private int[] parse(String str) {
            String[] strs = str.split("-");
            int r = Integer.parseInt(strs[0]);
            int p = Integer.parseInt(strs[1]);
            return new int[]{r, p};
        }

        private boolean isPossible(String some, int s, Set<String> blocked) {
            int[][] arr = getP(some) % 2 == 0 ? even : odd;
            for (int i = 0; i < 3; i++) {
                String m = move(some, arr[i]);
                if (check(m, s) && !blocked.contains(m)) {
                    return true;
                }
            }
            return false;
        }

        private int calcScore(Set<String> blocked, int s, String alma, String berthe, boolean turn, int score) {
            if (turn) {
                // alma
                int ans = Integer.MIN_VALUE;
                int[][] arr = getP(alma) % 2 == 0 ? even : odd;
                boolean moved = false;
//            System.out.println(turn + " " + alma + " " + berthe + " " + getP(alma) + blocked);
                for (int i = 0; i < 3; i++) {
                    String al = move(alma, arr[i]);
                    if (check(al, s) && !blocked.contains(al)) {
                        blocked.add(al);
                        moved = true;
                        ans = Math.max(ans, calcScore(blocked, s, al, berthe, !turn, score + 1));
                        blocked.remove(al);
                    }
                }

                if (!moved) {
                    if (isPossible(berthe, s, blocked)) {
                        ans = Math.max(ans, calcScore(blocked, s, alma, berthe, !turn, score));
                    } else {
                        ans = score;
                    }
                }
//            System.out.println("a " + alma + " " + ans);
                return ans;
            } else {
                int ans = Integer.MAX_VALUE;
                int[][] arr = getP(berthe) % 2 == 0 ? even : odd;

//            System.out.println(turn + " " + alma + " " + berthe + " " + getP(berthe) + blocked);

                boolean moved = false;
                for (int i = 0; i < 3; i++) {
                    String be = move(berthe, arr[i]);
                    if (check(be, s) && !blocked.contains(be)) {
                        blocked.add(be);
                        moved = true;
                        ans = Math.min(ans, calcScore(blocked, s, alma, be, !turn, score - 1));
                        blocked.remove(be);
                    }
                }
                if (!moved) {
                    if (isPossible(alma, s, blocked)) {
                        ans = Math.min(ans, calcScore(blocked, s, alma, berthe, !turn, score));
                    } else {
                        ans = score;
                    }
                }
//            System.out.println("b " + berthe + " " + ans);
                return ans;
            }
        }

    }

    static class OutputWriter {
        private final PrintWriter writer;

        public OutputWriter(OutputStream outputStream) {
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
        }

        public OutputWriter(Writer writer) {
            this.writer = new PrintWriter(writer);
        }

        public void print(Object... objects) {
            for (int i = 0; i < objects.length; i++) {
                if (i != 0) {
                    writer.print(' ');
                }
                writer.print(objects[i]);
            }
        }

        public void println(Object... objects) {
            print(objects);
            writer.println();
        }

        public void close() {
            writer.close();
        }

    }

    static class InputReader {
        private InputStream stream;
        private byte[] buf = new byte[1024];
        private int curChar;
        private int numChars;
        private InputReader.SpaceCharFilter filter;

        public InputReader(InputStream stream) {
            this.stream = stream;
        }

        public int read() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars <= 0) {
                    return -1;
                }
            }
            return buf[curChar++];
        }

        public int nextInt() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = read();
            }
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res *= 10;
                res += c - '0';
                c = read();
            } while (!isSpaceChar(c));
            return res * sgn;
        }

        public String nextString() {
            int c = read();
            while (isSpaceChar(c)) {
                c = read();
            }
            StringBuilder res = new StringBuilder();
            do {
                if (Character.isValidCodePoint(c)) {
                    res.appendCodePoint(c);
                }
                c = read();
            } while (!isSpaceChar(c));
            return res.toString();
        }

        public boolean isSpaceChar(int c) {
            if (filter != null) {
                return filter.isSpaceChar(c);
            }
            return isWhitespace(c);
        }

        public static boolean isWhitespace(int c) {
            return c == ' ' || c == '\n' || c == '\r' || c == '\t' || c == -1;
        }

        public String next() {
            return nextString();
        }

        public interface SpaceCharFilter {
            public boolean isSpaceChar(int ch);

        }

    }
}


