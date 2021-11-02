package ca.cmpt276.parentapp.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ChildManagerTest {

    @Test
    void testNumOfChildren(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        ChildManager childManager = ChildManager.getInstance();
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        assertEquals(3, childManager.numOfChildren());
        childManager.cleanSingleton();
    }

    @Test
    void testAddChild(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        ChildManager childManager = ChildManager.getInstance();
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        assertEquals("Child-A", childManager.getChild(0).getName());
        assertEquals("Child-B", childManager.getChild(1).getName());
        assertEquals("Child-C", childManager.getChild(2).getName());
        childManager.cleanSingleton();
    }

    @Test
    void testRemoveChild(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        ChildManager childManager = ChildManager.getInstance();
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        assertEquals("Child-A", childManager.getChildName(0));
        assertEquals("Child-B", childManager.getChildName(1));
        assertEquals("Child-C", childManager.getChildName(2));
        childManager.removeChild(c2);
        assertEquals("Child-A", childManager.getChildName(0));
        assertEquals("Child-C", childManager.getChildName(1));
        childManager.removeChildAtIndex(0);
        assertEquals("Child-C", childManager.getChildName(0));
        childManager.removeChildAtIndex(0);
        assertEquals(0, childManager.numOfChildren());
        childManager.cleanSingleton();
    }

    @Test
    void testEditChild(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        ChildManager childManager = ChildManager.getInstance();
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        assertEquals("Child-A", childManager.getChildName(0));
        assertEquals("Child-B", childManager.getChildName(1));
        assertEquals("Child-C", childManager.getChildName(2));
        childManager.editChild(0, "Child-1");
        childManager.editChild(1, "Child-2");
        childManager.editChild(2, "Child-3");
        assertEquals("Child-1", childManager.getChildName(0));
        assertEquals("Child-2", childManager.getChildName(1));
        assertEquals("Child-3", childManager.getChildName(2));
        childManager.cleanSingleton();
    }

    @Test
    void testGetChild(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        ChildManager childManager = ChildManager.getInstance();
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        assertEquals(c1, childManager.getChild(0));
        assertEquals(c2, childManager.getChild(1));
        assertEquals(c3, childManager.getChild(2));
        childManager.cleanSingleton();
    }

    @Test
    void testGetChildName(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        ChildManager childManager = ChildManager.getInstance();
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        assertEquals("Child-A", childManager.getChildName(0));
        assertEquals("Child-B", childManager.getChildName(1));
        assertEquals("Child-C", childManager.getChildName(2));
        childManager.cleanSingleton();
    }

    @Test
    void testGetPickingChild(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        Child c4 = new Child("Child-D");
        Child c5 = new Child("Child-E");
        ChildManager childManager = ChildManager.getInstance();
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        childManager.addChild(c4);
        childManager.addChild(c5);
        assertEquals(c1, childManager.getPickingChild());
        assertEquals(c2, childManager.getPickingChild());
        assertEquals(c3, childManager.getPickingChild());
        assertEquals(c4, childManager.getPickingChild());
        assertEquals(c5, childManager.getPickingChild());
        assertEquals(c1, childManager.getPickingChild());
        assertEquals(c2, childManager.getPickingChild());
        assertEquals(c3, childManager.getPickingChild());
        assertEquals(c4, childManager.getPickingChild());
        assertEquals(c5, childManager.getPickingChild());
        assertEquals(c1, childManager.getPickingChild());
        assertEquals(1, childManager.getPickingChildIndex());
        assertEquals(c2, childManager.getPickingChild());
        assertEquals(2, childManager.getPickingChildIndex());
        childManager.removeChildAtIndex(3);
        assertEquals(c3, childManager.getChild(childManager.getPickingChildIndex()));
        childManager.removeChildAtIndex(1);
        assertEquals(1, childManager.getPickingChildIndex());
        assertEquals(c3, childManager.getChild(childManager.getPickingChildIndex()));
        childManager.removeChildAtIndex(1);
        assertEquals(1, childManager.getPickingChildIndex());
        assertEquals(c5, childManager.getChild(childManager.getPickingChildIndex()));
        childManager.cleanSingleton();
    }

    @Test
    void testNoChildren(){
        Child c1 = new Child("Child-A");
        Child c2 = new Child("Child-B");
        Child c3 = new Child("Child-C");
        ChildManager childManager = ChildManager.getInstance();
        assertTrue(childManager.noChildren());
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        assertFalse(childManager.noChildren());
        childManager.cleanSingleton();
    }

}