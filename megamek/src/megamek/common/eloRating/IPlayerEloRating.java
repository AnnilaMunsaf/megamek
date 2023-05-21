package megamek.common.eloRating;

import megamek.common.IPlayer;
import megamek.server.victory.VictoryResult;

import java.util.Vector;

public interface IPlayerEloRating {
    void updateRatings(VictoryResult victoryResult, Vector<IPlayer> players);
}