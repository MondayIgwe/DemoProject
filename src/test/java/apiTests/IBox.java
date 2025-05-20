package apiTests;

import java.time.LocalDate;

public interface IBox {

    ConcreteBox getBoxInstance();
    LocalDate getDate(String date);
    String getPayload(String name, String job);
}
