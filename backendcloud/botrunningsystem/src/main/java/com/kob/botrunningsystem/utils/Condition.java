package com.kob.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Condition {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static  boolean check_tail_increasing(int step) {  // 检验当前回合，蛇的长度是否增加
        if (step <= 10) return true;
        return step % 3 == 1;
    }

    public static  List<Bot.Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);
        List<Bot.Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Bot.Cell(x, y));
        for (int i = 0; i < steps.length(); i ++ ) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Bot.Cell(x, y));
            if (!check_tail_increasing( ++ step)) {
                res.remove(0);
            }
        }
        return res;
    }


    public static int[][] getCondition(String input) {
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for (int i = 0, k = 0; i < 13; i ++ ) {
            for (int j = 0; j < 14; j ++, k ++ ) {
                if (strs[0].charAt(k) == '1') {
                    g[i][j] = 1;
                }
            }
        }

        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Bot.Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Bot.Cell> bCells = getCells(bSx, bSy, strs[6]);

        for (Bot.Cell c: aCells) g[c.x][c.y] = 1;
        for (Bot.Cell c: bCells) g[c.x][c.y] = 1;
        return g;
    }

    public static int getx(String input) {
        String[] strs = input.split("#");

        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Bot.Cell> aCells = getCells(aSx, aSy, strs[3]);
        int x = aCells.get(aCells.size() - 1).x;
        return x;
    }
    public static int gety(String input) {
        String[] strs = input.split("#");

        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);

        List<Bot.Cell> aCells = getCells(aSx, aSy, strs[3]);
        int y = aCells.get(aCells.size() - 1).y;
        return y;
    }
}
