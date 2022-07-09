package me.g1tommy.samplesecuritysession.service;

import lombok.RequiredArgsConstructor;
import me.g1tommy.samplesecuritysession.domain.Item;
import me.g1tommy.samplesecuritysession.repository.ItemRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item save(String name) {
        return itemRepository.save(Item.builder()
                                        .name(name)
                                        .build());
    }
}
