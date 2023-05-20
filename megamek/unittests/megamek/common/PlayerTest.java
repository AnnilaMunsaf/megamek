/*
 * MegaMek - Copyright (C) 2000,2001,2002,2003,2004,2005 Ben Mazur
 * (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */
package megamek.common;

import megamek.client.ui.swing.util.PlayerColour;
import megamek.common.icons.Camouflage;
import megamek.common.options.GameOptions;
import megamek.common.options.OptionsConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.Vector;

import static org.junit.Assert.*;


@RunWith(JUnit4.class)
public class PlayerTest {

    private IGame game;
    private Player player;

    private Minefield minefield;

    @Before
    public void setup() {
        game = Mockito.mock(IGame.class);
        player = new Player(1, "John");
        minefield = Mockito.mock(Minefield.class);

    }

    @Test
    public void testAddMinefield() {
        player.setGame(game);
        player.addMinefield(minefield);

        // Assert that the minefield is added to the player's minefields
        assertTrue(player.getMinefields().contains(minefield));
    }


    @Test
    public void testPlayerEloRating() {
        player.setPlayerEloRating(200);
        assertEquals(200, player.getPlayerEloRating());
    }

    @Test
    public void testRemoveMinefield() {
        player.addMinefield(minefield);
        player.removeMinefield(minefield);

        // Assert that the minefield is removed from the player's minefields
        assertFalse(player.getMinefields().contains(minefield));
    }


    @Test
    public void testRemoveArtyAutoHitHexes() {
        player.addArtyAutoHitHex(new Coords(1, 2));
        player.addArtyAutoHitHex(new Coords(3, 4));
        player.removeArtyAutoHitHexes();
        assertTrue(player.getArtyAutoHitHexes().isEmpty());
    }


    @Test
    public void testAddArtyAutoHitHex() {
        player.addArtyAutoHitHex(new Coords(1, 2));
        assertFalse(player.getArtyAutoHitHexes().isEmpty());
        Assert.assertEquals(1, player.getArtyAutoHitHexes().size());
        Assert.assertEquals(new Coords(1, 2), player.getArtyAutoHitHexes().get(0));
    }


    @Test
    public void testIsEnemyOf() {
        Player player1 = new Player(1, "John1"); // id=1, team=1
        Player player2 = new Player(2, "John2"); // id=2, team=2
        Player player3 = new Player(3, "John3"); // id=3, team=1
        Player player4 = new Player(4, "John4"); // id=4, team=3

        assertTrue(player1.isEnemyOf(null)); // Null player, not enemy


        player1.setTeam(4);
        player3.setTeam(4);

        assertFalse(player1.isEnemyOf(player1)); // Same player, not enemy
        assertTrue(player1.isEnemyOf(player2)); // Different team, enemy
        assertFalse(player1.isEnemyOf(player3)); // Same team, not enemy
        assertTrue(player1.isEnemyOf(player4)); // Unassigned team, enemy
    }


    @Test
    public void testEquals() {
        Player player2 = new Player(2, "John2");
        Assert.assertEquals(player, player);
        Assert.assertNotEquals(null, player);
        Player other_player = new Player(player.getId(), "John5");
        Assert.assertEquals(other_player, player);
        Assert.assertNotEquals(player2, player);
    }

    @Test
    public void testSetAdmitsDefeat() {
        assertFalse(player.admitsDefeat());
        player.setAdmitsDefeat(true);
        assertTrue(player.admitsDefeat());
        player.setAdmitsDefeat(false);
        assertFalse(player.admitsDefeat());
    }

    @Test
    public void testAdmitsDefeat() {
        Player player1 = new Player(1, "John1"); // id=1, team=1
        Player player2 = new Player(2, "John2"); // id=2, team=2

        assertFalse(player1.admitsDefeat()); // Initial value should be false
        assertFalse(player2.admitsDefeat()); // Initial value should be false

        player1.setAdmitsDefeat(true);
        assertTrue(player1.admitsDefeat()); // Value should be true
        assertFalse(player2.admitsDefeat()); // Value of other player should still be false
    }

