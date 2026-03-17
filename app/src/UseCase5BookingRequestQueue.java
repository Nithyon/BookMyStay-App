import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * Version: 5.0
 *
 * Demonstrates:
 * - Queue-based booking request intake
 * - FIFO ordering for fairness
 * - Decoupling request intake from room allocation
 */
public class UseCase5BookingRequestQueue {

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

        @Override
        public String toString() {
            return requestId + " | Guest: " + guestName
                    + " | Room: " + roomType
                    + " | Nights: " + nights;
        }
    }

    static class BookingRequestQueueService {
        private final Queue<Reservation> requestQueue = new ArrayDeque<>();

        public void submitRequest(Reservation reservation) {
            if (reservation == null) {
                return;
            }

            requestQueue.offer(reservation);
            System.out.println("Request queued -> " + reservation.getRequestId());
        }

        public Reservation peekNextRequest() {
            return requestQueue.peek();
        }

        public int size() {
            return requestQueue.size();
        }

        public void displayQueue() {
            System.out.println("\n--- Current Booking Request Queue (FIFO) ---");
            if (requestQueue.isEmpty()) {
                System.out.println("No requests in queue.");
                return;
            }

            int position = 1;
            for (Reservation reservation : requestQueue) {
                System.out.println(position + ". " + reservation);
                position++;
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("===============================================");
        System.out.println(" Use Case 5: Booking Request Intake (FIFO) ");
        System.out.println("===============================================");

        BookingRequestQueueService queueService = new BookingRequestQueueService();

        queueService.submitRequest(new Reservation("BR-101", "Aarav", "Single Room", 2));
        queueService.submitRequest(new Reservation("BR-102", "Meera", "Suite Room", 1));
        queueService.submitRequest(new Reservation("BR-103", "Rohan", "Double Room", 3));
        queueService.submitRequest(new Reservation("BR-104", "Sara", "Single Room", 1));

        queueService.displayQueue();

        Reservation next = queueService.peekNextRequest();
        System.out.println("\nNext request to be processed: "
                + (next != null ? next.getRequestId() : "None"));

        System.out.println("Total queued requests: " + queueService.size());
        System.out.println("No room allocation or inventory updates are performed in UC5.");
    }
}
