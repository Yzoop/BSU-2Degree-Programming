public interface Record {

    String getStorageId();

    String getProductId();

    String getProductName();

    Time getArrivalTime();

    int getQuantity();

    int getShelfInDays();

    float getSinglePrice();

}