    @Test
    public void testGetTurnInitBonus() {
        // Test case 1: No game or entities vector
        Assert.assertEquals(0, player.getTurnInitBonus());

        // Test case 2: Empty entities vector
        player.setGame(game);
        game.setOptions(new GameOptions());
        Assert.assertEquals(0, player.getTurnInitBonus());

        // Create mock entities
        Entity entity1 = Mockito.mock(Entity.class);
        Entity entity2 = Mockito.mock(Entity.class);
        Entity entity3 = Mockito.mock(Entity.class);

        // Set the mock entity owners
        Mockito.when(entity1.getOwner()).thenReturn(player);
        Mockito.when(entity2.getOwner()).thenReturn(player);
        Mockito.when(entity3.getOwner()).thenReturn(player);

        // Set the mock entity bonuses
        Mockito.when(entity1.getHQIniBonus()).thenReturn(5);
        Mockito.when(entity1.getQuirkIniBonus()).thenReturn(10);

        Mockito.when(entity2.getQuirkIniBonus()).thenReturn(8);

        Mockito.when(entity3.getHQIniBonus()).thenReturn(3);
        Mockito.when(entity3.getQuirkIniBonus()).thenReturn(6);

        // Create a mock entities vector and add the mock entities
        Vector<Entity> entitiesVector = new Vector<>();
        entitiesVector.add(entity1);
        entitiesVector.add(entity2);
        entitiesVector.add(entity3);

        // Set the mock entities vector on the game
        Mockito.when(game.getEntitiesVector()).thenReturn(entitiesVector);

        // Create a mock Options instance and set it on the game
        GameOptions options = Mockito.mock(GameOptions.class);
        Mockito.when(game.getOptions()).thenReturn(options);

        // Set the mock booleanOption value
        Mockito.when(options.booleanOption(OptionsConstants.ADVANCED_TACOPS_MOBILE_HQS)).thenReturn(true);


        int expectedBonus = 15; // The highest quirkIniBonus among the owned entities

        Assert.assertEquals(expectedBonus, player.getTurnInitBonus());
    }

    @Test
    public void testGetAirborneVTOL() {
        player.setGame(game);

        // Create mock entities
        VTOL vtol1 = Mockito.mock(VTOL.class);
        VTOL vtol2 = Mockito.mock(VTOL.class);
        VTOL vtol3 = Mockito.mock(VTOL.class);
        Entity entity1 = Mockito.mock(Entity.class);
        Entity entity2 = Mockito.mock(Entity.class);
        Entity entity3 = Mockito.mock(Entity.class);

        // Set the mock entity owners
        Mockito.when(vtol1.getOwner()).thenReturn(player);
        Mockito.when(vtol2.getOwner()).thenReturn(player);
        Mockito.when(vtol3.getOwner()).thenReturn(player);
        Mockito.when(entity1.getOwner()).thenReturn(player);
        Mockito.when(entity2.getOwner()).thenReturn(player);
        Mockito.when(entity3.getOwner()).thenReturn(player);

        // Set the mock entity properties
        Mockito.when(vtol1.getMovementMode()).thenReturn(EntityMovementMode.WIGE);
        Mockito.when(vtol1.isDestroyed()).thenReturn(false);
        Mockito.when(vtol1.getElevation()).thenReturn(2);
        Mockito.when(vtol1.getId()).thenReturn(1);

        Mockito.when(vtol2.getMovementMode()).thenReturn(EntityMovementMode.NAVAL);
        Mockito.when(vtol2.isDestroyed()).thenReturn(false);
        Mockito.when(vtol2.getElevation()).thenReturn(0);
        Mockito.when(vtol2.getId()).thenReturn(2);

        Mockito.when(vtol3.getMovementMode()).thenReturn(EntityMovementMode.WIGE);
        Mockito.when(vtol3.isDestroyed()).thenReturn(true);
        Mockito.when(vtol3.getElevation()).thenReturn(3);
        Mockito.when(vtol3.getId()).thenReturn(3);

        Mockito.when(entity1.getMovementMode()).thenReturn(EntityMovementMode.AIRSHIP);
        Mockito.when(entity1.isDestroyed()).thenReturn(false);
        Mockito.when(entity1.getElevation()).thenReturn(1);
        Mockito.when(entity1.getId()).thenReturn(4);

        Mockito.when(entity2.getMovementMode()).thenReturn(EntityMovementMode.WIGE);
        Mockito.when(entity2.isDestroyed()).thenReturn(false);
        Mockito.when(entity2.getElevation()).thenReturn(2);
        Mockito.when(entity2.getId()).thenReturn(5);

        Mockito.when(entity3.getMovementMode()).thenReturn(EntityMovementMode.AEROSPACE);
        Mockito.when(entity3.isDestroyed()).thenReturn(false);
        Mockito.when(entity3.getElevation()).thenReturn(0);
        Mockito.when(entity3.getId()).thenReturn(6);

        // Create a mock entities vector and add the mock entities
        Vector<Entity> entitiesVector = new Vector<>();
        entitiesVector.add(vtol1);
        entitiesVector.add(vtol2);
        entitiesVector.add(vtol3);
        entitiesVector.add(entity1);
        entitiesVector.add(entity2);
        entitiesVector.add(entity3);

        // Set the mock entities vector on the game
        Mockito.when(game.getEntitiesVector()).thenReturn(entitiesVector);

        Vector<Integer> expectedUnits = new Vector<>();
        expectedUnits.add(1); // Only vtol1 meets the conditions
        expectedUnits.add(5); // Only vtol1 meets the conditions

        Assert.assertEquals(expectedUnits, player.getAirborneVTOL());
    }


