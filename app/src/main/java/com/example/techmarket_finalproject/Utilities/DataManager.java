package com.example.techmarket_finalproject.Utilities;

import com.example.techmarket_finalproject.Models.CategoryEnum;
import com.example.techmarket_finalproject.Models.Product;
import com.example.techmarket_finalproject.R;

import java.util.ArrayList;

public class DataManager {

    public static ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<>();

        // Smart Watches
        allProducts.add(new Product("SW_1", "Smart Watch A", R.drawable.sw_1, 100, 4.5, 199.99,
                "Smart Watch A with multiple features including fitness tracking, sleep monitoring, customizable watch faces, and notifications for calls, texts, and apps. It also has a sleek design and is water-resistant up to 50 meters.",
                CategoryEnum.SMART_WATCHES));
        allProducts.add(new Product("SW_2", "Smart Watch B", R.drawable.sw_2, 120, 4.0, 149.99,
                "Smart Watch B with heart rate monitor, GPS tracking, water resistance up to 50 meters, and an extended battery life of up to 10 days. It also includes music control and weather updates.",
                CategoryEnum.SMART_WATCHES));
        allProducts.add(new Product("SW_3", "Smart Watch C", R.drawable.sw_3, 90, 4.2, 179.99,
                "Smart Watch C with built-in GPS, music storage for up to 500 songs, and long battery life up to 7 days. It also features advanced health metrics like VO2 max and stress tracking.",
                CategoryEnum.SMART_WATCHES));

        // Cell Phones
        allProducts.add(new Product("CP_1", "Cell Phone A", R.drawable.cp_1, 200, 4.7, 699.99,
                "Cell Phone A with a high-resolution 48MP camera, vibrant AMOLED display, 128GB storage, and a powerful Snapdragon processor. It also supports fast charging and has a battery life that lasts all day.",
                CategoryEnum.CELL_PHONES));
        allProducts.add(new Product("CP_2", "Cell Phone B", R.drawable.cp_2, 150, 4.3, 599.99,
                "Cell Phone B with a long-lasting 5000mAh battery, fast charging capabilities, dual SIM support, and a 6.4-inch FHD+ display. It also features a triple camera setup for stunning photos.",
                CategoryEnum.CELL_PHONES));
        allProducts.add(new Product("CP_3", "Cell Phone C", R.drawable.cp_3, 180, 4.6, 799.99,
                "Cell Phone C with 5G support, a powerful octa-core processor, 256GB of internal storage, and a stunning 6.7-inch Super AMOLED display. It also includes advanced camera features like night mode and portrait mode.",
                CategoryEnum.CELL_PHONES));

        // Televisions
        allProducts.add(new Product("TV_1", "Television A", R.drawable.tv_1, 250, 4.8, 999.99,
                "Television A with 4K Ultra HD resolution, HDR support, and built-in streaming apps like Netflix, Hulu, and Disney+. It also features voice control via Amazon Alexa and Google Assistant.",
                CategoryEnum.TELEVISIONS));
        allProducts.add(new Product("TV_2", "Television B", R.drawable.tv_2, 220, 4.5, 799.99,
                "Television B with Smart TV features, voice control, a sleek slim design, and a 55-inch screen. It also includes Dolby Vision and Atmos for an immersive viewing experience.",
                CategoryEnum.TELEVISIONS));
        allProducts.add(new Product("TV_3", "Television C", R.drawable.tv_3, 210, 4.7, 899.99,
                "Television C with HDR support, a 120Hz refresh rate for smooth motion, excellent sound quality with built-in subwoofers, and compatibility with Apple AirPlay and Google Cast.",
                CategoryEnum.TELEVISIONS));

