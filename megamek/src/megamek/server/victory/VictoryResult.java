/*
 * MegaMek - Copyright (C) 2007-2008 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
package megamek.server.victory;

import megamek.common.IPlayer;
import megamek.common.Report;
import megamek.common.eloRating.IPlayerEloRating;
import megamek.common.eloRating.PlayerEloRating;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * quick implementation of a Victory.Result stores player scores and a flag if
 * game-ending victory is achieved or not
 */
public class VictoryResult implements IResult {
    protected boolean victory;
    protected Throwable tr;
    protected ArrayList<Report> reports = new ArrayList<>();
    protected HashMap<Integer, Double> playerScore = new HashMap<>();
    protected HashMap<Integer, Double> teamScore = new HashMap<>();
    protected double hiScore = 0;

    protected VictoryResult(boolean win) {
        this.victory = win;
        tr = new Throwable();
    }
    
    protected VictoryResult(boolean win, int player, int team) {
    	this.victory = win;
    	tr = new Throwable();
        if (player != IPlayer.PLAYER_NONE) {
            addPlayerScore(player, 1.0);
        }
        if (team != IPlayer.TEAM_NONE) {
            addTeamScore(team, 1.0);
        }
    }
    
    protected static VictoryResult noResult() {
    	return new VictoryResult(false, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
    }
    
    protected static VictoryResult drawResult() {
        return new VictoryResult(true, IPlayer.PLAYER_NONE, IPlayer.TEAM_NONE);
    }

    private int getWinner(HashMap<Integer, Double> score, int defaultId) {
        double max = Double.MIN_VALUE;
        int maxWinnerId = defaultId;
        boolean draw = false;

        for (Map.Entry<Integer, Double> entry : score.entrySet()) {
            if (entry.getValue() == max) {
                draw = true;
            }
            if (entry.getValue() > max) {
                draw = false;
                max = entry.getValue();
                maxWinnerId = entry.getKey();
            }
        }

        if (draw)
            return defaultId;
        return maxWinnerId;
    }


    public int getWinningPlayer() {
        return getWinner(playerScore, IPlayer.PLAYER_NONE);
    }

    public int getWinningTeam() {
        return getWinner(teamScore, IPlayer.TEAM_NONE);
    }

    public void updateEloRatings(Vector<IPlayer> players) {
        IPlayerEloRating playerEloRating = new PlayerEloRating();
        playerEloRating.updateRatings(this, players);
    }

    protected void updateHiScore() {
        // used to calculate winner
        hiScore = Double.MIN_VALUE;
        for (Map.Entry<Integer, Double> entry : playerScore.entrySet()) {
            double score = entry.getValue();
            if (score > hiScore) {
                hiScore = score;
            }
        }

        for (Map.Entry<Integer, Double> entry : teamScore.entrySet()) {
            double score = entry.getValue();
            if (score > hiScore) {
                hiScore = score;
            }
        }
    }

    public void addPlayerScore(int id, double score) {
        playerScore.put(id, score);
        updateHiScore();
    }

    public void addTeamScore(int id, double score) {
        teamScore.put(id, score);
        updateHiScore();
    }

    public boolean isWinningPlayer(int id) {
        double d = getPlayerScore(id);
        // two decimal compare..
        return ((d * 100) % 100) == ((hiScore * 100) % 100);
    }

    public boolean isWinningTeam(int id) {
        double d = getTeamScore(id);
        // two decimal compare..
        return ((d * 100) % 100) == ((hiScore * 100) % 100);
    }

    public boolean victory() {
        return victory;
    }

    public void setVictory(boolean b) {
        this.victory = b;
    }

    public double getPlayerScore(int id) {
        if (playerScore.get(id) == null)
            return 0.0;
        return playerScore.get(id);
    }

    public int[] getPlayers() {
        return intify(playerScore.keySet().toArray(new Integer[0]));
    }

    public double getTeamScore(int id) {
        if (teamScore.get(id) == null)
            return 0.0;
        return teamScore.get(id);
    }

    public int[] getTeams() {
        return intify(teamScore.keySet().toArray(new Integer[0]));
    }

    public void addReport(Report r) {
        reports.add(r);
    }

    public ArrayList<Report> getReports() {
        return reports;
    }

    protected String getTrace() {
        StringWriter sw = new StringWriter();
        PrintWriter pr = new PrintWriter(sw);
        tr.printStackTrace(pr);
        pr.flush();
        return sw.toString();
    }

    private int[] intify(Integer[] ar) {
        int[] ret = new int[ar.length];
        System.arraycopy(ar, 0, ret, 0, ar.length);
        return ret;
    }

    @Override
    public String toString() {
        return "victory provided to you by:" + getTrace();
    }

    public boolean isDraw() {
        return (getWinningPlayer() == IPlayer.PLAYER_NONE && getWinningTeam() == IPlayer.TEAM_NONE);
    }
}