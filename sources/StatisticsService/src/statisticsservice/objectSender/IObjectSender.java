package statisticsservice.objectSender;

public interface IObjectSender {
    <T> void send(T object);
}
