package org.skypro.counter.service;

import org.springframework.stereotype.Service;

@Service
public class CounterService {
    private int count;

    public void countdown() {
        this.count++;
    }

    public int getCount() {
        return this.count;
    }
}