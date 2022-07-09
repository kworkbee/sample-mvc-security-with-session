package me.g1tommy.samplesecuritysession.repository;

import me.g1tommy.samplesecuritysession.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
