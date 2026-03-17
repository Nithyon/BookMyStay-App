import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Use Case 4: Room Search & Availability Check
 *
 * Version: 4.0
 *
 * Demonstrates:
 * - Read-only inventory access
 * - Defensive filtering of unavailable room types
 * - Separation of concerns between search and inventory mutation
 */
public class UseCase4RoomSearch {

    static class Room {
        private final String roomType;
        private final double pricePerNight;
        private final String amenities;

        public Room(String roomType, double pricePerNight, String amenities) {
            this.roomType = roomType;
            this.pricePerNight = pricePerNight;
            this.amenities = amenities;
        }

        public String getRoomType() {
            return roomType;
        }

        public double getPricePerNight() {
            return pricePerNight;
        }

        public String getAmenities() {
            return amenities;
        }
    }

    static class InventoryService {
        private final Map<String, Integer> inventory;

        public InventoryService() {
            inventory = new LinkedHashMap<>();
            inventory.put("Single Room", 5);
            inventory.put("Double Room", 0);
            inventory.put("Suite Room", 2);
        }

        public int getAvailability(String roomType) {
            if (roomType == null || roomType.trim().isEmpty()) {
                return 0;
            }
            return Math.max(0, inventory.getOrDefault(roomType, 0));
        }

        public Map<String, Integer> getInventorySnapshot() {
            return Collections.unmodifiableMap(new LinkedHashMap<>(inventory));
        }

        public void displayInventorySnapshot() {
            System.out.println("\n--- Inventory Snapshot (Read-Only) ---");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    static class RoomSearchService {
        private final InventoryService inventoryService;
        private final Map<String, Room> roomCatalog;

        public RoomSearchService(InventoryService inventoryService, Map<String, Room> roomCatalog) {
            this.inventoryService = inventoryService;
            this.roomCatalog = new LinkedHashMap<>(roomCatalog);
        }

        public List<Room> searchAvailableRooms() {
            List<Room> availableRooms = new ArrayList<>();

            for (Map.Entry<String, Room> entry : roomCatalog.entrySet()) {
                String roomType = entry.getKey();
                Room room = entry.getValue();

                if (roomType == null || room == null) {
                    continue;
                }

                int availableCount = inventoryService.getAvailability(roomType);
                if (availableCount > 0) {
                    availableRooms.add(room);
                }
            }

            return availableRooms;
        }

        public void displayAvailableRooms() {
            List<Room> availableRooms = searchAvailableRooms();

            System.out.println("\n--- Available Rooms for Guest Search ---");
            if (availableRooms.isEmpty()) {
                System.out.println("No rooms available right now.");
                return;
            }

            for (Room room : availableRooms) {
                System.out.println(room.getRoomType()
                        + " | Price: " + room.getPricePerNight()
                        + " | Amenities: " + room.getAmenities()
                        + " | Available: " + inventoryService.getAvailability(room.getRoomType()));
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Use Case 4: Room Search Service ");
        System.out.println("=====================================");

        InventoryService inventoryService = new InventoryService();

        Map<String, Room> roomCatalog = new LinkedHashMap<>();
        roomCatalog.put("Single Room", new Room("Single Room", 3500.0, "WiFi, Queen Bed"));
        roomCatalog.put("Double Room", new Room("Double Room", 5000.0, "WiFi, Twin Beds"));
        roomCatalog.put("Suite Room", new Room("Suite Room", 8500.0, "WiFi, King Bed, Balcony"));

        RoomSearchService roomSearchService = new RoomSearchService(inventoryService, roomCatalog);

        inventoryService.displayInventorySnapshot();

        System.out.println("\nGuest initiates room search...");
        roomSearchService.displayAvailableRooms();

        System.out.println("\nVerifying inventory remains unchanged after search...");
        inventoryService.displayInventorySnapshot();

        System.out.println("\nSearch completed successfully without state mutation.");
    }
}
