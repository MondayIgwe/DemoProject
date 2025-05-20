package apiTests;

import java.time.LocalDate;

public class AbstractBox implements IBox {
    @Override
    public ConcreteBox getBoxInstance() {
        return new ConcreteBox().getBoxInstance();
    }

    @Override
    public LocalDate getDate(String date) {
        return LocalDate.now();
    }

    @Override
    public String getPayload(String name, String job) {
        return """
                {
                    "name": "%s",
                    "job": "%s"
                }
                
                """.formatted(name, job);
    }
}