    @Test
    public void testToString() {
        int id = 1;
        String name = "John Doe";
        Player player = new Player(id, name);

        String expectedString = "Player " + id + " (" + name + ")";
        String actualString = player.toString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testSetAndGetInitCompensationBonus() {
        int bonus = 5;

        player.setInitCompensationBonus(bonus);
        int actualBonus = player.getInitCompensationBonus();

        Assert.assertEquals(bonus, actualBonus);
    }

    @Test
    public void testSetAndGetConstantInitBonus() {
        int bonus = 3;

        player.setConstantInitBonus(bonus);
        int actualBonus = player.getConstantInitBonus();

        Assert.assertEquals(bonus, actualBonus);
    }

    @Test
    public void testGetBV() {
        // Create a mock game instance
        player.setGame(game);

        // Create a mock entity
        Entity entity1 = Mockito.mock(Entity.class);
        Mockito.when(entity1.getOwner()).thenReturn(player);
        Mockito.when(entity1.isDestroyed()).thenReturn(false);
        Mockito.when(entity1.isTrapped()).thenReturn(false);
        Mockito.when(entity1.calculateBattleValue()).thenReturn(100);

        Entity entity2 = Mockito.mock(Entity.class);
        Mockito.when(entity2.getOwner()).thenReturn(player);
        Mockito.when(entity2.isDestroyed()).thenReturn(false);
        Mockito.when(entity2.isTrapped()).thenReturn(false);
        Mockito.when(entity2.calculateBattleValue()).thenReturn(150);

        Vector<Entity> entities = new Vector<>();
        entities.add(entity1);
        entities.add(entity2);
        // Mock the behavior of game.getEntitiesVector()
        Mockito.when(game.getEntitiesVector()).thenReturn(entities);


        int actualBV = player.getBV();
        int expectedBV = 250;

        Assert.assertEquals(expectedBV, actualBV);
    }

    @Test
    public void testGetFledBV() {
        player.setGame(game);

        // Create a mock entity
        Entity entity1 = Mockito.mock(Entity.class);
        Mockito.when(entity1.getOwner()).thenReturn(player);
        Mockito.when(entity1.calculateBattleValue()).thenReturn(200);

        Entity entity2 = Mockito.mock(Entity.class);
        Mockito.when(entity2.getOwner()).thenReturn(player);
        Mockito.when(entity2.calculateBattleValue()).thenReturn(300);

        Vector<Entity> retreatedEntities = new Vector<>();
        retreatedEntities.add(entity1);
        retreatedEntities.add(entity2);

        // Mock the behavior of game.getRetreatedEntities()
        Mockito.when(game.getRetreatedEntities()).thenReturn(retreatedEntities.elements());


        int actualFledBV = player.getFledBV();
        int expectedFledBV = 500;

        Assert.assertEquals(expectedFledBV, actualFledBV);
    }

    @Test
    public void testSetAndGetInitialBV() {
        player.setGame(game);

        player.setInitialBV();

        int actualInitialBV = player.getInitialBV();
        int expectedInitialBV = 0; // Assuming the initial BV is set to 0

        Assert.assertEquals(expectedInitialBV, actualInitialBV);
    }

    @Test
    public void testIncreaseInitialBV() {
        player.setGame(game);

        player.increaseInitialBV(200);

        int actualInitialBV = player.getInitialBV();
        int expectedInitialBV = 200;

        Assert.assertEquals(expectedInitialBV, actualInitialBV);
    }

    @Test
    public void testAllowTeamChange() {

        // Test initial state
        assertFalse(player.isAllowingTeamChange());

        // Test setting and getting team change permission
        player.setAllowTeamChange(true);
        assertTrue(player.isAllowingTeamChange());

        player.setAllowTeamChange(false);
        assertFalse(player.isAllowingTeamChange());
    }

    @Test
    public void testPlayerCamoAndColour(){
        Assert.assertEquals(new Camouflage(Camouflage.COLOUR_CAMOUFLAGE, PlayerColour.BLUE.name()), player.getCamouflage());
        player.setColour(PlayerColour.CYAN);
        Assert.assertEquals(PlayerColour.CYAN, player.getColour());
        player.setCamoCategory(Camouflage.NO_CAMOUFLAGE);
        player.setCamoFileName(PlayerColour.BROWN.name());
        Assert.assertEquals(new Camouflage(Camouflage.NO_CAMOUFLAGE, PlayerColour.BROWN.name()), player.getCamouflage());
        player.setColour(PlayerColour.BLUE);
        Assert.assertEquals(PlayerColour.BLUE, player.getColour());
    }


}

