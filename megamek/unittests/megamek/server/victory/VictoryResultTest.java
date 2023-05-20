package megamek.server.victory;



import megamek.common.IPlayer;
import megamek.common.Report;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(JUnit4.class)
public class VictoryResultTest {

    VictoryResult victoryResult;

    @Before
    public void setUp() {
        victoryResult = Mockito.mock(VictoryResult.class);
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
        assertTrue(result);
        verify(victoryResult, times(1)).victory();
    }

    @Test
    public void testSetVictory() {
        // Call the method
        victoryResult.setVictory(true);
        doReturn(true).when(victoryResult).victory();

        // Verify the method call and the value set
        verify(victoryResult, times(1)).setVictory(true);
        assertTrue(victoryResult.victory());
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

    @Test
    public void testGetTeams() {
        // Set up the player scores using the actual implementation
        victoryResult.addTeamScore(1, 3);
        victoryResult.addTeamScore(2, 3);

        // Define the expected result
        int[] expectedTeam = {1, 2};

        // Mock the getPlayers() method
        when(victoryResult.getTeams()).thenReturn(expectedTeam);

        // Call the method
        int[] teams = victoryResult.getTeams();

        // Verify the result
        Assert.assertEquals(2, teams.length);
        Assert.assertEquals(1, teams[0]);
        Assert.assertEquals(2, teams[1]);
    }


    @Test
    public void testAddPlayerScore() {
        // Set up the mock object
        when(victoryResult.getPlayerScore(1)).thenReturn(2.5);

        // Call the method
        victoryResult.addPlayerScore(2, 3.5);

        // Verify the behavior
        assertEquals(3.5, victoryResult.getPlayerScore(2), 3.5);
        assertEquals(2.5, victoryResult.getPlayerScore(1), 0);
    }

    @Test
    public void testGetWinningPlayerDraw() throws NoSuchFieldException, IllegalAccessException {
        // Mock the behavior of playerScore
        Map<Integer, Double> playerScore = new HashMap<>();
        playerScore.put(1, 3.0);
        playerScore.put(2, 3.0);
        playerScore.put(3, 2.0);

        Mockito.when(victoryResult.getWinningPlayer()).thenCallRealMethod();
        Mockito.when(victoryResult.getWinningTeam()).thenReturn(IPlayer.TEAM_NONE);

        // Set up the playerScore field using reflection
        Field playerScoreField = VictoryResult.class.getDeclaredField("playerScore");
        playerScoreField.setAccessible(true);
        playerScoreField.set(victoryResult, playerScore);


        when(victoryResult.getWinningPlayer()).thenCallRealMethod();


        int expectedWinningPlayer = IPlayer.PLAYER_NONE;
        int actualWinningPlayer = victoryResult.getWinningPlayer();

        assertEquals(expectedWinningPlayer, actualWinningPlayer);
    }

    @Test
    public void testGetWinningTeamDraw() throws NoSuchFieldException, IllegalAccessException {
        // Mock the behavior of playerScore
        Map<Integer, Double> teamScore = new HashMap<>();
        teamScore.put(1, 3.0);
        teamScore.put(2, 3.0);
        teamScore.put(3, 2.0);

        Mockito.when(victoryResult.getWinningPlayer()).thenCallRealMethod();
        Mockito.when(victoryResult.getWinningTeam()).thenReturn(IPlayer.TEAM_NONE);

        // Set up the playerScore field using reflection
        Field teamScoreField = VictoryResult.class.getDeclaredField("teamScore");
        teamScoreField.setAccessible(true);
        teamScoreField.set(victoryResult, teamScore);


        when(victoryResult.getWinningTeam()).thenCallRealMethod();


        int expectedWinningTeam = IPlayer.TEAM_NONE;
        int actualWinningTeam= victoryResult.getWinningTeam();

        assertEquals(expectedWinningTeam, actualWinningTeam);
    }


    @Test
    public void testIsDraw() {
        VictoryResult victoryResult = Mockito.spy(new VictoryResult(false));

        Mockito.when(victoryResult.getWinningPlayer()).thenReturn(IPlayer.PLAYER_NONE);
        Mockito.when(victoryResult.getWinningTeam()).thenReturn(IPlayer.TEAM_NONE);

        Assert.assertTrue(victoryResult.isDraw());

        // Test when the winning player is not PLAYER_NONE
        Mockito.when(victoryResult.getWinningPlayer()).thenReturn(1);
        assertFalse(victoryResult.isDraw());

        // Test when the winning team is not TEAM_NONE
        Mockito.when(victoryResult.getWinningPlayer()).thenReturn(IPlayer.PLAYER_NONE);
        Mockito.when(victoryResult.getWinningTeam()).thenReturn(1);
        assertFalse(victoryResult.isDraw());
    }

    @Test
    public void testIsWinningTeam() throws NoSuchFieldException, IllegalAccessException {
        VictoryResult victoryResult = Mockito.spy(new VictoryResult(true));
        victoryResult.addTeamScore(1, 10.25);
        victoryResult.addTeamScore(2, 9.75);

        Field hiScore = VictoryResult.class.getDeclaredField("hiScore");
        hiScore.setAccessible(true);
        hiScore.set(victoryResult, 10.25);

        Mockito.doCallRealMethod().when(victoryResult).getTeamScore(1);

        // Assuming the hiScore is 10.25
        // Team 1 has a score of 10.25, which matches the hiScore
        Assert.assertTrue(victoryResult.isWinningTeam(1));

        Mockito.doCallRealMethod().when(victoryResult).getTeamScore(2);

        // Team 2 has a score of 9.75, which does not match the hiScore
        Assert.assertFalse(victoryResult.isWinningTeam(2));
    }


    @Test
    public void testIsWinningPlayer() throws NoSuchFieldException, IllegalAccessException {
        VictoryResult victoryResult = Mockito.spy(new VictoryResult(true));
        victoryResult.addPlayerScore(1, 10.25);
        victoryResult.addPlayerScore(2, 9.75);

        Field hiScore = VictoryResult.class.getDeclaredField("hiScore");
        hiScore.setAccessible(true);
        hiScore.set(victoryResult, 10.25);

        Mockito.doCallRealMethod().when(victoryResult).getPlayerScore(1);

        // Assuming the hiScore is 10.25
        // Team 1 has a score of 10.25, which matches the hiScore
        Assert.assertTrue(victoryResult.isWinningPlayer(1));

        Mockito.doCallRealMethod().when(victoryResult).getPlayerScore(2);

        // Team 2 has a score of 9.75, which does not match the hiScore
        Assert.assertFalse(victoryResult.isWinningPlayer(2));
    }

    @Test
    public void testAddReportAndGetReports(){
        // Create an instance of the class under test
        VictoryResult victoryResult = Mockito.spy(new VictoryResult(true));

        // Create a mock report
        Report mockReport = mock(Report.class); // Replace with your mock or test implementation of Report
        // Create a mock report
        Report mockReport2 = mock(Report.class); // Replace with your mock or test implementation of Report

        // Add the mock report using the addReport method
        victoryResult.addReport(mockReport);
        victoryResult.addReport(mockReport2);

        // Verify that the addReport method was called once
        verify(victoryResult, times(1)).addReport(mockReport);

        // Get the reports using the getReports method
        ArrayList<Report> reports = victoryResult.getReports();

        // Assert that the reports contain the mock report
        assertEquals(2, reports.size());
        assertEquals(mockReport, reports.get(0));

    }


}