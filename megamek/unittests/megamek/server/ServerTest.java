package megamek.server;

import megamek.common.*;
import megamek.server.victory.Victory;
import megamek.server.victory.VictoryResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ServerTest {

    IGame game;
    Player player2;

    private VictoryResult victoryResultMock;



    @Before
    public void setUp() {
        game = mock(Game.class);
        player2 = new Player(2, "Player2");
        victoryResultMock = Mockito.mock(VictoryResult.class);
    }

    @Test
    public void testForceVictoryForTeam() throws IOException {

        // Create a mock player
        IPlayer victor = mock(IPlayer.class);
        Mockito.doNothing().when(victor).setAdmitsDefeat(false);

        // Create a mock player vector
        Vector<IPlayer> playersVector = new Vector<>();
        for (int i = 0; i < 2; i++) {
            IPlayer player = mock(IPlayer.class);
            playersVector.add(player);
        }
        when(game.getPlayersVector()).thenReturn(playersVector);

        Mockito.when(game.getEntities()).thenReturn(mock(Iterator.class));
        Mockito.when(game.getPlayers()).thenReturn(mock(Enumeration.class));
        Mockito.when(game.getAttacks()).thenReturn(mock(Enumeration.class));

        Mockito.when(victor.getTeam()).thenReturn(2);
        Mockito.when(victor.getId()).thenReturn(1);

        Server server = new Server("Server 1", 5);
        server.setGame(game);

        server.forceVictory(victor);
        // Verify the expected behavior and state changes

        // Verify that game.setForceVictory(true) was called
        verify(game).setForceVictory(true);


        // Verify
        verify(game).setVictoryTeam(2);
        verify(game).getPlayersVector();

        for (IPlayer player : playersVector) {
            verify(player).setAdmitsDefeat(false);
        }

        server.die();

    }

    @Test
    public void testForceVictoryForPlayer() throws IOException {

        // Create a mock player
        IPlayer victor = mock(IPlayer.class);
        when(victor.getId()).thenReturn(2);

        // Create a mock player vector
        Vector<IPlayer> playersVector = new Vector<>();
        for (int i = 0; i < 2; i++) {
            IPlayer player = mock(IPlayer.class);
            playersVector.add(player);
        }
        when(game.getPlayersVector()).thenReturn(playersVector);

        Mockito.when(game.getEntities()).thenReturn(mock(Iterator.class));
        Mockito.when(game.getPlayers()).thenReturn(mock(Enumeration.class));
        Mockito.when(game.getAttacks()).thenReturn(mock(Enumeration.class));

        Server server = new Server("Server 2", 5);
        server.setGame(game);

        server.forceVictory(victor);
        // Verify the expected behavior and state changes

        // Verify that game.setForceVictory(true) was called
        verify(game).setForceVictory(true);


        // Verify
        verify(game).setVictoryPlayerId(2);
        Mockito.verify(game).setVictoryTeam(IPlayer.TEAM_NONE);
        verify(game).getPlayersVector();

        for (IPlayer player : playersVector) {
            verify(player).setAdmitsDefeat(false);
        }

        server.die();

    }

    @Test
    public void testForceVictory() throws IOException {

        // Create the server and set the game
        Server server = new Server("Server 3", 5);
        Game game = new Game();

        server.setGame(game);

        // Invoke the method being tested
        server.forceVictory(player2);

        // Assert the expected outcome
        assertEquals(2, server.getGame().getVictoryPlayerId());

        server.die();

    }


    @Test
    public void testVictory() throws IOException {

        Victory victoryMock = Mockito.mock(Victory.class);
        HashMap victoryContextMock = mock(HashMap.class);

        // Create a mock player vector
        Vector<IPlayer> playersVector = new Vector<>();
        for (int i = 0; i < 2; i++) {
            IPlayer player = mock(IPlayer.class);
            playersVector.add(player);
        }

        Report report1 = new Report();
        Report report2 = new Report();
        ArrayList<Report> reports = new ArrayList<>();
        reports.add(report1);
        reports.add(report2);

        when(game.getVictory()).thenReturn(victoryMock);
        when(game.getVictoryContext()).thenReturn(victoryContextMock);
        when(victoryMock.checkForVictory(game, game.getVictoryContext())).thenReturn(victoryResultMock);

        when(game.getPlayersVector()).thenReturn(playersVector);
        Mockito.when(game.getEntities()).thenReturn(mock(Iterator.class));
        Mockito.when(game.getPlayers()).thenReturn(mock(Enumeration.class));
        Mockito.when(game.getAttacks()).thenReturn(mock(Enumeration.class));

        Server server = new Server("Server 6", 5);
        server.setGame(game);
        game.setVictoryContext(victoryContextMock);


        // Set up mock VictoryResult
        when(victoryResultMock.getReports()).thenReturn(reports);
        when(victoryResultMock.victory()).thenReturn(true);
        when(victoryResultMock.isDraw()).thenReturn(true);
        when(victoryResultMock.getWinningPlayer()).thenReturn(IPlayer.PLAYER_NONE);
        when(victoryResultMock.getWinningTeam()).thenReturn(IPlayer.TEAM_NONE);



        // Call the victory() method
        boolean result = server.victory();


        // Verify that the expected methods were called
        verify(game).getVictory();
        verify(victoryMock).checkForVictory(game, game.getVictoryContext());
        verify(victoryResultMock).updateEloRatings(playersVector);
        verify(victoryResultMock, Mockito.times(2)).victory();
        verify(victoryResultMock).isDraw();
        verify(victoryResultMock).getWinningPlayer();
        verify(victoryResultMock).getWinningTeam();

        // Assert the result
        Assert.assertTrue(result);
        server.die();
    }



}
