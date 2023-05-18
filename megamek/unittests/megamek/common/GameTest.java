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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.util.Hashtable;

@RunWith(JUnit4.class)
public class GameTest {

    private Game game;

    private Hashtable<Integer, Entity> entityIds;

    @Before
    public void setup() {
        game = new Game();
        entityIds = new Hashtable<>();

    }

    @Test
    public void testAddEntity_Mech() {
        // Mock Mech entity
        Mech mechEntity = Mockito.mock(Mech.class);
        mechEntity.setId(1); // Set initial ID

        // Set up necessary method calls or behaviors for Mech entity
        Mockito.when(mechEntity.hasBattleArmorHandles()).thenReturn(false);
        Mockito.when(mechEntity.isOmni()).thenReturn(false);
        Mockito.when(mechEntity.getId()).thenReturn(1);
        Mockito.when(mechEntity.calculateBattleValue(false, false)).thenReturn(100);
        Mockito.doNothing().when(mechEntity).addTransporter(Mockito.any());

        // Call the method
        game.addEntity(mechEntity, true);

        // Verify the expected method calls
        Mockito.verify(mechEntity).setBAGrabBars();
        Mockito.verify(mechEntity).setProtomechClampMounts();
        Mockito.verify(mechEntity).addTransporter(Mockito.any());
        Mockito.verify(mechEntity).setGameOptions();
        Mockito.verify(mechEntity).getC3UUIDAsString();
        Mockito.verify(mechEntity).setC3UUID();
        Mockito.verify(mechEntity).getId();
        Mockito.verify(mechEntity).setId(Mockito.anyInt());
        Mockito.verify(mechEntity).calculateBattleValue(false, false);


        int id = mechEntity.getId();

        // Verify that the entity is not already in the hashtable
        Assert.assertFalse(entityIds.containsKey(id));

        // Add the entity to the hashtable
        entityIds.put(id, mechEntity);

        // Verify that the entity is added to the hashtable
        Assert.assertTrue(entityIds.containsKey(id));
        Assert.assertEquals(mechEntity, entityIds.get(id));

    }

    @Test
    public void testAddEntity_Tank() {
        // Mock Tank entity
        Tank tankEntity = Mockito.mock(Tank.class);
        tankEntity.setId(1); // Set initial ID

        // Set up necessary method calls or behaviors for Tank entity
        Mockito.when(tankEntity.hasBattleArmorHandles()).thenReturn(false);
        Mockito.when(tankEntity.isOmni()).thenReturn(false);

        // Mock the required transporters
        Mockito.doNothing().when(tankEntity).addTransporter(Mockito.any());

        // Call the method
        game.addEntity(tankEntity, true);

        // Verify the expected method calls
        Mockito.verify(tankEntity).setBAGrabBars();
        Mockito.verify(tankEntity).setTrailerHitches();
        Mockito.verify(tankEntity).addTransporter(Mockito.any());
        Mockito.verify(tankEntity).setGameOptions();
        Mockito.verify(tankEntity).getC3UUIDAsString();
        Mockito.verify(tankEntity).setC3UUID();
        Mockito.verify(tankEntity).getId();
        Mockito.verify(tankEntity).setId(Mockito.anyInt());
        Mockito.verify(tankEntity).calculateBattleValue(false, false);

        int id = tankEntity.getId();

        // Verify that the entity is not already in the hashtable
        Assert.assertFalse(entityIds.containsKey(id));

        // Add the entity to the hashtable
        entityIds.put(id, tankEntity);

        // Verify that the entity is added to the hashtable
        Assert.assertTrue(entityIds.containsKey(id));
        Assert.assertEquals(tankEntity, entityIds.get(id));
    }
}

