package experience.demo.Exception;

public class NegativeStockException extends RuntimeException {
    public NegativeStockException() {
        super("Ürün miktarı eksi değerde olamaz.");
    }

    public NegativeStockException(String message) {
        super(message);
    }
}