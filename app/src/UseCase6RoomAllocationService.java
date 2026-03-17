import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Version: 6.0
 *
 * Demonstrates:
 * - FIFO request processing
 * - Unique room ID assignment using Set
 * - Room type to allocated IDs mapping using HashMap<String, Set<String>>
 * - Immediate inventory synchronization after allocation
 */
public class UseCase6RoomAllocationService {

    static class Reservation {
        private final String requestId;
        private final String guestName;
        private final String roomType;
        private final int nights;

        public Reservation(String requestId, String guestName, String roomType, int nights) {
            this.requestId = requestId;
            this.guestName = guestName;
            this.roomType = roomType;
            this.nights = nights;
        }

        public String getRequestId() {
            return requestId;
        }

        public String getGuestName() {
            return guestName;
        }

        public String getRoomType() {
            return roomType;
        }

        public int getNights() {
            return nights;
        }
    }

    static class BookingRequestQueue {
        private final Queue<Reservation> requestQueue = new ArrayDeque<>();

        public void submitRequest(Reservation reservation) {
            if (reservation != null) {
                requestQueue.offer(reservation);
            }
        }

        public Reservation dequeueRequest() {
            return requestQueue.poll();
        }

        public boolean isEmpty() {
            return requestQueue.isEmpty();
        }

        public void displayQueue() {
            System.out.println("\n--- Incoming Booking Requests (FIFO Order) ---");
            if (requestQueue.isEmpty()) {
                System.out.println("No pending booking requests.");
                return;
            }

            int position = 1;
            for (Reservation reservation : requestQueue) {
                System.out.println(position + ". "
                        + reservation.getRequestId() + " | "
                        + reservation.getGuestName() + " | "
                        + reservation.getRoomType() + " | Nights: "
                        + reservation.getNights());
                position++;
            }
        }
    }

    static class InventoryService {
        private final Map<String, Integer> inventory = new LinkedHashMap<>();

        public InventoryService() {
            inventory.put("Single Room", 2);
            inventory.put("Double Room", 1);
            inventory.put("Suite Room", 1);
        }

        public int getAvailability(String roomType) {
            if (roomType == null || roomType.trim().isEmpty()) {
                return 0;
            }
            return Math.max(0, inventory.getOrDefault(roomType, 0));
        }

        public boolean decrementAvailability(String roomType) {
            int available = getAvailability(roomType);
            if (available <= 0) {
                return false;
            }
            inventory.put(roomType, available - 1);
            return true;
        }

        public void displayInventory() {
            System.out.println("\n--- Current Inventory State ---");
            for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
    }

    static class BookingService {
        private final InventoryService inventoryService;
        private final Set<String> allocatedRoomIds = new HashSet<>();
        private final Map<String, Set<String>> allocationsByRoomType = new HashMap<>();
        private final Map<String, Integer> roomTypeCounters = new HashMap<>();

        public BookingService(InventoryService inventoryService) {
            this.inventoryService = inventoryService;
        }

        public void processQueuedRequests(BookingRequestQueue requestQueue) {
            while (!requestQueue.isEmpty()) {
                Reservation reservation = requestQueue.dequeueRequest();
                confirmReservation(reservation);
            }
        }

        private void confirmReservation(Reservation reservation) {
            if (reservation == null) {
                return;
            }

            String roomType = reservation.getRoomType();
            System.out.println("\nProcessing request: " + reservation.getRequestId());

            if (inventoryService.getAvailability(roomType) <= 0) {
                System.out.println("Reservation failed for " + reservation.getGuestName()
                        + " -> No availability for " + roomType);
                return;
            }

            String roomId = generateUniqueRoomId(roomType);

            if (!allocatedRoomIds.add(roomId)) {
                System.out.println("Reservation failed due to duplicate room ID conflict.");
                return;
            }

            allocationsByRoomType
                    .computeIfAbsent(roomType, key -> new LinkedHashSet<>())
                    .add(roomId);

            boolean inventoryUpdated = inventoryService.decrementAvailability(roomType);
            if (!inventoryUpdated) {
                allocatedRoomIds.remove(roomId);
                allocationsByRoomType.get(roomType).remove(roomId);
                System.out.println("Reservation rolled back due to inventory synchronization failure.");
                return;
            }

            System.out.println("Reservation confirmed -> Guest: " + reservation.getGuestName()
                    + " | Room Type: " + roomType
                    + " | Assigned Room ID: " + roomId);
        }

        private String generateUniqueRoomId(String roomType) {
            String prefix = buildPrefix(roomType);
            int sequence = roomTypeCounters.getOrDefault(roomType, 0) + 1;
            String candidate = prefix + "-" + String.format("%03d", sequence);

            while (allocatedRoomIds.contains(candidate)) {
                sequence++;
                candidate = prefix + "-" + String.format("%03d", sequence);
            }

            roomTypeCounters.put(roomType, sequence);
            return candidate;
        }

        private String buildPrefix(String roomType) {
            if (roomType == null || roomType.trim().isEmpty()) {
                return "R";
            }

            String[] parts = roomType.trim().split("\\s+");
            StringBuilder prefixBuilder = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    prefixBuilder.append(Character.toUpperCase(part.charAt(0)));
                }
            }

            if (prefixBuilder.length() == 0) {
                return "R";
            }

            return prefixBuilder.toString();
        }

        public void displayAllocationsByRoomType() {
            System.out.println("\n--- Allocated Room IDs by Room Type ---");
            if (allocationsByRoomType.isEmpty()) {
                System.out.println("No successful allocations yet.");
                return;
            }

            for (Map.Entry<String, Set<String>> entry : allocationsByRoomType.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }

        public int getTotalAllocatedCount() {
            return allocatedRoomIds.size();
        }
    }

    public static void main(String[] args) {

        System.out.println("======================================================");
        System.out.println(" Use Case 6: Reservation Confirmation & Allocation ");
        System.out.println("======================================================");

        InventoryService inventoryService = new InventoryService();
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        requestQueue.submitRequest(new Reservation("BR-201", "Aarav", "Single Room", 2));
        requestQueue.submitRequest(new Reservation("BR-202", "Meera", "Suite Room", 1));
        requestQueue.submitRequest(new Reservation("BR-203", "Rohan", "Single Room", 1));
        requestQueue.submitRequest(new Reservation("BR-204", "Sara", "Double Room", 2));
        requestQueue.submitRequest(new Reservation("BR-205", "Kabir", "Suite Room", 1));

        inventoryService.displayInventory();
        requestQueue.displayQueue();

        BookingService bookingService = new BookingService(inventoryService);

        System.out.println("\nStarting allocation process...");
        bookingService.processQueuedRequests(requestQueue);

        bookingService.displayAllocationsByRoomType();
        inventoryService.displayInventory();

        System.out.println("\nTotal confirmed allocations: " + bookingService.getTotalAllocatedCount());
        System.out.println("Allocation completed with inventory consistency and no double-booking.");
    }
}
