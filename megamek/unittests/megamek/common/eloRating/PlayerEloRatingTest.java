package megamek.common.eloRating;

import megamek.common.IPlayer;
import megamek.server.victory.VictoryResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Vector;

import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class PlayerEloRatingTest {

    @Test
    public void testUpdateRatings() {

        // Create mock objects
        IPlayer winningPlayer = mock(IPlayer.class);
        IPlayer losingPlayer = mock(IPlayer.class);
        Vector<IPlayer> players = new Vector<>();
        players.add(winningPlayer);
        players.add(losingPlayer);

        // Set up mock behaviors
        when(winningPlayer.getId()).thenReturn(1);
        when(winningPlayer.getPlayerEloRating()).thenReturn(2000);
        when(losingPlayer.getId()).thenReturn(2);
        when(losingPlayer.getPlayerEloRating()).thenReturn(1800);

        // Create a mock VictoryResult
        VictoryResult victoryResult = mock(VictoryResult.class);
        when(victoryResult.getWinningPlayer()).thenReturn(1);
        when(victoryResult.getWinningTeam()).thenReturn(1);

        // Call the method to be tested
        PlayerEloRating.updateRatings(victoryResult, players);

        // Verify that the player ratings are updated correctly
        verify(winningPlayer).setPlayerEloRating(anyInt());
        verify(losingPlayer).setPlayerEloRating(anyInt());

        // Verify the expected updated ratings
        int expectedUpdatedRatingWinner = 2006; // Calculate the expected updated rating based on the formula
        int expectedUpdatedRatingLoser = 1792; // Calculate the expected updated rating based on the formula
        verify(winningPlayer).setPlayerEloRating(expectedUpdatedRatingWinner);
        verify(losingPlayer).setPlayerEloRating(expectedUpdatedRatingLoser);

    }
}

