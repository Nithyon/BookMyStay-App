/**
 * Use Case 2: Room Availability (Scattered Variables Approach)
 *
 * Version: 2.0
 *
 * Problem Demonstrated:
 * - Multiple scattered variables for room types
 * - Hard to scale (100 room types = 100 variables)
 * - Error-prone state management
 * - No centralized source of truth
 *
 * This use case intentionally shows the WRONG approach
 * to illustrate why UC3's HashMap solution is superior.
 */

public class UseCase2RoomAvailability {

    // ❌ PROBLEMATIC: Scattered variables
    private static int singleAvailable = 5;
    private static int doubleAvailable = 3;
    private static int suiteAvailable = 2;

    /**
     * Display scattered room availability
     */
    public static void displayAvailability() {
        System.out.println("\n--- Room Availability (Scattered Variables) ---");
        System.out.println("Single Room: " + singleAvailable);
        System.out.println("Double Room: " + doubleAvailable);
        System.out.println("Suite Room: " + suiteAvailable);
    }

    /**
     * Check availability for a specific room type
     * ❌ Problem: Manual conditional logic
     */
    public static int checkAvailability(String roomType) {
        if (roomType == null || roomType.trim().isEmpty()) {
            return 0;
        }

        switch (roomType) {
            case "Single Room":
                return singleAvailable;
            case "Double Room":
                return doubleAvailable;
            case "Suite Room":
                return suiteAvailable;
            default:
                return 0;
        }
    }

    /**
     * Update availability for a specific room type
     * ❌ Problem: Each room type requires separate code
     */
    public static void updateAvailability(String roomType, int newCount) {
        if (roomType == null || roomType.trim().isEmpty()) {
            return;
        }

        switch (roomType) {
            case "Single Room":
                singleAvailable = Math.max(0, newCount);
                break;
            case "Double Room":
                doubleAvailable = Math.max(0, newCount);
                break;
            case "Suite Room":
                suiteAvailable = Math.max(0, newCount);
                break;
            default:
                System.out.println("Unknown room type: " + roomType);
        }
    }

    /**
     * Decrement availability when a booking is made
     * ❌ Problem: New room type = new method or modify switch
     */
    public static boolean decrementAvailability(String roomType) {
        int current = checkAvailability(roomType);

        if (current <= 0) {
            return false;
        }

        updateAvailability(roomType, current - 1);
        return true;
    }

    public static void main(String[] args) {

        System.out.println("=====================================");
        System.out.println(" Use Case 2: Scattered Variables ");
        System.out.println("=====================================");

        System.out.println("\n📊 Initial State:");
        displayAvailability();

        System.out.println("\n🔍 Checking Single Room availability:");
        System.out.println("Available: " + checkAvailability("Single Room"));

        System.out.println("\n📝 Booking 1 Single Room...");
        boolean booked = decrementAvailability("Single Room");
        System.out.println("Booking success: " + booked);

        System.out.println("\n📊 After 1 booking:");
        displayAvailability();

        System.out.println("\n⚠️  Problems with this approach:");
        System.out.println("1. ❌ Not scalable → 100 room types = 100 variables");
        System.out.println("2. ❌ Error-prone → easy to miss a room type in switch");
        System.out.println("3. ❌ Hard to maintain → adding rooms requires code changes");
        System.out.println("4. ❌ No unified interface → scattered state management");
        System.out.println("5. ❌ Difficult to iterate → can't loop over room types");

        System.out.println("\n✅ Solution in UC3: Use HashMap for centralized state!");
    }
}
