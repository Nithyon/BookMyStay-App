import java.util.HashMap;

/**
 * Use Case 3: Centralized Room Inventory Management
 *
 * Version: 3.0
 *
 * Demonstrates:
 * - HashMap usage
 * - Centralized state management
 * - Encapsulation
 */

class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor initializes inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display all inventory
    public void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");

        for (String roomType : inventory.keySet()) {
            System.out.println(roomType + " : " + inventory.get(roomType));
        }
    }
}

public class UseCase3InventorySetup {

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Centralized Room Inventory System ");
        System.out.println("=====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display inventory
        inventory.displayInventory();

        // Example: Check availability
        System.out.println("\nAvailable Single Rooms: " +
                inventory.getAvailability("Single Room"));

        // Example: Update availability
        inventory.updateAvailability("Single Room", 4);

        System.out.println("\nAfter Booking 1 Single Room:");
        inventory.displayInventory();

        System.out.println("\nSystem initialized successfully!");
    }
}
