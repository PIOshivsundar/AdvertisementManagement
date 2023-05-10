package com.cs.services;

import com.cs.entities.Advertisement;

import java.util.List;

public interface AdvertisementService {
    List<Advertisement> getAllAdvertisements();
    void deleteAdvertisement(int id);
}
