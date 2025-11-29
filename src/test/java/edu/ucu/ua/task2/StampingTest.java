package edu.ucu.ua.task2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StampingTest {
    private List<Integer> results;

    @BeforeEach
    void setUp() {
        results = new ArrayList<>();
    }

    @Test
    void testSignatureCreation() {
        Signature<Integer> signature = new Signature<>(x -> results.add(x * 2));
        assertNotNull(signature);
        assertNotNull(signature.consumer);
    }

    @Test
    void testSignatureApply() {
        Signature<Integer> signature = new Signature<>(results::add);
        signature.apply(42);
        
        assertEquals(1, results.size());
        assertEquals(42, results.get(0));
        assertNotNull(signature.getId()); // Check that freeze was called
    }

    @Test
    void testGroupCreation() {
        Group<Integer> group = new Group<>();
        assertNotNull(group);
        assertNull(group.getTasks()); // Initially null
    }

    @Test
    void testGroupAddTask() {
        Group<Integer> group = new Group<>();
        Signature<Integer> sig1 = new Signature<>(results::add);
        Signature<Integer> sig2 = new Signature<>(x -> results.add(x * 2));
        
        group.addTask(sig1).addTask(sig2);
        
        assertNotNull(group.getTasks());
        assertEquals(2, group.getTasks().size());
    }

    @Test
    void testGroupApply() {
        Group<Integer> group = new Group<>();
        group.addTask(new Signature<>(results::add));
        group.addTask(new Signature<>(x -> results.add(x * 2)));
        group.addTask(new Signature<>(x -> results.add(x * 3)));
        
        group.apply(10);
        
        assertEquals(3, results.size());
        assertEquals(10, results.get(0));
        assertEquals(20, results.get(1));
        assertEquals(30, results.get(2));
    }

    @Test
    void testGroupAutoStamping() {
        Group<Integer> group = new Group<>();
        Signature<Integer> sig1 = new Signature<>(results::add);
        Signature<Integer> sig2 = new Signature<>(results::add);
        
        group.addTask(sig1).addTask(sig2);
        group.apply(5);
        
        // Check that groupUuid was generated
        assertNotNull(group.groupUuid);
        assertNotNull(group.getId());
        
        // Check that all signatures got the group_uuid stamp
        assertNotNull(sig1.getHeader("group_uuid"));
        assertNotNull(sig2.getHeader("group_uuid"));
        assertEquals(group.groupUuid, sig1.getHeader("group_uuid"));
        assertEquals(group.groupUuid, sig2.getHeader("group_uuid"));
    }

    @Test
    void testStampingVisitor() {
        Signature<Integer> signature = new Signature<>(results::add);
        StampingVisitor<Integer> visitor = new StampingVisitor<>("test_key", "test_value");
        
        signature.accept(visitor);
        
        assertEquals("test_value", signature.getHeader("test_key"));
    }

    @Test
    void testStampingVisitorOnGroup() {
        Group<Integer> group = new Group<>();
        Signature<Integer> sig1 = new Signature<>(results::add);
        Signature<Integer> sig2 = new Signature<>(results::add);
        
        group.addTask(sig1).addTask(sig2);
        
        StampingVisitor<Integer> visitor = new StampingVisitor<>("custom_key", "custom_value");
        group.accept(visitor);
        
        // Check that group and all children got stamped
        assertEquals("custom_value", group.getHeader("custom_key"));
        assertEquals("custom_value", sig1.getHeader("custom_key"));
        assertEquals("custom_value", sig2.getHeader("custom_key"));
    }

    @Test
    void testNestedGroups() {
        Group<Integer> nestedGroup = new Group<>();
        nestedGroup.addTask(new Signature<>(results::add));
        nestedGroup.addTask(new Signature<>(x -> results.add(x * 2)));
        
        Group<Integer> mainGroup = new Group<>();
        mainGroup.addTask(nestedGroup);
        mainGroup.addTask(new Signature<>(x -> results.add(x * 3)));
        
        mainGroup.apply(10);
        
        assertEquals(3, results.size());
        assertEquals(10, results.get(0));
        assertEquals(20, results.get(1));
        assertEquals(30, results.get(2));
    }
}
