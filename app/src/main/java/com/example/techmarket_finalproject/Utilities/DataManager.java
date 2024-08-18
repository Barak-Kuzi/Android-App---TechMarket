package com.example.techmarket_finalproject.Utilities;

import com.example.techmarket_finalproject.Models.Category;
import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.R;

import java.util.ArrayList;

public class DataManager {
    private static ArrayList<SliderItems> bannerImages = new ArrayList<>();
    private static ArrayList<Category> categories = new ArrayList<>();

    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();

        // Smart Watches
        allProducts.add(new Product("SW_1", "Apple Watch 45mm", R.drawable.sw_1, 45, 4.7, 638.58,
                "Smart Watch A with multiple features including fitness tracking, sleep monitoring, customizable watch faces, and notifications for calls, texts, and apps. It also has a sleek design and is water-resistant up to 50 meters.",
                CategoryEnum.SMART_WATCHES, true, 15));
        allProducts.add(new Product("SW_2", "Garmin Forerunner", R.drawable.sw_2, 20, 3.5, 423.84,
                "Smart Watch B with heart rate monitor, GPS tracking, water resistance up to 50 meters, and an extended battery life of up to 10 days. It also includes music control and weather updates.",
                CategoryEnum.SMART_WATCHES, false, 0));
        allProducts.add(new Product("SW_3", "Samsung Galaxy Watch", R.drawable.sw_3, 90, 4.2, 684.21,
                "Smart Watch C with built-in GPS, music storage for up to 500 songs, and long battery life up to 7 days. It also features advanced health metrics like VO2 max and stress tracking.",
                CategoryEnum.SMART_WATCHES, false, 0));

        // Cell Phones
        allProducts.add(new Product("CP_1", "Apple iPhone 15 Pro Max", R.drawable.cp_1, 70, 4.7, 1476.06,
                "Cell Phone A with a high-resolution 48MP camera, vibrant AMOLED display, 128GB storage, and a powerful Snapdragon processor. It also supports fast charging and has a battery life that lasts all day.",
                CategoryEnum.CELL_PHONES, true, 15));
        allProducts.add(new Product("CP_2", "Samsung Galaxy S24", R.drawable.cp_2, 59, 4.0, 991.83,
                "Cell Phone B with a long-lasting 5000mAh battery, fast charging capabilities, dual SIM support, and a 6.4-inch FHD+ display. It also features a triple camera setup for stunning photos.",
                CategoryEnum.CELL_PHONES, false, 0));
        allProducts.add(new Product("CP_3", "Xiaomi 14", R.drawable.cp_3, 90, 4.6, 1046.58,
                "Cell Phone C with 5G support, a powerful octa-core processor, 256GB of internal storage, and a stunning 6.7-inch Super AMOLED display. It also includes advanced camera features like night mode and portrait mode.",
                CategoryEnum.CELL_PHONES, false, 0));

        // Televisions
        allProducts.add(new Product("TV_1", "LG OLED A3", R.drawable.tv_1, 80, 4.8, 1210.32,
                "Television A with 4K Ultra HD resolution, HDR support, and built-in streaming apps like Netflix, Hulu, and Disney+. It also features voice control via Amazon Alexa and Google Assistant.",
                CategoryEnum.TELEVISIONS, true, 15));
        allProducts.add(new Product("TV_2", "Samsung 55'' 4K", R.drawable.tv_2, 20, 3.5, 1937.75,
                "Television B with Smart TV features, voice control, a sleek slim design, and a 55-inch screen. It also includes Dolby Vision and Atmos for an immersive viewing experience.",
                CategoryEnum.TELEVISIONS, false, 0));
        allProducts.add(new Product("TV_3", "Sony Bravia 4K", R.drawable.tv_3, 210, 4.7, 1302.59,
                "Television C with HDR support, a 120Hz refresh rate for smooth motion, excellent sound quality with built-in subwoofers, and compatibility with Apple AirPlay and Google Cast.",
                CategoryEnum.TELEVISIONS, false, 0));

