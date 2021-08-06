package com.codecool.stampcollection.service;

import com.codecool.stampcollection.exception.ItemNotFoundException;
import com.codecool.stampcollection.model.Item;
import com.codecool.stampcollection.repository.ItemReposiroty;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemReposiroty reposiroty;

    public ItemService(ItemReposiroty reposiroty) {
        this.reposiroty = reposiroty;
    }

    public Item findById(Long id) {
        return reposiroty.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
    }

    public List<Item> findAll() {
        return reposiroty.findAll();
    }

    public Item addNew(Item item) {
        return reposiroty.save(item);
    }

    public void deleteById(Long id) {
        if (reposiroty.countAllByIdIsGreaterThan(id) != 0) {
            throw new UnsupportedOperationException("Only the last item can be deleted");
        }
        reposiroty.deleteById(id);
    }
}
