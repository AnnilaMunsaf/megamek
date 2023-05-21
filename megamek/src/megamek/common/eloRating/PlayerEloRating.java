package megamek.common.eloRating;

import megamek.common.IPlayer;
import megamek.server.victory.VictoryResult;

import java.util.Enumeration;
import java.util.Vector;

public class PlayerEloRating implements IPlayerEloRating {
    private static final int K_FACTOR = 32; // K-factor determines the magnitude of rating changes

    public void updateRatings(VictoryResult victoryResult, Vector<IPlayer> players) {
        int winningPlayerId = victoryResult.getWinningPlayer();
        int winningTeamId = victoryResult.getWinningTeam();

        IPlayer winningPlayer = findPlayerById(winningPlayerId, players);
        IPlayer losingPlayer = findLosingPlayer(winningPlayerId, winningTeamId, players);

        if (winningPlayer != null && losingPlayer != null) {
            int winnerRating = winningPlayer.getPlayerEloRating();
            int loserRating = losingPlayer.getPlayerEloRating();

            double expectedOutcomeWinner = calculateExpectedOutcome(winnerRating, loserRating);
            double expectedOutcomeLoser = calculateExpectedOutcome(loserRating, winnerRating);

            int updatedRatingWinner = calculateUpdatedRating(winnerRating, 1, expectedOutcomeWinner);
            int updatedRatingLoser = calculateUpdatedRating(loserRating, 0, expectedOutcomeLoser);

            winningPlayer.setPlayerEloRating(updatedRatingWinner);
            losingPlayer.setPlayerEloRating(updatedRatingLoser);
        }
    }

    private static IPlayer findPlayerById(int playerId, Vector<IPlayer> players) {
        for (IPlayer player : players) {
            if (player.getId() == playerId) {
                return player;
            }
        }
        return null;
    }

    private static IPlayer findLosingPlayer(int winningPlayerId, int winningTeamId, Vector<IPlayer> players) {
        for (IPlayer player : players) {
            if (player.getId() != winningPlayerId && player.getTeam() != winningTeamId) {
                return player;
            }
        }
        return null;
    }

    private static double calculateExpectedOutcome(int ratingA, int ratingB) {
        return 1.0 / (1 + Math.pow(10, (ratingB - ratingA) / 400.0));
    }

    private static int calculateUpdatedRating(int currentRating, double score, double expectedOutcome) {
        return (int) Math.round(currentRating + determineK(currentRating) * (score - expectedOutcome));
    }

    /**
     * Determine the rating constant K-factor based on current rating
     *
     * @param rating
     *            Player rating
     * @return K-factor
     */
    private static int determineK(int rating) {
        int K;
        if (rating < 2000) {
            K = K_FACTOR;
        } else if (rating < 2400) {
            K = 24;
        } else {
            K = 16;
        }
        return K;
    }
}
