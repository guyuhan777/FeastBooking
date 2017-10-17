package com.iplay.feastbooking.gson.hotelDetail;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 2017/10/13.
 */

public class HotelDetail {

    public Address address;

    public List<BanquetHall> banquetHalls;

    public String contact;

    public String description;

    public String email;

    public List<Feast> feasts;

    public int id;

    public String name;

    public List<String> pictureUrls;

    public double[] priceRange;

    public double rating;

    public int[] tableRange;

    public String telephone;

    @Override
    public String toString() {
        return "HotelDetail{" +
                "address=" + address +
                ", banquetHalls=" + banquetHalls +
                ", contact='" + contact + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", feasts=" + feasts +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", pictureUrls=" + pictureUrls +
                ", priceRange=" + Arrays.toString(priceRange) +
                ", rating=" + rating +
                ", tableRange=" + Arrays.toString(tableRange) +
                ", telephone='" + telephone + '\'' +
                '}';
    }

    public static class Address{
        public String city;

        public String district;

        public String street;
    }

}
