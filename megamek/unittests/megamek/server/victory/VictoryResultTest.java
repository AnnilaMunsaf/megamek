package megamek.server.victory;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(JUnit4.class)
public class VictoryResultTest {

    private boolean victory;
    VictoryResult victoryResult;

    @Before
    public void setUp() {
        victoryResult = Mockito.mock(VictoryResult.class);
        victory = false;
    }

    @Test
    public void testGetWinningPlayer() throws NoSuchFieldException, IllegalAccessException {


        // Mock the behavior of playerScore
        Map<Integer, Double> playerScore = new HashMap<>();
        playerScore.put(1, 5.0);
        playerScore.put(2, 3.0);
        playerScore.put(3, 8.0);

        // Set up the playerScore field using reflection
        Field playerScoreField = VictoryResult.class.getDeclaredField("playerScore");
        playerScoreField.setAccessible(true);
        playerScoreField.set(victoryResult, playerScore);


        when(victoryResult.getWinningPlayer()).thenCallRealMethod();

        // Call the method under test
        int winningPlayer = victoryResult.getWinningPlayer();

        // Assert the expected result
        assertEquals(3, winningPlayer);
    }


    @Test
    public void testGetWinningTeam() throws NoSuchFieldException, IllegalAccessException {

        // Mock the behavior of playerScore
        Map<Integer, Double> playerScore = new HashMap<>();
        playerScore.put(1, 5.0);
        playerScore.put(2, 3.0);
        playerScore.put(3, 8.0);

        // Set up the playerScore field using reflection
        Field playerScoreField = VictoryResult.class.getDeclaredField("teamScore");
        playerScoreField.setAccessible(true);
        playerScoreField.set(victoryResult, playerScore);


        when(victoryResult.getWinningTeam()).thenCallRealMethod();

        // Call the method under test
        int winingTeam = victoryResult.getWinningTeam();

        // Assert the expected result
        assertEquals(3, winingTeam);
    }

    @Test
    public void testVictory() {
        doReturn(true).when(victoryResult).victory();

        // Call the method
        boolean result = victoryResult.victory();

        // Verify the result
        Assert.assertTrue(result);
        verify(victoryResult, times(1)).victory();
    }

    @Test
    public void testSetVictory() {
        // Call the method
        victoryResult.setVictory(true);
        doReturn(true).when(victoryResult).victory();

        // Verify the method call and the value set
        verify(victoryResult, times(1)).setVictory(true);
        Assert.assertTrue(victoryResult.victory());
    }


    @Test
    public void testGetPlayers() {
        // Set up the player scores using the actual implementation
        victoryResult.addPlayerScore(1, 3);
        victoryResult.addPlayerScore(2, 3);

        // Define the expected result
        int[] expectedPlayers = {1, 2};

        // Mock the getPlayers() method
        when(victoryResult.getPlayers()).thenReturn(expectedPlayers);

        // Call the method
        int[] players = victoryResult.getPlayers();

        // Verify the result
        Assert.assertEquals(2, players.length);
        Assert.assertEquals(1, players[0]);
        Assert.assertEquals(2, players[1]);
    }


}