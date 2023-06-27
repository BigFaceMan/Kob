package com.kob.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Bot implements com.kob.botrunningsystem.utils.BotInterface {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }


    @Override
    public Integer nextMove(int[][] g, int x, int y) {
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for (int i = 0; i < 4; i ++ ) {
            int xx = x + dx[i];
            int yy = y + dy[i];
            if (xx >= 0 && xx < 13 && yy >= 0 && yy < 14 && g[xx][yy] == 0) {
                return i;
            }
        }
        return 0;
    }
}
