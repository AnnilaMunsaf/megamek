package megamek.server;

import megamek.common.Game;
import megamek.common.IGame;
import megamek.common.IPlayer;
import megamek.common.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class ServerTest {

    IGame game;
    Player player1;
    Player player2;


    @Before
    public void setUp() {
        game = Mockito.mock(Game.class);
        player1 = new Player(1, "Player1");
        player2 = new Player(2, "Player2");
        game.addPlayer(1, player1);
        game.addPlayer(2, player2);


    }

    @Test
    public void testForceVictoryForTeam() throws IOException {

        // Create a mock player
        IPlayer victor = Mockito.mock(IPlayer.class);
        Mockito.doNothing().when(victor).setAdmitsDefeat(false);

        // Create a mock player vector
        Vector<IPlayer> playersVector = new Vector<>();
        for (int i = 0; i < 2; i++) {
            IPlayer player = Mockito.mock(IPlayer.class);
            playersVector.add(player);
        }
        when(game.getPlayersVector()).thenReturn(playersVector);

        Mockito.when(game.getEntities()).thenReturn(Mockito.mock(Iterator.class));
        Mockito.when(game.getPlayers()).thenReturn(Mockito.mock(Enumeration.class));
        Mockito.when(game.getAttacks()).thenReturn(Mockito.mock(Enumeration.class));

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
    }

    @Test
    public void testForceVictoryForPlayer() throws IOException {

        // Create a mock player
        IPlayer victor = Mockito.mock(IPlayer.class);
        when(victor.getId()).thenReturn(2);
        Mockito.doNothing().when(victor).setAdmitsDefeat(false);

        // Create a mock player vector
        Vector<IPlayer> playersVector = new Vector<>();
        for (int i = 0; i < 2; i++) {
            IPlayer player = Mockito.mock(IPlayer.class);
            playersVector.add(player);
        }
        when(game.getPlayersVector()).thenReturn(playersVector);

        Mockito.when(game.getEntities()).thenReturn(Mockito.mock(Iterator.class));
        Mockito.when(game.getPlayers()).thenReturn(Mockito.mock(Enumeration.class));
        Mockito.when(game.getAttacks()).thenReturn(Mockito.mock(Enumeration.class));

        Server server = new Server("Server 1", 5);
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
    }




}
