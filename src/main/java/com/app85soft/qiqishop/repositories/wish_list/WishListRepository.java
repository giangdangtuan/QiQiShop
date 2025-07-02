package com.app85soft.qiqishop.repositories.wish_list;

import com.app85soft.qiqishop.entities.wish_list.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer>, WishListRepositoryCustom {
}
