public interface Transmitter {

    public void transmit(Object obj);
    public void sub(Receiver receiver);
    public void unsub(Receiver receiver);
}
