import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class GameBoard extends JFrame {
    private static final int SIZE = 8; // 定义棋盘大小
    private JPanel[][] squares = new JPanel[SIZE][SIZE]; // JPanel数组存储颜色块
    private Color[][] colors = new Color[SIZE][SIZE]; // 存储颜色数据

    public GameBoard(boolean sort) {
        setTitle(sort ? "Sorted Colors" : "Unsorted Colors");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(SIZE, SIZE));
        initializeColors();
        initializeBoard();
        if (sort) {
            sortColors(); // 如果是排序窗口，则进行排序
        }
    }

    private void initializeColors() {
    Random random = new Random();
    // 生成颜色数组
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                // 生成仅包含红色和蓝色的随机颜色
                int red = random.nextInt(256);
                int blue = random.nextInt(256);
                colors[i][j] = new Color(red, 0, blue);
                // 打印每个颜色的行、列和RGB值
                System.out.printf("Row: %d, Col: %d, Color: R%d B%d%n", i, j, red, blue);
            }
        }
    }


    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                squares[i][j] = new JPanel();
                squares[i][j].setBackground(colors[i][j]);
                add(squares[i][j]);
            }
        }
    }

    public void sortColors() {
        // 扁平化索引和颜色数组，以便进行排序
        Color[] flatColors = new Color[SIZE * SIZE];
        int index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                flatColors[index++] = colors[i][j];
            }
        }

        // 归并排序
        mergeSort(flatColors, 0, flatColors.length - 1);

        // 更新界面
        index = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                colors[i][j] = flatColors[index];
                squares[i][j].setBackground(colors[i][j]);
                index++;
            }
        }
    }

    private void mergeSort(Color[] array, int left, int right) {
        if (left < right) {
            int middle = (left + right) / 2;
            mergeSort(array, left, middle);
            mergeSort(array, middle + 1, right);
            merge(array, left, middle, right);
        }
    }

    private void merge(Color[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        Color[] L = new Color[n1];
        Color[] R = new Color[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, middle + 1, R, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (colorValue(L[i]) <= colorValue(R[j])) {
                array[k] = L[i];
                i++;
            } else {
                array[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            array[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            array[k] = R[j];
            j++;
            k++;
        }
    }

    private int colorValue(Color color) {
        // 使用红色和蓝色值的和作为排序依据
        return color.getRed() + color.getBlue();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameBoard unsortedBoard = new GameBoard(false);
            unsortedBoard.setVisible(true);
            GameBoard sortedBoard = new GameBoard(true);
            sortedBoard.setLocation(unsortedBoard.getX() + unsortedBoard.getWidth(), unsortedBoard.getY());
            sortedBoard.setVisible(true);
        });
    }
}