        // Laptops
        allProducts.add(new Product("LP_1", "Asus ROG Zephyrus", R.drawable.lp_1, 62, 4.4, 2522.92,
                "Laptop A with an Intel i7 processor, 16GB RAM, and 512GB SSD for fast performance. It also features a high-resolution 15.6-inch display, backlit keyboard, and long battery life of up to 12 hours.",
                CategoryEnum.LAPTOPS, true, 15));
        allProducts.add(new Product("LP_2", "Apple MacBook Air M2", R.drawable.lp_2, 46, 3.9, 1145.90,
                "Laptop B with 16GB RAM, 1TB HDD, dedicated graphics card for gaming and creative work, and a 17.3-inch FHD display. It also includes advanced cooling technology to keep the laptop running smoothly.",
                CategoryEnum.LAPTOPS, false, 0));
        allProducts.add(new Product("LP_3", "Lenovo ideapad", R.drawable.lp_3, 18, 3.4, 724.48,
                "Laptop C with 512GB SSD storage, high-resolution 4K display, long battery life, and a powerful Intel i9 processor. It also features Thunderbolt 3 ports for fast data transfer and connectivity.",
                CategoryEnum.LAPTOPS, false, 0));

        // Desktops
        allProducts.add(new Product("DT_1", "Intel Core i9 14900KF", R.drawable.dt_1, 77, 4.8, 4899.01,
                "Desktop A with AMD Ryzen 5 processor, 16GB RAM, 512GB SSD, and a sleek design. It also includes a powerful GPU for gaming and video editing, and multiple USB ports for connectivity.",
                CategoryEnum.DESKTOPS, true, 15));
        allProducts.add(new Product("DT_2", "Intel Core i3 14100", R.drawable.dt_2, 46, 3.3, 1462.91,
                "Desktop B with NVIDIA GTX 1660 graphics card, 8GB RAM, 1TB HDD, and a compact design. It's perfect for gaming and productivity tasks, and includes built-in Wi-Fi and Bluetooth.",
                CategoryEnum.DESKTOPS, false, 0));
        allProducts.add(new Product("DT_3", "Intel Core i7", R.drawable.dt_3, 54, 4.1, 1946.07,
                "Desktop C with 1TB HDD, 8GB RAM, powerful Intel i5 processor, and an elegant design. It also features a high-quality audio system and multiple expansion slots for future upgrades.",
                CategoryEnum.DESKTOPS, false, 0));

        // Headphones
        allProducts.add(new Product("HP_1", "Logitech G733", R.drawable.hp_1, 92, 4.6, 166.15,
                "Headphones A with active noise cancellation, wireless Bluetooth connectivity, and up to 20 hours of battery life. These headphones also feature a comfortable over-ear design and high-quality sound.",
                CategoryEnum.HEADPHONES, true, 15));
        allProducts.add(new Product("HP_2", "Apple AirPods 3", R.drawable.hp_2, 60, 4.8, 201.05,
                "Headphones B with powerful bass, foldable design for easy storage, and built-in microphone for hands-free calls. They also offer wired and wireless connectivity options.",
                CategoryEnum.HEADPHONES, false, 0));
        allProducts.add(new Product("HP_3", "SteelSeries Arctis Nova", R.drawable.hp_3, 43, 4.2, 313.25,
                "Headphones C with high-resolution audio, comfortable memory foam ear cushions, and up to 30 hours of battery life. These headphones also feature touch controls and voice assistant support.",
                CategoryEnum.HEADPHONES, false, 0));

        return allProducts;
    }

    public static ArrayList<Category> getCategories() {
        categories.add(new Category("Phones", 0, R.drawable.cp_cat));
        categories.add(new Category("Headphones", 1, R.drawable.hp_cat));
        categories.add(new Category("Desktops", 2, R.drawable.dt_cat));
        categories.add(new Category("Smartwatches", 3, R.drawable.sw_cat));
        categories.add(new Category("Laptops", 4, R.drawable.lp_cat));
        categories.add(new Category("Televisions", 5, R.drawable.tv_cat));

        return categories;
    }

    public static ArrayList<SliderItems> getBannerImages() {
        bannerImages.add(new SliderItems(R.drawable.banner_1));
        bannerImages.add(new SliderItems(R.drawable.banner_2));

        return bannerImages;
    }

}
