package com.cs.repositories;

import com.cs.entities.Advertisement;
import com.cs.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {
    //pagination
    @Query("from Advertisement as a where a.user.userId=:userId")
    //currentPage-page
    //Advertisement per page-10
    Page<Advertisement> findAdvertisementByUser(@Param("userId") int userId, Pageable pageable);

    Optional<List<Advertisement>> findByUser(User user);
}
