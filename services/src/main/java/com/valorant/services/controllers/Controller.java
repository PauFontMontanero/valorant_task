package com.valorant.services.controllers;

public interface Controller<K, V> {
    String get(K key);  // Get a single resource by key
    String get();       // Get all resources
    void post(V value); // Create a new resource
    void put(K key, V value); // Update an existing resource
    void delete(K key); // Delete a resource by key
}