        // Computer Monitors
//        allProducts.add(new Product("CM001", "Monitor A", R.drawable.monitor_a, 120, 4.4, 299.99,
//                "Monitor A with a 144Hz refresh rate, 1ms response time, G-Sync support, and a 27-inch display. It's perfect for gaming and provides smooth, tear-free gameplay.",
//                Category.COMPUTER_MONITORS));
//        allProducts.add(new Product("CM002", "Monitor B", R.drawable.monitor_b, 110, 4.2, 249.99,
//                "Monitor B with an IPS panel for accurate colors, 1080p resolution, wide viewing angles, and a 24-inch screen. It also includes built-in speakers and multiple connectivity options.",
//                Category.COMPUTER_MONITORS));
//        allProducts.add(new Product("CM003", "Monitor C", R.drawable.monitor_c, 130, 4.5, 349.99,
//                "Monitor C with a curved display for an immersive experience, 1440p resolution, ergonomic stand for comfortable viewing, and multiple ports including HDMI and DisplayPort.",
//                Category.COMPUTER_MONITORS));

        // Laptops
        allProducts.add(new Product("LP_1", "Laptop A", R.drawable.lp_1, 300, 4.7, 1199.99,
                "Laptop A with an Intel i7 processor, 16GB RAM, and 512GB SSD for fast performance. It also features a high-resolution 15.6-inch display, backlit keyboard, and long battery life of up to 12 hours.",
                CategoryEnum.LAPTOPS));
        allProducts.add(new Product("LP_2", "Laptop B", R.drawable.lp_2, 280, 4.5, 999.99,
                "Laptop B with 16GB RAM, 1TB HDD, dedicated graphics card for gaming and creative work, and a 17.3-inch FHD display. It also includes advanced cooling technology to keep the laptop running smoothly.",
                CategoryEnum.LAPTOPS));
        allProducts.add(new Product("LP_3", "Laptop C", R.drawable.lp_3, 320, 4.8, 1399.99,
                "Laptop C with 512GB SSD storage, high-resolution 4K display, long battery life, and a powerful Intel i9 processor. It also features Thunderbolt 3 ports for fast data transfer and connectivity.",
                CategoryEnum.LAPTOPS));

        // Desktops
        allProducts.add(new Product("DT_1", "Desktop A", R.drawable.dt_1, 150, 4.6, 899.99,
                "Desktop A with AMD Ryzen 5 processor, 16GB RAM, 512GB SSD, and a sleek design. It also includes a powerful GPU for gaming and video editing, and multiple USB ports for connectivity.",
                CategoryEnum.DESKTOPS));
        allProducts.add(new Product("DT_2", "Desktop B", R.drawable.dt_2, 170, 4.3, 799.99,
                "Desktop B with NVIDIA GTX 1660 graphics card, 8GB RAM, 1TB HDD, and a compact design. It's perfect for gaming and productivity tasks, and includes built-in Wi-Fi and Bluetooth.",
                CategoryEnum.DESKTOPS));
        allProducts.add(new Product("DT_3", "Desktop C", R.drawable.dt_3, 160, 4.5, 999.99,
                "Desktop C with 1TB HDD, 8GB RAM, powerful Intel i5 processor, and an elegant design. It also features a high-quality audio system and multiple expansion slots for future upgrades.",
                CategoryEnum.DESKTOPS));

        // Headphones
        allProducts.add(new Product("HP_1", "Headphones A", R.drawable.hp_1, 200, 4.5, 199.99,
                "Headphones A with active noise cancellation, wireless Bluetooth connectivity, and up to 20 hours of battery life. These headphones also feature a comfortable over-ear design and high-quality sound.",
                CategoryEnum.HEADPHONES));
        allProducts.add(new Product("HP_2", "Headphones B", R.drawable.hp_2, 180, 4.3, 149.99,
                "Headphones B with powerful bass, foldable design for easy storage, and built-in microphone for hands-free calls. They also offer wired and wireless connectivity options.",
                CategoryEnum.HEADPHONES));
        allProducts.add(new Product("HP_3", "Headphones C", R.drawable.hp_3, 220, 4.7, 249.99,
                "Headphones C with high-resolution audio, comfortable memory foam ear cushions, and up to 30 hours of battery life. These headphones also feature touch controls and voice assistant support.",
                CategoryEnum.HEADPHONES));


        return allProducts;
    }


}
